package base;

import com.microsoft.playwright.Page;

/**
 * 页面父类,统一持有 Page 对象
 */
public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }
}

