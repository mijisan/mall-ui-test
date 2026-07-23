package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePageTest extends BaseTest {

    @Test
    public void titleTest() {
        String title = homePage.getHomePageTitle();
        Assert.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE);
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
        homePage.clickSearchBtn();

        assertThat(searchPage.getHeader()).containsText("Search - "+productName);
    }

}
