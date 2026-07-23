package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AccountPage extends BasePage {

    private final Locator logoutLink = page.locator("#column-right").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Logout"));

    public AccountPage(Page page) {
        super(page);
    }

    public Locator getLogoutLink() {
        return logoutLink;
    }
}
