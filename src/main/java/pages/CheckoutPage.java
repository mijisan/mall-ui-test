package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class CheckoutPage extends BasePage {
    Locator firstNameInput= page.getByLabel("First Name");
    Locator lastNameInput = page.getByLabel("Last Name");
    Locator address1Input = page.getByLabel("Address 1");
    Locator cityInput = page.getByLabel("City");
    Locator postCodeInput = page.getByLabel("Post Code");
    Locator region = page.getByLabel("Region / State");
    Locator paymentAddressBtn = page.locator("#button-payment-address");
    Locator shippingAddressBtn = page.locator("#button-shipping-address");
    Locator shippingMethodBtn = page.locator("#button-shipping-method");
    Locator paymentMethodBtn = page.locator("#button-payment-method");
    Locator agreeField = page.getByRole(AriaRole.CHECKBOX);
    Locator productRow = page.locator("table.table-bordered.table-hover tbody tr").first();
    Locator confirmOrderTable = page.getByRole(AriaRole.TABLE);
    Locator productNameCell = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("iPod Touch"));
    Locator confirmBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Order"));

    public CheckoutPage(Page page) {
        super(page);
    }

    public String getProductName() {
        return productRow.locator("td:nth-child(1) a").textContent().trim();
    }

    public String getProductCount() {
        return productRow.locator("td:nth-child(3)").textContent().trim();
    }

    public String getProductPrice() {
        return productRow.locator("td:nth-child(5)").textContent().trim();
    }

    @Step("输入名：{0}")
    public void addFirstName(String value) {
        firstNameInput.fill(value);
    }

    @Step("输入姓：{0}")
    public void addLastName(String value) {
        lastNameInput.fill(value);
    }

    @Step("输入地址1：{0}")
    public void addAddress1(String value) {
        address1Input.fill(value);
    }

    @Step("输入城市：{0}")
    public void addCity(String value) {
        cityInput.fill(value);
    }

    @Step("输入邮编码：{0}")
    public void addPostCode(String value) {
        postCodeInput.fill(value);
    }

    @Step("选择地区")
    public void selectRegion(String value) {
        region.selectOption(value);
    }

    @Step("点击确认付款地址按钮")
    public void clickBtn1() {
        paymentAddressBtn.click();
    }

    @Step("点击确认运输地址按钮")
    public void clickBtn2() {
        shippingAddressBtn.click();
    }

    @Step("点击确认运输方式按钮")
    public void clickBtn3() {
        shippingMethodBtn.click();
    }

    @Step("点击确认支付方式按钮")
    public void clickBtn4() {
        paymentMethodBtn.click();
    }

    @Step("同意条款")
    public void checkAgreeTerms() {
        agreeField.check();
    }

    @Step("点击确认订单按钮")
    public SuccessPage clickConfirmBtn() {
        confirmBtn.click();
        return new SuccessPage(page);
    }
}
