package base;

import auth.LoginManager;
import auth.SkipLogin;
import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.HomePage;

import java.lang.reflect.Method;
import java.util.Properties;

@Slf4j
public class BaseTest {
    protected PlaywrightFactory pf;
    protected Properties prop;

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
    public void setUpMethod(Method method) {
        boolean isSkipLogin = method.isAnnotationPresent(SkipLogin.class);
        boolean useLoginState = !isSkipLogin;
        log.info("当前执行用例: {} | 是否加载登录态: {}", method.getName(), useLoginState);
        // 每个用例开始前，创建一个全新的 Context/Page 并导航到目标 URL，实现极速隔离
        Page page = pf.createContextAndPage(prop.getProperty("url"), useLoginState);
        homePage = new HomePage(page);
    }

    @AfterMethod
    public void tearDownMethod() {
        // 每个用例结束后，销毁当前的 Page 和 Context
        pf.closeContextAndPage();
    }

    @AfterTest
    public void tearDown() {
        pf.quitBrowser();
    }
}
