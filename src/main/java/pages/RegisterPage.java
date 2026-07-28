package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class RegisterPage extends BasePage {
    private final Locator header = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Register Account").setLevel(1));
    Locator firstName = page.getByLabel("First Name");
    Locator lastName = page.getByLabel("Last Name");
    Locator email = page.getByLabel("E-Mail");
    Locator phone = page.getByLabel("Telephone");
    Locator pwd = page.locator("#input-password");
    Locator pwd2 = page.getByLabel("Password Confirm");
    Locator subscribeRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes"));
    Locator agreePolicy = page.locator("[name='agree']");
    Locator continueBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));

    public RegisterPage(Page page) {
        super(page);
    }

    @Step("输入名: {0}")
    public void addFirstName(String firstName) {
        getFirstName().fill(firstName);
    }

    @Step("输入姓: {0}")
    public void addLastName(String lastName) {
        getLastName().fill(lastName);
    }

    @Step("输入邮箱: {0}")
    public void addEmail(String email) {
        getEmail().fill(email);
    }

    @Step("输入手机号: {0}")
    public void addPhone(String phone) {
        getPhone().fill(phone);
    }

    @Step("输入密码: {0}")
    public void addPwd(String pwd) {
        getPwd().fill(pwd);
    }

    @Step("输入确认密码: {0}")
    public void addPwd2(String pwd2) {
        getPwd2().fill(pwd2);
    }

    @Step("勾选订阅")
    public void checkSubs() {
        subscribeRadio.check();
    }

    @Step("勾选统一政策")
    public void checkAgreePolicy() {
        agreePolicy.check();
    }

    @Step("点击继续按钮")
    public void clickBtn() {
        continueBtn.click();
    }

    @Step("点击继续按钮")
    public SuccessPage clickBtnNOpenSuccessPage() {
        continueBtn.click();
        return new SuccessPage(page);
    }

    public Locator alertMsg(String msg) {
        return page.getByText(msg);
    }
}
