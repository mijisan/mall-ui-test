package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class HomePage extends BasePage {
    private final Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
    private final Locator searchBtn = page.locator("#search").getByRole(AriaRole.BUTTON);
    private final Locator myAccountLink = page.locator("span").getByText("My Account");
    private final Locator loginLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login"));
    private final Locator currencyBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Currency"));
    private final Locator euroBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Euro"));

    private final Locator cartButton = page.locator("#cart button"); // 迷你购物车黑色按钮
    private final Locator cartTotalText = page.locator("#cart-total"); // 购物车按钮上的文字
    private final Locator cartDropdown = page.locator("#cart .dropdown-menu"); // 购物车下拉浮层
    // 定位第一个特色商品 (MacBook) 的“加入购物车”按钮
    private final Locator firstProductAddToCartBtn = page.locator(".product-layout").first().locator("button:has(i.fa-shopping-cart)");
    private final Locator successAlert = page.locator(".alert-success"); // 添加成功后的绿色提示条

    private final Locator carousel = page.locator(".slideshow.swiper-viewport"); // 顶部轮播图容器
    private final Locator nextArrow = carousel.locator(".swiper-button-next"); // 右箭头 >
    private final Locator prevArrow = carousel.locator(".swiper-button-prev"); // 左箭头 <
    private final Locator paginationDots = page.locator(".swiper-pagination").first().locator(".swiper-pagination-bullet"); // 分页小圆点
//    .swiper-pagination > span:nth-child(1)
    private final Locator activeBannerImg = carousel.locator(".swiper-slide-active img"); // 当前正在显示的图片
    private final Locator activeBannerLink = carousel.locator(".swiper-slide-active a"); // 当前显示的Banner超链接

    public HomePage(Page page) {
        super(page);
    }

    @Step("输入商品名：{0}")
    public void addProduct(String productName) {
        searchInput.fill(productName);
    }

    @Step("点击搜索按钮并跳转到搜索页面")
    public SearchPage clickSearchBtn() {
        searchBtn.click();
        return new SearchPage(page);
    }

    @Step("打开登录页面")
    public LoginPage openLoginPage() {
        myAccountLink.click();
        loginLink.click();
        return new LoginPage(page);
    }

    @Step("点击货币切换按钮")
    public void clickCurrencyBtn() {
        currencyBtn.click();
    }

    @Step("点击切换欧元")
    public void clickEuroBtn() {
        euroBtn.click();
    }

    @Step("点击迷你购物车按钮")
    public void clickMiniCartBtn() {
        cartButton.click();
    }

    @Step("点击第一个商品（macbook）的加入购物车按钮")
    public void clickFirstProductAddToCartBtn() {
        firstProductAddToCartBtn.click();
    }

    @Step("获取激活的图片 src")
    public String getActiveImgSrc() {
        return activeBannerImg.getAttribute("src");
    }

    @Step("点击右侧切换箭")
    public void clickNextArrow() {
        nextArrow.first().click();
    }

    @Step("点击左侧切换箭")
    public void clickPrevArrow() {
        prevArrow.first().click();
    }

    public int getDotsCount() {
        return paginationDots.count();
    }

    @Step("点击第{0}个小圆点")
    public void clickDot(int index) {
        paginationDots.nth(index).click();
    }

}
