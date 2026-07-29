package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class SuccessPage extends BasePage {
    Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(1));
    Locator btn = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continue"));

    public SuccessPage(Page page) {
        super(page);
    }

    @Step("点击继续按钮")
    public LoginPage clickBtn() {
        btn.click();
        return new LoginPage(page);
    }

}
