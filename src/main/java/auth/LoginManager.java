package auth;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import constants.AppConstants;
import factory.PlaywrightFactory;
import lombok.extern.slf4j.Slf4j;
import pages.CommonComponent;
import pages.LoginPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

@Slf4j
public class LoginManager {

    private static final Path AUTH_FILE = Paths.get("auth.json");

    private final PlaywrightFactory pf;
    private final Properties prop;

    public LoginManager(PlaywrightFactory pf, Properties prop) {
        this.pf = pf;
        this.prop = prop;
    }

    public void ensureLogin() {
        // 第一次运行
        if (!Files.exists(AUTH_FILE)) {
            log.info("auth.json 不存在，开始登录...");
            loginNStore();
            return;
        }
        // 已存在，验证登录态
        if (isAuthValid()) {
            log.info("检测到有效登录态，跳过登录。");
            return;
        }
        log.info("登录态已失效，重新登录。");
        try {
            Files.deleteIfExists(AUTH_FILE);
            log.warn("auth.json 登录态已失效，已删除旧文件，将重新登录");
        } catch (IOException e) {
            log.warn("删除 auth.json 失败", e);
        }
        loginNStore();
    }

    /**
     * 确保登录态可用。优先复用已有的 auth.json，无效时才重新登录。
     */
    private void loginNStore() {
        log.info("====== 开始执行登录 ======");
        pf.initBrowser(prop);
        Page page = pf.createContextAndPage(false);
        page.navigate(prop.getProperty("url").trim(), new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        loginSteps(page);
        PlaywrightFactory.getContext().storageState(
                new BrowserContext.StorageStateOptions()
                        .setPath(AUTH_FILE));
        log.info("====== 登录状态保存完成 ======");
        pf.closeContextAndPage();
        pf.quitBrowser();
    }

    /**
     * 用已有的 auth.json 创建临时 Context，访问账户页验证登录态是否有效。
     * 判断标准：访问账户页后，页面标题是否为 "My Account"。
     */
    private boolean isAuthValid() {
        try {
            pf.initBrowser(prop);
            // 加载 auth.json 创建 Context
            Page page = pf.createContextAndPage(true);
            // 导航到账户页，验证是否仍在登录态
            page.navigate(prop.getProperty("url") + "index.php?route=account/account");
            String title = page.title();
            boolean isAuth = AppConstants.ACCOUNT_PAGE_TITLE.equals(title);
            if (isAuth) {
                log.info("成功导航至账户页，登录态有效");
            }else {
                log.info("无法导航至账户页，登录态无效");
            }
            return isAuth;
        } catch (Exception e) {
            log.warn("验证 auth.json 时发生异常，登录态可能已失效: {}", e.getMessage());
            return false;
        } finally {
            // 无论验证成功与否，都销毁临时浏览器
            pf.closeContextAndPage();
            pf.quitBrowser();
        }
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
