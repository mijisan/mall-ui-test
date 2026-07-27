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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pages.AccountPage;
import pages.ForgottenPage;
import pages.LoginPage;
import pages.RegisterPage;
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
    @Story("登录")
    @Test(description = "验证用户输入正确的邮箱和密码后成功跳转至账户页")
    void login() {
        LoginPage loginPage = homePage.openLoginPage();
        loginPage.addEmail(prop.getProperty("username"));
        loginPage.addPwd(prop.getProperty("password"));
        AccountPage accountPage = loginPage.clickLoginBtn();
        AssertUtils.assertVisible(accountPage.getLogoutLink(), "登出链接");
    }

    @DataProvider
    public Object[][] getLoginData() {
        return new Object[][] {
                {"dudulu1@mail.com", "123456"},
                {"dudulu@mail.com", "12345"},
                {"", ""}
        };
    }

    @SkipLogin
    @Story("登录")
    @Test(dataProvider = "getLoginData", description = "未注册邮箱、错误密码、空值提交")
    void loginFailed(String username, String pwd) {
        LoginPage loginPage = homePage.openLoginPage();
        loginPage.addEmail(username);
        loginPage.addPwd(pwd);
        loginPage.clickLoginBtnWithWrongData();
        AssertUtils.assertPageTitle(AppConstants.LOGIN_PAGE_TITLE);
        AssertUtils.assertVisible(loginPage.getAlertMessage(), "提示框");
        AssertUtils.assertContainsText(loginPage.getAlertMessage(), "Warning: ", "提示框");
    }

    @Story("链接跳转")
    @SkipLogin
    @Test(description = "注册入口跳转")
    void testRegisterBtn() {
        LoginPage loginPage = homePage.openLoginPage();
        AssertUtils.assertVisible(loginPage.getRegisterBtn(), "注册跳转按钮");
        RegisterPage registerPage = loginPage.clickRegisterBtn();
        AssertUtils.assertPageTitle(AppConstants.REGISTER_PAGE_TITLE);
        AssertUtils.assertVisible(registerPage.getHeader(), "登录页标题");
    }

    @Story("链接跳转")
    @SkipLogin
    @Test(description = "忘记密码跳转")
    void testForgottenPasswordLink() {
        LoginPage loginPage = homePage.openLoginPage();
        AssertUtils.assertVisible(loginPage.getForgetPwdLink(), "忘记密码链接");
        ForgottenPage forgottenPage = loginPage.clickForgetPwdLink();
        AssertUtils.assertPageTitle(AppConstants.FORGETTEN_PAGE_TITLE);
        AssertUtils.assertVisible(forgottenPage.getHeader(), "忘记密码页标题");
    }

    @Story("链接跳转")
    @SkipLogin
    @Test(description = "未登录状态访问受限页面")
    void testAccessRestrictedPage() {
        LoginPage loginPage = homePage.openLoginPage();
        loginPage.clickMyAccountLink();
        AssertUtils.assertPageTitle(AppConstants.LOGIN_PAGE_TITLE);
    }

    @Story("状态安全")
    @Test(description = "已登录状态防重复登录")
    void testRegisterLink2() {
        homePage.navigateLoginPage();
        AssertUtils.assertPageTitle(AppConstants.ACCOUNT_PAGE_TITLE);
    }
}
