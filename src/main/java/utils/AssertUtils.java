package utils;

import com.microsoft.playwright.Locator;
import factory.PlaywrightFactory;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AssertUtils {
    /**
     * 验证当前页面的 Title
     */
    @Step("断言: 页面 Title 应为 [{expectedTitle}]")
    public static void assertPageTitle(String expectedTitle) {
        assertThat(PlaywrightFactory.getPage()).hasTitle(expectedTitle);
    }

    /**
     * 验证当前页面的 URL 包含特定文本
     */
    @Step("断言: 页面 URL 应包含 [{expectedUrlFragment}]")
    public static void assertUrlContains(String expectedUrlFragment) {
        // Playwright 的断言支持正则表达式
        assertThat(PlaywrightFactory.getPage())
                .hasURL(Pattern.compile(".*" + expectedUrlFragment + ".*"));
    }

    /**
     * 验证某个元素是否可见
     * @param locator Playwright的Locator对象
     * @param elementName 元素名称（仅用于Allure报告展示）
     */
    @Step("断言: {elementName} 应该是可见的")
    public static void assertVisible(Locator locator, String elementName) {
        assertThat(locator).isVisible();
    }

    /**
     * 验证元素的文本内容
     */
    @Step("断言: {elementName} 的文本应包含 [{expectedText}]")
    public static void assertContainsText(Locator locator, String expectedText, String elementName) {
        assertThat(locator).containsText(expectedText);
    }

    @Step("断言：{1}")
    public static void assertTrue(boolean isTrue, String msg) {
        Assert.assertTrue(isTrue, msg);
    }

}
