package tests;

import auth.SkipLogin;
import base.BaseTest;
import constants.AppConstants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductPage;
import pages.SearchPage;
import utils.AssertUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("商城ui测试")
@Feature("主页")
public class HomePageTest extends BaseTest {

    private String second;

    @Story("成功导航至主页")
    @Test(priority = -1, description = "验证主页title正确")
    public void titleTest() {
        AssertUtils.assertPageTitle(AppConstants.HOME_PAGE_TITLE);
    }

    @DataProvider
    public Object[][] getProductData() {
        return new Object[][] {
                {"Macbook"},
                {"iMac"},
                {"Samsung"}
        };
    }

    @SkipLogin
    @Story("全局搜索框")
    @Test(dataProvider = "getProductData", description = "搜索存在商品")
    public void searchExistProductTest(String productName) {
        commonComponent.addProduct(productName);
        SearchPage searchPage = commonComponent.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getHeader(), "Search - "+productName, "搜索页标题");
    }

    @SkipLogin
    @Story("全局搜索框")
    @Test( description = "搜索不存在商品")
    public void searchNotExistProductTest() {
        commonComponent.addProduct("小米手机");
        SearchPage searchPage = commonComponent.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getNotExistMeg(), "There is no product", "提示");
    }

    @SkipLogin
    @Story("全局搜索框")
    @Test( description = "直接点击搜索")
    public void searchTest() {
        SearchPage searchPage = commonComponent.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getHeader(), "Search", "标题");
    }

    @Story("货币切换")
    @SkipLogin
    @Test(description = "点击货币下拉菜单，选择欧元")
    public void switchCurrencyTest() {
        commonComponent.clickCurrencyBtn();
        commonComponent.clickEuroBtn();
        AssertUtils.assertContainsText(commonComponent.getCartButton(), "0 item(s) - 0.00€", "迷你购物车");
    }

    @Story("LOGO")
    @SkipLogin
    @Test(description = "打开搜索页，点击左侧LOGO，正确返回首页")
    public void logoTest() {
        SearchPage searchPage = commonComponent.clickSearchBtn();
        searchPage.clickLogo();
        AssertUtils.assertPageTitle(AppConstants.HOME_PAGE_TITLE);
    }

    @Story("迷你购物车")
    @SkipLogin
    @Test(description = "测试迷你购物车：初始状态、展开浮层及动态更新")
    public void miniCartTest() {
        AssertUtils.assertContainsText(commonComponent.getCartTotalText(), "0.00", "迷你购物车文本初始状态");

        commonComponent.clickMiniCartBtn();
        AssertUtils.assertVisible(commonComponent.getCartDropdown(), "购物车浮层");
        AssertUtils.assertContainsText(commonComponent.getCartDropdown(), "Your shopping cart is empty", "空购物车提示");
        commonComponent.clickMiniCartBtn();

        homePage.clickAddToCartBtn();
        AssertUtils.assertVisible(homePage.getAlertMessage(), "成功提示条");
        AssertUtils.assertContainsText(homePage.getAlertMessage(), "Success: You have added", "成功提示条");

        AssertUtils.assertContainsText(commonComponent.getCartTotalText(), "1 item(s) - $602.00", "迷你购物车文本");
        commonComponent.clickMiniCartBtn();
        AssertUtils.assertContainsText(commonComponent.getCartDropdown(), "MacBook", "浮层内商品明细");

    }

    @SkipLogin
    @Story("轮播图区域")
    @Test(priority = -1, description = "测试轮播图：箭头切换")
    void testHeroBannerCarousel() {
        AssertUtils.assertVisible(homePage.getCarousel(), "轮播图");

        String first = homePage.getActiveImgSrc();
        homePage.clickNextArrow();
        assertThat(homePage.getActiveBannerImg()).not().hasAttribute("src", first);
        second = homePage.getActiveImgSrc();
        Assert.assertNotEquals(second, first);
        homePage.clickPrevArrow();
        Assert.assertEquals(homePage.getActiveImgSrc(), first);
    }

    @Story("轮播图区域")
    @Test(description = "测试轮播图：底部分页圆")
    void testHeroBannerCarousel2() {
        AssertUtils.assertTrue(homePage.getDotsCount() >= 2, "轮播图至少有 2 个小圆点可以点击");
        homePage.clickDot(1);
        assertThat(homePage.getActiveBannerImg()).hasAttribute("src", second);
    }

    @Story("轮播图区域")
    @Test(description = "验证 Banner 链接跳转")
    void testClickBannerNOpenProductPage() {
        ProductPage productPage = homePage.clickBannerLink();
        AssertUtils.assertVisible(productPage.getProductHeading("Samsung Galaxy Tab 10.1"), "商品详情页标题");
    }

    @Story("推荐商品区域")
    @Test(description = "首页商品交互按钮：加入收藏夹-未登录")
    @SkipLogin
    void testProductAddToWishListBtn() {
        homePage.clickAddToWishListBtn();
        AssertUtils.assertVisible(homePage.getAlertMessage(), "提示框");
        AssertUtils.assertContainsText(homePage.getAlertMessage(), "You must login", "提示框");
    }

    @Story("推荐商品区域")
    @Test(description = "首页商品交互按钮：加入对比-未登录")
    @SkipLogin
    void testProductCompareBtn() {
        homePage.clickCompareBtn();
        AssertUtils.assertVisible(homePage.getAlertMessage(), "提示框");
        AssertUtils.assertContainsText(homePage.getAlertMessage(), "Success: You have added MacBook to your product comparison!", "提示框");
    }

    @Story("推荐商品区域")
    @Test(description = "首页商品交互按钮：加入购物车-未登录")
    @SkipLogin
    void testProductAddToCartBtn() {
        homePage.clickAddToCartBtn();
        AssertUtils.assertVisible(homePage.getAlertMessage(), "提示框");
        AssertUtils.assertContainsText(homePage.getAlertMessage(), "Success: You have added MacBook to your shopping cart!", "提示框");
        AssertUtils.assertContainsText(commonComponent.getCartTotalText(), "1 item(s) - $602.00", "迷你购物车");
    }
}
