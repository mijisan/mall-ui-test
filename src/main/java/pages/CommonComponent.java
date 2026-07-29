package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class CommonComponent extends BasePage {

    private final Locator currencyBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Currency"));
    private final Locator euroBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Euro"));
    private final Locator myAccountLink = page.locator("span").getByText("My Account");
    private final Locator loginLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login"));
    private final Locator registerLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Register"));;

    private final Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
    private final Locator searchBtn = page.locator("#search").getByRole(AriaRole.BUTTON);

    private final Locator cartButton = page.locator("#cart > button"); // 迷你购物车黑色按钮
    private final Locator cartTotalText = page.locator("#cart-total"); // 购物车按钮上的文字
    private final Locator cartDropdown = page.locator("#cart .dropdown-menu"); // 购物车下拉浮层
    private final Locator checkoutLink = page.locator("strong:has-text('Checkout')");

    public CommonComponent(Page page) {
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

    @Step("打开注册页面")
    public RegisterPage openRegisterPage() {
        myAccountLink.click();
        registerLink.click();
        return new RegisterPage(page);
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

    @Step("点击结账链接")
    public CheckoutPage clickCheckoutLink() {
        checkoutLink.click();
        return new CheckoutPage(page);
    }
}
