package tests;

import auth.SkipLogin;
import base.BaseTest;
import constants.AppConstants;
import factory.PlaywrightFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegisterPage;
import pages.SuccessPage;
import utils.AssertUtils;

@Epic("商城ui测试")
@Feature("注册页")
public class RegisterPageTest extends BaseTest {

    @SkipLogin
    @Story("导航至注册页")
    @Test(priority = -1, description = "验证注册页title正确")
    void goToLoginPageTest() {
        RegisterPage registerPage = commonComponent.openRegisterPage();
        AssertUtils.assertPageTitle(AppConstants.REGISTER_PAGE_TITLE);
        AssertUtils.assertVisible(registerPage.getHeader(), "注册页标题");
    }

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {

        String firstName = prop.getProperty("firstname");
        String lastName = prop.getProperty("lastname");
        String phone = prop.getProperty("telephone");
        String relEmail = prop.getProperty("username");
        String email = System.currentTimeMillis() + prop.getProperty("username");
        String pwd = prop.getProperty("password");

        return new Object[][] {
                // 场景 1: First Name 为空
                {"", lastName, email, phone, pwd, pwd, true, "First Name must be between 1 and 32 characters!", "必填项空值提交(First Name 为空)"},
                // 场景 2: 密码与确认密码不一致
                {firstName, lastName, email, phone, pwd, "Mismatch456", true, "Password confirmation does not match password!", "密码与确认密码不一致"},
                // 场景 3: 未勾选 Privacy Policy
                {firstName, lastName, email, phone, pwd, pwd, false, "Warning: You must agree to the Privacy Policy!", "未勾选 Privacy Policy"},
                // 场景 4: 邮箱已被注册
                {firstName, lastName, relEmail, phone, pwd, pwd, true, "Warning: E-Mail Address is already registered!", "邮箱已被注册"},
                // 场景 5: 密码长度校验
                {firstName, lastName, email, phone, "123", "123", true, "Password must be between 4 and 20 characters!", "密码长度校验"}

        };
    }

    @Story("注册-逆向场景")
    @SkipLogin
    @Test(dataProvider = "registerData")
    public void testRegisterFormValidation(
            String firstName, String lastName, String email, String phone, String password,
            String confirmPassword, boolean agreePolicy, String errorMsg, String caseName) {

        Allure.getLifecycle().updateTestCase(result -> {
            result.setName(caseName);
        });

        RegisterPage registerPage = commonComponent.openRegisterPage();

        // 1. 填充表单数据
        registerPage.addFirstName(firstName);
        registerPage.addLastName(lastName);
        registerPage.addEmail(email);
        registerPage.addPhone(phone);
        registerPage.addPwd(password);
        registerPage.addPwd2(confirmPassword);

        // 2. 根据参数决定是否勾选隐私政策
        if (agreePolicy) {
            registerPage.getAgreePolicy().check();
        } else {
            registerPage.getAgreePolicy().uncheck();
        }

        // 3. 点击继续按钮提交表单
        registerPage.clickBtn();

        // 4. 断言：验证是否出现了预期的错误提示信息
        AssertUtils.assertVisible(registerPage.alertMsg(errorMsg), "错误提示");
        AssertUtils.assertContainsText(registerPage.alertMsg(errorMsg), errorMsg, "错误提示");
    }

    @Story("注册-正向场景")
    @SkipLogin
    @Test(description = "用户使用正确信息注册")
    public void testRegister() {

        RegisterPage registerPage = commonComponent.openRegisterPage();

        registerPage.addFirstName(prop.getProperty("firstname"));
        registerPage.addLastName(prop.getProperty("lastname"));
        registerPage.addEmail(System.currentTimeMillis() + prop.getProperty("username"));
        registerPage.addPhone(prop.getProperty("telephone"));
        registerPage.addPwd(prop.getProperty("password"));
        registerPage.addPwd2(prop.getProperty("password"));
        registerPage.checkAgreePolicy();
        SuccessPage successPage = registerPage.clickBtnNOpenSuccessPage();

        AssertUtils.assertPageTitle(AppConstants.REGISTER_SUCCESS_PAGE_TITLE);
        AssertUtils.assertVisible(successPage.getHeading(), "成功注册页标题");
        AssertUtils.assertContainsText(successPage.getHeading(), "Your Account Has Been Created!", "成功注册页标题");
//        PlaywrightFactory.getPage().pause();
    }
}
