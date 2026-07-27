package tests;

import auth.SkipLogin;
import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ProductPage;
import utils.AssertUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
@Epic("商城ui测试")
@Feature("商品详情页")
public class ProductPageTest extends BaseTest {

    @Story("页面跳转")
    @SkipLogin
    @Test(priority = -1, description = "页面跳转")
    public void testOpenProductPage() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        AssertUtils.assertPageTitle(prop.getProperty("productName"));
        AssertUtils.assertVisible(productPage.getProductHeading(prop.getProperty("productName")), "标题");
    }

    @Story("商品信息")
    @SkipLogin
    @Test(description = "核心信息展示")
    public void testCoreInfoDisplay() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        AssertUtils.assertVisible(productPage.getProductHeading(prop.getProperty("productName")), "标题");
        AssertUtils.assertContainsText(productPage.getBrand(prop.getProperty("brand")), prop.getProperty("brand"), "品牌信息");
        AssertUtils.assertVisible(productPage.getPrice(), "商品价格");
    }

    @Story("商品信息")
    @SkipLogin
    @Test(description = "加入收藏夹 (未登录)")
    public void testAdd2Favorites() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickAdd2WishListBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "You must login", "提示框");
    }

    @Story("商品信息")
    @SkipLogin
    @Test(description = "加入对比列表")
    public void testAddComparison() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickCompareBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "Success: You have added MacBook to your product comparison!", "提示框");
    }

    @Story("商品信息")
    @SkipLogin
    @Test(description = "选项卡内容切换")
    public void testTabSwitching() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickDescriptionLink();
        AssertUtils.assertVisible(productPage.getDesMsg(), "商品详情信息");
        productPage.clickSpecificationLink();
        AssertUtils.assertVisible(productPage.getSpecMsg(), "商品规格信息");
        productPage.clickReviewsLink();
        AssertUtils.assertVisible(productPage.getRevMsg(), "商品评价");
    }

    @Story("购物车")
    @SkipLogin
    @Test(description = "添加有效数量商品到购物车")
    public void testAddValidQuantity() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.addQty("1");
        productPage.clickAdd2CartBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "Success: You have added MacBook to your shopping cart!", "提示框");
        AssertUtils.assertContainsText(homePage.getCartTotalText(), "1 item(s) - $602.00", "迷你购物车");
    }

    @Story("购物车")
    @SkipLogin
    @Test(description = "添加无效数量商品到购物车")
    public void testAddInvalidQuantity() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.addQty("0");
        productPage.clickAdd2CartBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(homePage.getCartTotalText(), "0 item(s) - $0.00", "迷你购物车");
    }

    @Story("商品评价")
    @SkipLogin
    @Test(priority = 1, description = "提交有效评价")
    public void testSubmitValidReview() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickReviewsLink();
        AssertUtils.assertVisible(productPage.getRevMsg(), "商品评价");
        productPage.addName("Test User");
        productPage.addReview("This is a fantastic product! The performance is outstanding and I highly recommend it.");
        productPage.chooseStar();
        productPage.clickContinueBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "Thank you for your review", "提示框");

    }

    @Story("商品评价")
    @SkipLogin
    @Test(priority = 1, description = "提交空评价表单")
    public void testSubmitInvalidReview() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickReviewsLink();
        AssertUtils.assertVisible(productPage.getRevMsg(), "商品评价");
        productPage.clickContinueBtn();
        AssertUtils.assertVisible(productPage.getAlertMeg(), "提示框");
        AssertUtils.assertContainsText(productPage.getAlertMeg(), "Warning:", "提示框");
    }

    @Story("图片画廊")
    @SkipLogin
    @Test(description = "缩略图与大图交互")
    public void testImageInteraction() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        String mini = productPage.getMainThumbnail().getAttribute("href");
        productPage.clickImg();
        AssertUtils.assertVisible(productPage.getPopupImage(), "大图");
        Assert.assertEquals(productPage.getPopupImage().getAttribute("src"), mini);

    }

    @Story("图片画廊")
    @SkipLogin
    @Test(priority = 1, description = "大图弹窗切换")
    public void testSwitchImages() {
        ProductPage productPage = homePage.clickProductLink(prop.getProperty("productName"));
        productPage.clickImg();
        AssertUtils.assertVisible(productPage.getPopupImage(), "大图");
        String firstImageSrc = productPage.getPopupImage().getAttribute("src");
        AssertUtils.assertContainsText(productPage.getImageCounter(), "1 of", "计数器");

        productPage.clickRightArrow();
        AssertUtils.assertContainsText(productPage.getImageCounter(), "2 of", "计数器");
        String secondImageSrc = productPage.getPopupImage().getAttribute("src");
        Assert.assertNotEquals(firstImageSrc, secondImageSrc, "图片 src 应该发生变化，证明图片已切换");

        productPage.clickLeftArrow();
        AssertUtils.assertContainsText(productPage.getImageCounter(), "1 of", "计数器");
        assertThat(productPage.getPopupImage()).hasAttribute("src", firstImageSrc);
    }


}
