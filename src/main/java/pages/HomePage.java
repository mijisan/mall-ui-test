package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class HomePage extends BasePage {
    private final Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
    private final Locator searchBtn = page.locator("#search").getByRole(AriaRole.BUTTON);
    private final Locator myAccountLink = page.locator("span").getByText("My Account");
    private final Locator loginLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login"));

    public HomePage(Page page) {
        super(page);
    }

    @Step("获取主页title")
    public String getHomePageTitle() {
        return page.title();
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

}
