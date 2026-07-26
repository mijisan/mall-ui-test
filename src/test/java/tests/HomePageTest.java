package tests;

import auth.SkipLogin;
import base.BaseTest;
import constants.AppConstants;
import factory.PlaywrightFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchPage;
import utils.AssertUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("商城ui测试")
@Feature("主页")
public class HomePageTest extends BaseTest {

    private String secondImgSrc;

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
        homePage.addProduct(productName);
        SearchPage searchPage = homePage.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getHeader(), "Search - "+productName, "搜索页标题");
    }

    @SkipLogin
    @Story("全局搜索框")
    @Test( description = "搜索不存在商品")
    public void searchNotExistProductTest() {
        homePage.addProduct("小米手机");
        SearchPage searchPage = homePage.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getNotExistMeg(), "There is no product", "提示");
    }

    @SkipLogin
    @Story("全局搜索框")
    @Test( description = "直接点击搜索")
    public void searchTest() {
        SearchPage searchPage = homePage.clickSearchBtn();
        AssertUtils.assertContainsText(searchPage.getHeader(), "Search", "标题");
    }

    @Story("货币切换")
    @SkipLogin
    @Test(description = "点击货币下拉菜单，选择欧元")
    public void switchCurrencyTest() {
        homePage.clickCurrencyBtn();
        homePage.clickEuroBtn();
        AssertUtils.assertContainsText(homePage.getCartButton(), "0 item(s) - 0.00€", "迷你购物车");
    }

    @Story("LOGO")
    @SkipLogin
    @Test(description = "打开搜索页，点击左侧LOGO，正确返回首页")
    public void logoTest() {
        SearchPage searchPage = homePage.clickSearchBtn();
        searchPage.clickLogo();
        AssertUtils.assertPageTitle(AppConstants.HOME_PAGE_TITLE);
    }

    @Story("迷你购物车")
    @SkipLogin
    @Test(description = "测试迷你购物车：初始状态、展开浮层及动态更新")
    public void miniCartTest() {
        AssertUtils.assertContainsText(homePage.getCartTotalText(), "0.00", "迷你购物车文本初始状态");

        homePage.clickMiniCartBtn();
        AssertUtils.assertVisible(homePage.getCartDropdown(), "购物车浮层");
        AssertUtils.assertContainsText(homePage.getCartDropdown(), "Your shopping cart is empty", "空购物车提示");
        homePage.clickMiniCartBtn();

        homePage.clickFirstProductAddToCartBtn();
        AssertUtils.assertVisible(homePage.getSuccessAlert(), "成功提示条");
        AssertUtils.assertContainsText(homePage.getSuccessAlert(), "Success: You have added", "成功提示条");

        AssertUtils.assertContainsText(homePage.getCartTotalText(), "1 item(s) - $602.00", "迷你购物车文本");
        homePage.clickMiniCartBtn();
        AssertUtils.assertContainsText(homePage.getCartDropdown(), "MacBook", "浮层内商品明细");

    }

    @SkipLogin
    @Story("轮播图区域")
    @Test(priority = 1, description = "测试轮播图：箭头切换")
    void testHeroBannerCarousel() {
        AssertUtils.assertVisible(homePage.getCarousel(), "轮播图");
        String initialImgSrc = homePage.getActiveImgSrc();
        homePage.clickNextArrow();
        AssertUtils.assertNotHasAttr(homePage.getActiveBannerImg(), "src", initialImgSrc, "当前激活图片");
        secondImgSrc = homePage.getActiveImgSrc();
        homePage.clickPrevArrow();
        AssertUtils.assertHasAttr(homePage.getActiveBannerImg(), "src", initialImgSrc, "当前激活图片");
    }

    @Story("轮播图区域")
    @Test(priority = 2, description = "测试轮播图：底部分页圆")
    void testHeroBannerCarousel2() {
        AssertUtils.assertTrue(homePage.getDotsCount() >= 2, "轮播图至少有 2 个小圆点可以点击");
        homePage.clickDot(1);
        AssertUtils.assertHasAttr(homePage.getActiveBannerImg(), "src", secondImgSrc, "当前激活图片");
    }
}
