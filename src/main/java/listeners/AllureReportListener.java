package listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class AllureReportListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        log.info("Test开始: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test结束: {}", context.getName());
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
        int retryCount = result.getMethod().getCurrentInvocationCount() - 1;
        if (retryCount > 0) {
            log.warn("测试失败（第{}次重试）：{}", retryCount, result.getName());
        } else {
            log.error("测试失败：{}", result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("测试跳过：{}", result.getMethod().getMethodName());
    }
}
