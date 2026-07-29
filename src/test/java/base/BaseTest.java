package base;

import auth.LoginManager;
import auth.SkipLogin;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.CommonComponent;
import pages.HomePage;

import java.lang.reflect.Method;
import java.util.Properties;

public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected PlaywrightFactory pf;
    protected Properties prop;

    protected CommonComponent commonComponent;
    protected HomePage homePage;

    @BeforeSuite
    public void loginBeforeSuite() {
        PlaywrightFactory tempPf = new PlaywrightFactory();
        Properties tempProp = tempPf.initProp();
        // 实例化 LoginManager 并执行登录
        LoginManager loginManager = new LoginManager(tempPf, tempProp);
        loginManager.loginNStore();
    }

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional String browserName) {
        pf = new PlaywrightFactory();
        prop = pf.initProp();
        if (browserName != null && !browserName.isBlank()) {
            prop.setProperty("browser", browserName);
        }
        pf.initBrowser(prop);
    }

    @BeforeMethod
    public void setUpMethod(Method method, ITestResult result) {
        boolean isSkipLogin = method.isAnnotationPresent(SkipLogin.class);
        boolean useLoginState = !isSkipLogin;
        log.info("当前执行用例: {} | 是否加载登录态: {}", method.getName(), useLoginState);
        // 每个用例开始前，创建一个全新的 Context/Page 并导航到目标 URL，实现极速隔离
        Page page = pf.createContextAndPage(prop.getProperty("url"), useLoginState);
        commonComponent = new CommonComponent(page);
        homePage = new HomePage(page);
        //将page、context存入result，监听器可以安全获取
        result.setAttribute("page", page);
        result.setAttribute("context", PlaywrightFactory.getContext());
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        BrowserContext context = (BrowserContext) result.getAttribute("context");
        Page page = (Page) result.getAttribute("page");
        // 失败保存截图和 trace
        if (ITestResult.FAILURE == result.getStatus() || result.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
            Throwable throwable = result.getThrowable();
            if(throwable instanceof AssertionError){
                log.error("【断言失败】{}", result.getName());
            }else{
                log.error("【程序异常(broken)】{}", result.getName(), throwable);
            }

            pf.saveScreenshot(page);
            pf.saveTrace(context, result.getName());
        } else {
            // 关闭 trace
            if (context != null) {
                try {
                    context.tracing().stop();
                }catch (Exception ignore) {}
            }
        }
        // 每个用例结束后，销毁当前的 Page 和 Context
        pf.closeContextAndPage();
    }

    @AfterTest
    public void tearDown() {
        pf.quitBrowser();
    }
}
