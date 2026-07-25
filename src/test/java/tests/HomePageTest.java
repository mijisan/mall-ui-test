package tests;

import auth.SkipLogin;
import base.BaseTest;
import constants.AppConstants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("商城ui测试")
@Feature("主页")
public class HomePageTest extends BaseTest {

    @Story("成功导航至主页")
    @Test(priority = -1, description = "验证主页title正确")
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

    @SkipLogin
    @Story("搜索商品成功")
    @Test(dataProvider = "getProductData", description = "搜索商品")
    public void searchTest(String productName) {
        homePage.addProduct(productName);
        SearchPage searchPage = homePage.clickSearchBtn();

        assertThat(searchPage.getHeader()).containsText("Search - "+productName);
    }

}
