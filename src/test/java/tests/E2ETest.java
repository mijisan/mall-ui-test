package tests;

import auth.SkipLogin;
import base.BaseTest;
import constants.AppConstants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.AssertUtils;

@Epic("商城ui测试")
@Feature("购物流程")
public class E2ETest extends BaseTest {

    @Test(description = "核心业务流程测试")
    @SkipLogin
    void e2eTest() {
        // ======== 准备数据 ==============
        String firstName = prop.getProperty("firstname");
        String lastName = prop.getProperty("lastname");
        String phone = prop.getProperty("telephone");
        String email = System.currentTimeMillis() + prop.getProperty("username");
        String pwd = prop.getProperty("password");
        String address1 = prop.getProperty("address");
        String city = prop.getProperty("city");
        String postCode = prop.getProperty("postcode");

        // ========== 注册登录 ==============
        RegisterPage registerPage = commonComponent.openRegisterPage();
        registerPage.addFirstName(firstName);
        registerPage.addLastName(lastName);
        registerPage.addEmail(email);
        registerPage.addPhone(phone);
        registerPage.addPwd(pwd);
        registerPage.addPwd2(pwd);
        registerPage.checkAgreePolicy();
        SuccessPage successPage = registerPage.clickBtnNOpenSuccessPage();
        AssertUtils.assertPageTitle(AppConstants.REGISTER_SUCCESS_PAGE_TITLE);
        AssertUtils.assertVisible(successPage.getHeading(), "成功注册页标题");

        // ============ 搜索商品 ===============
        commonComponent.addProduct("ipod");
        SearchPage searchPage = commonComponent.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getHeader(), "Search - ipod", "搜索页标题");

        // =============== 浏览与选择商品 ============
        int n = 4; // 指定要提取第几个商品 (例如第4个：iPod Touch)
        AssertUtils.assertTrue(searchPage.getProductCards().count() >= n, "页面上的商品数量不应少于 " + n + " 个");
        String productName = searchPage.getProductName(n);
        String productPrice = searchPage.getProductPrice(n);
        ProductPage productPage = searchPage.clickProductLink(n);
        AssertUtils.assertPageTitle(productName);
        AssertUtils.assertVisible(productPage.getHeader(), "商品页标题");
        AssertUtils.assertContainsText(productPage.getHeader(), productName, "商品页标题");
        AssertUtils.assertContainsText(productPage.getPrice(), productPrice, "商品价格");

        // ============ 加入购物车 ==================
        String count = "1";
        productPage.addQty(count);
        productPage.clickAdd2CartBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "Success: You have added " + productName + " to your shopping cart!", "提示框");
        commonComponent.clickMiniCartBtn();
        AssertUtils.assertContainsText(commonComponent.getCartDropdown(), productName, "浮层内商品名");
        AssertUtils.assertContainsText(commonComponent.getCartDropdown(), productPrice, "浮层内商品价格");
        CheckoutPage checkoutPage = commonComponent.clickCheckoutLink();
        AssertUtils.assertPageTitle(AppConstants.CHECKOUT_PAGE_TITLE);

        // ============= 商品结算 ============
        // step 2
        AssertUtils.assertVisible(checkoutPage.getPaymentAddressBtn(), "确认付款地址按钮");
        checkoutPage.addFirstName(firstName);
        checkoutPage.addLastName(lastName);
        checkoutPage.addAddress1(address1);
        checkoutPage.addCity(city);
        checkoutPage.addPostCode(postCode);
        checkoutPage.selectRegion("3513");
        checkoutPage.clickBtn1();
        // step 3
        AssertUtils.assertVisible(checkoutPage.getShippingAddressBtn(), "确认运输地址按钮");
        checkoutPage.clickBtn2();
        // step 4
        AssertUtils.assertVisible(checkoutPage.getShippingMethodBtn(), "确认运输方式按钮");
        checkoutPage.clickBtn3();
        // step 5
        AssertUtils.assertVisible(checkoutPage.getPaymentMethodBtn(), "确认支付方式按钮");
        checkoutPage.checkAgreeTerms();
        checkoutPage.clickBtn4();
        // step 6
        AssertUtils.assertVisible(checkoutPage.getConfirmBtn(), "确认订单按钮");
        Assert.assertEquals(checkoutPage.getProductName(), productName, "商品名不匹配");
        Assert.assertEquals(checkoutPage.getProductCount(), count, "商品数量不匹配");
        Assert.assertEquals(checkoutPage.getProductPrice(), productPrice, "商品总价不匹配");

        //========= 交易完成 ==============
        SuccessPage successPage1 = checkoutPage.clickConfirmBtn();
        AssertUtils.assertPageTitle(AppConstants.ORDER_SUCCESS_PAGE_TITLE);
        AssertUtils.assertVisible(successPage1.getHeading(), "成功订购页标题");
        AssertUtils.assertContainsText(successPage1.getHeading(), "Your order has been placed!", "成功订购页标题");
    }

}
