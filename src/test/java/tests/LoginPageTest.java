package tests;

import auth.SkipLogin;
import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.MouseButton;
import constants.AppConstants;
import factory.PlaywrightFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pages.AccountPage;
import pages.LoginPage;
import utils.AssertUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("商城ui测试")
@Feature("登录页")
public class LoginPageTest extends BaseTest {
    @SkipLogin
    @Story("导航至登录页成功")
    @Test(priority = -1, description = "验证登录页title正确")
    void goToLoginPageTest() {
        LoginPage loginPage = homePage.openLoginPage();
        AssertUtils.assertPageTitle(AppConstants.LOGIN_PAGE_TITLE);
        AssertUtils.assertVisible(loginPage.getForgetPwdLink(), "忘记密码链接");
    }

    @SkipLogin
    @Story("登录成功")
    @Test(description = "验证用户输入正确的邮箱和密码后成功跳转至账户页")
    void login() {
        LoginPage loginPage = homePage.openLoginPage();
        loginPage.addEmail(prop.getProperty("username"));
        loginPage.addPwd(prop.getProperty("password"));
        AccountPage accountPage = loginPage.clickLoginBtn();
        AssertUtils.assertVisible(accountPage.getLogoutLink(), "登出链接");
    }

    @Ignore
    void test2(Page page) {
        page.navigate("https://naveenautomationlabs.com/opencart/");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("$ Currency  ")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("€ Euro")).click();
        assertThat(page.locator("#cart-total")).containsText("0 item(s) - 0.00€");
        assertThat(page.locator("#content")).containsText("472.33€");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("€ Currency  ")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("$ US Dollar")).click();
        assertThat(page.locator("#cart-total")).containsText("0 item(s) - $0.00");
        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("123456789")).getByRole(AriaRole.LINK).click();
        page.getByText("123456789", new Page.GetByTextOptions().setExact(true)).click();
        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("123456789")).click();
        assertThat(page.locator("h1")).containsText("Contact Us");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("naveenopencart")).click();
    }
}
