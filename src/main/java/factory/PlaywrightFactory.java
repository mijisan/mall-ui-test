package factory;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
//    Properties properties;

    public void initBrowser(Properties prop) {

        String browserName = prop.getProperty("browser").trim().toLowerCase();
        boolean headlessMode = Boolean.parseBoolean(prop.getProperty("headless"));
        playwright = Playwright.create();

        browser = switch (browserName) {
            case "chromium" -> playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headlessMode));

            case "firefox" -> playwright.firefox()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headlessMode));

            case "safari" -> playwright.webkit()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headlessMode));

            case "chrome" -> playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("chrome")
                            .setHeadless(headlessMode));

            default -> throw new IllegalArgumentException("无效浏览器名称：" + browserName);
        };
    }

    /**
     * 每个测试方法执行前：创建一个完全隔离的 BrowserContext 和 Page
     */
    public Page createContextAndPage(String url) {
        context = browser.newContext();
        page = context.newPage();

        if (url != null && !url.isBlank()) {
            page.navigate(url.trim());
        }
        return page;
    }


    public void closeContextAndPage() {
        if (context != null) context.close();
    }

    public void quitBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    public Properties initProp() {
        Properties prop = new Properties();
        try {
            // 从classpath加载配置文件
            InputStream inputStream = PlaywrightFactory.class.getClassLoader()
                    .getResourceAsStream("config/config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
                System.out.println("配置文件加载成功: config.properties");
                log.info("配置文件加载成功: config.properties");
                inputStream.close();
            } else {
                System.out.println("未找到配置文件 config.properties，将使用默认配置");
                log.warn("未找到配置文件 config.properties，将使用默认配置");
            }
        } catch (Exception e) {
            log.error("加载配置文件失败", e);
        }
        return prop;
    }
}
