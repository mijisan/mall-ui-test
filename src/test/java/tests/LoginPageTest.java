package tests;

import auth.SkipLogin;
import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.MouseButton;
import constants.AppConstants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pages.AccountPage;
import pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("商城ui测试")
@Feature("登录页")
public class LoginPageTest extends BaseTest {
    @SkipLogin
    @Story("导航至登录页成功")
    @Test(priority = -1, description = "验证登录页title正确")
    void goToLoginPageTest() {
        LoginPage loginPage = homePage.openLoginPage();
        String title = loginPage.getLoginPageTitle();
        Assert.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE);
        assertThat(loginPage.getForgetPwdLink()).isVisible();
    }

    @SkipLogin
    @Story("登录成功")
    @Test(description = "验证用户输入正确的邮箱和密码后成功跳转至账户页")
    void login() {
        LoginPage loginPage = homePage.openLoginPage();
        loginPage.addEmail(prop.getProperty("username"));
        loginPage.addPwd(prop.getProperty("password"));
        AccountPage accountPage = loginPage.clickLoginBtn();
        assertThat(accountPage.getLogoutLink()).isVisible();
    }

    @Ignore
    void test2(Page page) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* First Name")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* Last Name")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* E-Mail")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* Telephone")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* Password").setExact(true)).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("* Password Confirm")).click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).check();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("No")).check();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("No")).click(new Locator.ClickOptions()
                .setButton(MouseButton.RIGHT));
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("No")).check();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("No")).check();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("No")).check();
        page.getByRole(AriaRole.CHECKBOX).check();
        page.getByRole(AriaRole.CHECKBOX).click(new Locator.ClickOptions()
                .setButton(MouseButton.RIGHT));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByText("First Name must be between 1").click();
        page.getByText("Last Name must be between 1").click();
        page.getByText("E-Mail Address does not").click();
        page.getByText("Telephone must be between 3").click();
        page.getByText("Password must be between 4").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Register Account")).click();
    }
}
