package pages;


import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

public class LoginPage extends BasePage {

    private final Locator emailBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("E-Mail Address"));
    private final Locator pwdBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
    private final Locator loginBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    private final Locator alert = page.getByText("Warning: ");
    @Getter
    private final Locator forgetPwdLink = page.locator("#content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Forgotten Password"));

    public LoginPage(Page page) {
        super(page);
    }

    @Step("获取登录页title")
    public String getLoginPageTitle() {
        return page.title();
    }

    @Step("输入邮箱：{0}")
    public void addEmail(String email) {
        emailBox.fill(email);
    }

    @Step("输入密码")
    public void addPwd(String pwd) {
        pwdBox.fill(pwd);
    }

    @Step("点击登录按钮并跳转到登录页")
    public AccountPage clickLoginBtn() {
        loginBtn.click();
        return new AccountPage(page);
    }
}
