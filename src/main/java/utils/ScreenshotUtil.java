package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 截图工具类，负责截图保存和 Allure 附件附加
 */
@Slf4j
public class ScreenshotUtil {

    /**
     * 截取当前页面全页截图，保存到文件并返回 Base64 编码（供 ExtentReports 使用）
     */
    public static String takeScreenshot(Page page) {
        String path = System.getProperty("user.dir") + "/target/screenshot/" + System.currentTimeMillis() + ".png";
        byte[] buffer = page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(true));
        return Base64.getEncoder().encodeToString(buffer);
    }

    /**
     * 将失败截图附加到 Allure 报告
     */
    public static void saveScreenshotToAllure(Page page) {
        if (page == null) return;
        try {
            byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("失败截图", "image/png", new ByteArrayInputStream(bytes), ".png");
            log.info("失败截图添加成功");
        } catch (Exception e) {
            log.error("截图失败", e);
        }
    }
}
