package auth;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import constants.AppConstants;
import factory.PlaywrightFactory;
import listeners.AllureReportListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.CommonComponent;
import pages.HomePage;
import pages.LoginPage;

import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

public class LoginManager {
    private static final Logger log = LoggerFactory.getLogger(LoginManager.class);

    private final PlaywrightFactory pf;
    private final Properties prop;

    public LoginManager(PlaywrightFactory pf, Properties prop) {
        this.pf = pf;
        this.prop = prop;
    }

    public void loginNStore() {
        log.info("====== 开始执行一次性全局登录 ======");
        // 1. 初始化临时浏览器
        pf.initBrowser(prop);
        // 2. 创建一个纯净的页面（不加载历史状态）
        Page page = pf.createContextAndPage(prop.getProperty("url"), false);
        // 3. 执行具体的业务登录步骤
        loginSteps(page);
        // 4. 将认证后的状态（Cookie/LocalStorage）保存到本地 JSON 文件
        PlaywrightFactory.getContext().storageState(new BrowserContext.StorageStateOptions()
                .setPath(Paths.get("auth.json")));
        log.info("====== 全局登录状态已成功保存至 auth.json ======");
        // 5. 销毁临时浏览器实例，释放资源
        pf.quitBrowser();
    }

    /**
     * 登录步骤
     */
    private void loginSteps(Page page) {
        CommonComponent commonComponent = new CommonComponent(page);
        LoginPage loginPage = commonComponent.openLoginPage();
        log.info("正在输入账号密码进行认证...");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
        // 确保登录完成再进行下一步，比如等待首页的某个特有元素出现，或者 URL 发生变化
        page.waitForURL(Pattern.compile(".*" + AppConstants.ACCOUNT_PAGE_URL + ".*"));
    }

}
