package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class ProductPage extends BasePage {

    private final Locator header = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Samsung Galaxy Tab 10.1").setLevel(1));

    public ProductPage(Page page) {
        super(page);
    }
}
