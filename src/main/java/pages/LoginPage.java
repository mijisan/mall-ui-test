package pages;


import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage extends BasePage {

    private final Locator emailBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("E-Mail Address"));
    private final Locator pwdBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
    private final Locator loginBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    private final Locator alert = page.getByText("Warning: ");
    private final Locator forgetPwdLink = page.locator("#content").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Forgotten Password"));

    public LoginPage(Page page) {
        super(page);
    }

    public Locator getForgetPwdLink() {
        return forgetPwdLink;
    }

    public String getLoginPageTitle() {
        return page.title();
    }

    public void addEmail(String email) {
        emailBox.fill(email);
    }

    public void addPwd(String pwd) {
        pwdBox.fill(pwd);
    }

    public AccountPage clickLoginBtn() {
        loginBtn.click();
        return new AccountPage(page);
    }
}
