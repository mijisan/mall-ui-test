package pages;


import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class LoginPage extends BasePage {

    private final Locator emailBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("E-Mail Address"));
    private final Locator pwdBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
    private final Locator loginBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    private final Locator alertMessage = page.locator(".alert");
    private final Locator forgetPwdLink = page.locator("#content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Forgotten Password"));
    private final Locator registerBtn = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continue"));
    private final Locator myAccountLink = page.locator("#column-right").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("My Account"));

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

    @Step("点击登录按钮")
    public void clickLoginBtnWithWrongData() {
        loginBtn.click();
    }

    public void login(String email, String pwd) {
        addEmail(email);
        addPwd(pwd);
        loginBtn.click();
    }

    @Step("点击注册跳转按钮")
    public RegisterPage clickRegisterBtn() {
        registerBtn.click();
        return new RegisterPage(page);
    }

    @Step("点击忘记密码跳转链接")
    public ForgottenPage clickForgetPwdLink() {
        forgetPwdLink.click();
        return new ForgottenPage(page);
    }

    @Step("点击我的账户跳转链接")
    public void clickMyAccountLink() {
        myAccountLink.click();
    }
}
