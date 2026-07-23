package base;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;
import pages.SearchPage;

import java.util.Properties;

public class BaseTest {
    protected PlaywrightFactory pf;
    protected Properties prop;

    protected HomePage homePage;
    protected SearchPage searchPage;

    @BeforeMethod
    public void setUp() {
        pf = new PlaywrightFactory();
        prop = pf.initProp();
//        System.out.println("读取到的内容："+prop.toString());
        Page page = pf.initBrowser(prop);
        // 理想情况应该在4s以内
        page.setDefaultTimeout(25000);

        homePage = new HomePage(page);
        searchPage = new SearchPage(page);
    }

    @AfterMethod
    public void tearDown() {
        pf.quitBrowser();
    }
}
