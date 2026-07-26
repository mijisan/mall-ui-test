package listeners;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureReportListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(AllureReportListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("测试套件开始: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("测试套件结束: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();

        log.info("测试开始：{} -> {}", className, methodName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("测试通过：{}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("测试失败：{}", result.getName());
        Page page = (Page) result.getAttribute("page");
        BrowserContext context = (BrowserContext) result.getAttribute("context");
        // 1. 失败时直接从内存获取截图并上传至 Allure（不落盘）
        saveScreenshot(page);

        // 2. 失败时保存 Playwright Trace 文件用于溯源排查
        if (context != null) {
            try {
                Path traceDir = Paths.get("target/playwright-traces");
                Files.createDirectories(traceDir);
                Path tracePath = traceDir.resolve(result.getName() + System.currentTimeMillis() + "-trace.zip");

                context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

                Allure.addAttachment("Playwright Trace 追踪文件", "application/zip", Files.newInputStream(tracePath), ".zip");
            } catch (Exception e) {
                log.error("未能保存追踪文件：{}", e.getMessage());
            }
        }
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("测试跳过：{}", result.getMethod().getMethodName());
    }

    /**
     * Allure 附件：截图（不保存在本地硬盘）
     */
    public void saveScreenshot(Page page){
        if (page == null) return;
        byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("失败截图", "image/png", new ByteArrayInputStream(bytes), ".png");
    }
}
