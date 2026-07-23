package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchPage extends BasePage {
    private final Locator header = page.locator("h1");

    public SearchPage(Page page) {
        super(page);
    }

    public Locator getHeader() {
        return header;
    }
}
