package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage extends BasePage {
    private final Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
    private final Locator searchBtn = page.locator("#search").getByRole(AriaRole.BUTTON);

    public HomePage(Page page) {
        super(page);
    }

    public String getHomePageTitle() {
        return page.title();
    }

    public void addProduct(String productName) {
        searchInput.fill(productName);
    }

    public void clickSearchBtn() {
        searchBtn.click();
    }
}
