package listeners;

import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.TimeoutError;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.IOException;

/**
 * 测试重试分析器
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        Throwable throwable = result.getThrowable();

        // 没有异常，不重试
        if (throwable == null) {
            return false;
        }

        // 1.AssertionError 是业务断言失败，不重试
        if (throwable instanceof AssertionError) {
            log.warn("【断言失败】{}，不重试", result.getName());
            return false;
        }

        // 2. Playwright Timeout
        if (throwable instanceof TimeoutError) {
            String msg = throwable.getMessage();
            // Locator 超时一般属于脚本问题
            if (msg != null && msg.contains("waiting for locator")) {
                log.warn("【Locator 超时】{}，不重试", result.getName());
                return false;
            }
            return retry(result, throwable);
        }

        // 3. 浏览器连接异常
        if (throwable instanceof PlaywrightException) {
            String msg = throwable.getMessage();
            if (msg != null &&
                    (msg.contains("Target closed")
                            || msg.contains("Connection closed")
                            || msg.contains("Browser has been closed"))) {
                return retry(result, throwable);
            }
            return false;
        }

        // 4. 网络异常
        if (throwable instanceof IOException) {
            return retry(result, throwable);
        }

        // 其他异常默认不重试
        return false;
    }

    private boolean retry(ITestResult result, Throwable throwable) {

        if (retryCount >= MAX_RETRY_COUNT) {
            log.warn("【重试次数已耗尽】{}，已重试{}次", result.getName(), MAX_RETRY_COUNT);
            return false;
        }

        retryCount++;

        log.warn("【重试 {} / {}】{}，异常：{}", retryCount, MAX_RETRY_COUNT,
                result.getName(), throwable.getClass().getSimpleName());

        return true;
    }

}
