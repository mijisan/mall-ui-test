package base;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import pages.HomePage;

import java.util.Properties;

public class BaseTest {
    protected PlaywrightFactory pf;
    protected Properties prop;

    protected HomePage homePage;

    @BeforeTest
    public void setUp() {
        pf = new PlaywrightFactory();
        prop = pf.initProp();
//        System.out.println("读取到的内容："+prop.toString());
        pf.initBrowser(prop);
    }

    @BeforeMethod
    public void setUpMethod() {
        // 每个用例开始前，创建一个全新的 Context/Page 并导航到目标 URL，实现极速隔离
        Page page = pf.createContextAndPage(prop.getProperty("url"));
        // 理想情况应该在4s以内
        page.setDefaultTimeout(5000);
        homePage = new HomePage(page);
    }

    @AfterMethod
    public void tearDownMethod() {
        // 每个用例结束后，销毁当前的 Page 和 Context
        pf.closeContextAndPage();
    }

    @AfterTest
    public void tearDown() {
        pf.quitBrowser();
    }
}
