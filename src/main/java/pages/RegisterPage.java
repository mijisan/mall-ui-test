package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class RegisterPage extends BasePage {
    private final Locator header = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Register Account").setLevel(1));;

    public RegisterPage(Page page) {
        super(page);
    }
}
