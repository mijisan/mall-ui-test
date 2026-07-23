package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePageTest extends BaseTest {

    @Test(priority = -1)
    public void titleTest() {
        String title = homePage.getHomePageTitle();
        Assert.assertEquals(title, AppConstants.HOME_PAGE_TITLE);
    }

    @DataProvider
    public Object[][] getProductData() {
        return new Object[][] {
                {"Macbook"},
                {"iMac"},
                {"Samsung"}
        };
    }
    @Test(dataProvider = "getProductData")
    public void searchTest(String productName) {
        homePage.addProduct(productName);
        SearchPage searchPage = homePage.clickSearchBtn();

        assertThat(searchPage.getHeader()).containsText("Search - "+productName);

//        page.navigate(prop.getProperty("url").trim());
    }

}
