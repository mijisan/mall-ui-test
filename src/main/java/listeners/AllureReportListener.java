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
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("测试跳过：{}", result.getMethod().getMethodName());
    }
}
