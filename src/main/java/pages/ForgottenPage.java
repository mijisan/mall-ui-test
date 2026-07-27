package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class ForgottenPage extends BasePage {

    private final Locator header = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Forgot Your Password?").setLevel(1));

    public ForgottenPage(Page page) {
        super(page);
    }

}
