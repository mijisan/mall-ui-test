package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import constants.AppConstants;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class HomePage extends BasePage {

    private final Locator firstProduct = page.locator(".product-layout").first();
    // 找到该商品对应的三个操作按钮（使用 FontAwesome 图标特征定位）
    private final Locator addToCartBtn = firstProduct.locator("button:has(i.fa-shopping-cart)");
    private final Locator addToWishListBtn = firstProduct.locator("button:has(i.fa-heart)");
    private final Locator compareBtn = firstProduct.locator("button:has(i.fa-exchange)");
    // 页面顶部的全局提示框和迷你购物车文本
    private final Locator alertMessage = page.locator(".alert");
    private final Locator carousel = page.locator(".slideshow.swiper-viewport"); // 顶部轮播图容器
    private final Locator nextArrow = carousel.locator(".swiper-button-next"); // 右箭头 >
    private final Locator prevArrow = carousel.locator(".swiper-button-prev"); // 左箭头 <
    private final Locator paginationDots = page.locator(".swiper-pagination").first().locator(".swiper-pagination-bullet"); // 分页小圆点
    private final Locator activeBannerImg = carousel.locator(".swiper-slide-active img"); // 当前正在显示的图片
    private final Locator activeBannerLink = carousel.locator(".swiper-slide-active a"); // 当前显示的Banner超链接

    public HomePage(Page page) {
        super(page);
    }

    @Step("点击第一个商品（macbook）的加入购物车按钮")
    public void clickAddToCartBtn() {
        addToCartBtn.click();
    }

    @Step("获取激活的图片 src")
    public String getActiveImgSrc() {
        return activeBannerImg.getAttribute("src");
    }

    // HTML 中有：
    // autoplay: 2500,
    // autoplayDisableOnInteraction: true,
    // 也就是说，这个轮播会自动播放。建议在调试时先关闭 autoplay（如果可以控制测试环境）
    @Step("点击右侧切换箭头")
    public void clickNextArrow() {
        carousel.hover();
        nextArrow.first().click();
        page.waitForTimeout(500);   // 后续可替换为更智能的等待

    }

    @Step("点击左侧切换箭")
    public void clickPrevArrow() {
        carousel.hover();
        prevArrow.first().click();
        page.waitForTimeout(500);
    }

    public int getDotsCount() {
        return paginationDots.count();
    }

    @Step("点击第{0}个小圆点")
    public void clickDot(int index) {
        paginationDots.nth(index).click();
    }

    @Step("点击banner图片并跳转到商品详情页")
    public ProductPage clickBannerLink() {
        activeBannerLink.click();
        return new ProductPage(page);
    }

    @Step("点击加入收藏夹按钮")
    public void clickAddToWishListBtn() {
        addToWishListBtn.click();
    }

    @Step("点击加入对比按钮")
    public void clickCompareBtn() {
        compareBtn.click();
    }

    @Step("导航至登录页")
    public void navigateLoginPage() {
        page.navigate(AppConstants.LOGIN_PAGE_URL);
    }

    public ProductPage clickProductLink(String productName) {
        page.getByTitle(productName).click();
        return new ProductPage(page);
    }
}
