package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class SearchPage extends BasePage {
    private final Locator header = page.locator("h1");
    private final Locator notExistMeg = page.locator("#content");
    private final Locator logoLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("naveenopencart"));
    private final Locator productCards = page.locator(".product-layout");
    private final Locator add2CartBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
    private final Locator alertMessage = page.locator(".alert");

    public SearchPage(Page page) {
        super(page);
    }

    public Locator getTargetProduct(int n) {
        return productCards.nth(n - 1);
    }

    public Locator getProductLink(int n) {
        return getTargetProduct(n).locator(".caption h4 a");
    }

    public String getProductName(int n) {
        return getProductLink(n).textContent().trim();
    }

    public String getProductPrice(int n) {
        return getTargetProduct(n).locator(".price").textContent().trim().split("\n")[0];
    }

    @Step("点击LOGO")
    public HomePage clickLogo() {
        logoLink.click();
        return new HomePage(page);
    }

    @Step("点击第 {0} 个商品的加入购物车按钮")
    public void clickAdd2CartBtn(int number) {
        add2CartBtn.nth(number -1 ).click();
    }

    @Step("点击第{0}个商品的链接进入详情页")
    public ProductPage clickProductLink(int n) {
        getProductLink(n).click();
        return new ProductPage(page);
    }

}
