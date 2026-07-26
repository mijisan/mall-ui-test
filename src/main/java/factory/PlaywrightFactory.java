package factory;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

public class PlaywrightFactory {

    private final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);
    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>(); // 全局复用（如果单线程或配合锁）
    private static final ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Page getPage() {
        return tlPage.get();
    }

    public static BrowserContext getContext() {
        return tlBrowserContext.get();
    }

    public void initBrowser(Properties prop) {

        String browserName = prop.getProperty("browser").trim().toLowerCase();
        // 优先使用系统属性（CI 环境），其次使用配置文件
        boolean headlessMode = Boolean.parseBoolean(System.getProperty("headless", prop.getProperty("headless")));
        Playwright playwright = Playwright.create();
        tlPlaywright.set(playwright);

        Browser browser = switch (browserName) {
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
        tlBrowser.set(browser);
        log.info("浏览器{}启动成功, headless:{}", browserName, headlessMode);
    }

    /**
     * 每个测试方法执行前：创建一个完全隔离的 BrowserContext 和 Page
     */
    public Page createContextAndPage(String url, boolean useLoginState) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        // 如果需要加载登录态，则设置 storageState 路径
        if (useLoginState) {
            contextOptions.setStorageStatePath(Paths.get("auth.json"));
            log.info("加载已保存的登录状态 auth.json");
        } else {
            log.info("不加载登录状态，创建干净的上下文");
        }
        // 使用配置好的 Options 创建 Context
        BrowserContext context = tlBrowser.get().newContext(contextOptions);
        // 在创建 Context 后立即开启 Trace 录制
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        tlBrowserContext.set(context);
        Page page = context.newPage();
        // 理想情况应该在4s以内
        page.setDefaultTimeout(5000);
        tlPage.set(page);

        if (url != null && !url.isBlank()) {
            page.navigate(url.trim());
        }
        return page;
    }


    public void closeContextAndPage() {
        if (tlPage.get() != null) {
            tlPage.get().close();
            tlPage.remove();
        }
        if (tlBrowserContext.get() != null) {
            tlBrowserContext.get().close();
            tlBrowserContext.remove();
        }
    }

    public void quitBrowser() {
        if (tlBrowser.get() != null) {
            tlBrowser.get().close();
            tlBrowser.remove();
        }
        if (tlPlaywright.get() != null) {
            tlPlaywright.get().close();
            tlPlaywright.remove();
        }
    }

    public Properties initProp() {
        Properties prop = new Properties();
        try {
            // 从classpath加载配置文件
            InputStream inputStream = PlaywrightFactory.class.getClassLoader()
                    .getResourceAsStream("config/config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
                log.info("配置文件加载成功: config.properties");
                inputStream.close();
            } else {
                log.warn("未找到配置文件 config.properties，将使用默认配置");
            }
        } catch (Exception e) {
            log.error("加载配置文件失败", e);
        }
        return prop;
    }

    public static String takeScreenshot() {
        String path = System.getProperty("user.dir") + "/target/screenshot/" + System.currentTimeMillis() + ".png";
        byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(true));
        return Base64.getEncoder().encodeToString(buffer);
    }
}
