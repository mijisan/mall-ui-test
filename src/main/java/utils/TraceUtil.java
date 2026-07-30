package utils;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Trace 工具类，负责 Playwright Trace 的保存和 Allure 附件附加
 */
@Slf4j
public class TraceUtil {

    /**
     * 保存 Trace 文件并附加到 Allure 报告
     */
    public static void saveTraceToAllure(BrowserContext context, String testName) {
        if (context != null) {
            try {
                Path traceDir = Paths.get("target/playwright-traces");
                Files.createDirectories(traceDir);
                Path tracePath = traceDir.resolve(testName + System.currentTimeMillis() + "-trace.zip");
                context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
                try (InputStream is = Files.newInputStream(tracePath)) {
                    Allure.addAttachment("Playwright Trace 追踪文件", "application/zip", is, ".zip");
                    log.info("Trace附件添加成功");
                }
            } catch (Exception e) {
                log.error("未能保存追踪文件：{}", e.getMessage());
            }
        }
    }
}
