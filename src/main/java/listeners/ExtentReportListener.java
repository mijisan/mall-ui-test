package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import static factory.PlaywrightFactory.takeScreenshot;

public class ExtentReportListener implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(ExtentReportListener.class);

    private static final String OUTPUT_FOLDER = "./target/build/";
    private static final String FILE_NAME = "TestExecutionReport.html";

    // 使用 ThreadLocal 确保多线程并发下每个测试拥有独立的 ExtentTest 实例
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    // 使用双重检查锁或线程安全的初始化机制，保证全局单例
    private static final ExtentReports extentReports = initExtentReports();

    private static synchronized ExtentReports initExtentReports() {
        Path path = Paths.get(OUTPUT_FOLDER);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建 Extent Report 目录: " + OUTPUT_FOLDER, e);
        }

        ExtentReports reports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);

        // 现代 Spark 报告配置
        reporter.config().setReportName("Open Cart 自动化测试结果");
        reporter.config().setDocumentTitle("Test Execution Automation Report");
        reports.attachReporter(reporter);

        // 支持通过系统属性（CI/CD）动态注入环境变量，回退到默认值
        reports.setSystemInfo("System", System.getProperty("os.name"));
        reports.setSystemInfo("Author", System.getProperty("user.name", "Automation Team"));
        reports.setSystemInfo("Environment", System.getProperty("env", "QA"));
        reports.setSystemInfo("Build#", System.getProperty("build.number", "1.1"));

        return reports;
    }

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        log.info("测试套件开始: {}", context.getName());
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        log.info("测试套件结束: {}", context.getName());
        extentReports.flush();
        extentTestThreadLocal.remove();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();

        log.info("测试开始：{} -> {}", className, methodName);

        ExtentTest extentTest = extentReports.createTest(methodName, result.getMethod().getDescription());
        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        extentTest.assignCategory(className);

        extentTestThreadLocal.set(extentTest);
        getTest().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("测试通过：{}", result.getMethod().getMethodName());
        logResultWithScreenshot(result, "测试通过");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("测试失败：{}", result.getMethod().getMethodName());
        logResultWithScreenshot(result, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("测试跳过：{}", result.getMethod().getMethodName());
        logResultWithScreenshot(result, result.getThrowable());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("测试失败但在成功率范围内：{}", result.getMethod().getMethodName());
    }

    /**
     * 统一处理日志与截图附加逻辑，消除代码冗余
     */
    private void logResultWithScreenshot(ITestResult result, Object details) {
        ExtentTest test = getTest();
        if (test == null) return;

        try {
            String base64Screenshot = takeScreenshot();
            var media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, result.getMethod().getMethodName()).build();

            if (details instanceof Throwable throwable) {
                test.fail(throwable, media);
            } else {
                test.pass(String.valueOf(details), media);
            }
        } catch (Exception e) {
            log.error("未能附加测试截图：{}", result.getMethod().getMethodName(), e);
        }

        test.getModel().setEndTime(getTime(result.getEndMillis()));
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
