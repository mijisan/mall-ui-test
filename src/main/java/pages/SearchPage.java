package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

public class SearchPage extends BasePage {
    @Getter
    private final Locator header = page.locator("h1");
    @Getter
    private final Locator notExistMeg = page.locator("#content");
    private final Locator logoLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("naveenopencart"));

    public SearchPage(Page page) {
        super(page);
    }

    @Step("点击LOGO")
    public  HomePage clickLogo() {
        logoLink.click();
        return new HomePage(page);

    }
}
