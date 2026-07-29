package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;

@Getter
public class ProductPage extends BasePage {

    Locator header = page.locator("h1");
    Locator price = page.locator("h2:has-text('$')");
    Locator qtyBox = page.getByLabel("Qty");
    Locator add2CartBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
    Locator add2WishListBtn = page.locator("button[data-original-title='Add to Wish List']");
    Locator compareBtn = page.locator(".fa.fa-exchange");
    Locator descriptionLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Description"));
    Locator specificationLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Specification"));
    Locator reviewsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reviews ("));
    Locator desMsg = page.locator("b:has-text('Intel Core 2 Duo processor')");
    Locator specMsg = page.locator("strong:has-text('Memory')");
    Locator revMsg = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Write a review").setLevel(2));
    Locator nameBox = page.getByLabel("Your Name");
    Locator reviewBox = page.getByLabel("Your Review");
    Locator highestRating = page.locator("input[name='rating'][value='5']");
    Locator continueBtn = page.locator("#button-review");
    Locator alertMeg = page.locator(".alert");
    Locator mainThumbnail = page.locator("ul.thumbnails li:first-child a.thumbnail");
    Locator popupImage = page.locator("img.mfp-img");
    Locator rightArrow = page.locator("button.mfp-arrow-right");
    Locator leftArrow = page.locator("button.mfp-arrow-left");
    Locator imageCounter = page.locator(".mfp-counter");

    public ProductPage(Page page) {
        super(page);
    }

    public Locator getProductHeading(String title) {
        return page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(title).setLevel(1)
        );
    }

    public Locator getBrand(String brand) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(brand));
    }

    @Step("输入加购商品数量")
    public void addQty(String number) {
        qtyBox.fill(number);
    }

    @Step("点击加入购物车按钮")
    public void clickAdd2CartBtn() {
        add2CartBtn.click();
    }

    @Step("点击加入收藏夹")
    public void clickAdd2WishListBtn() {
        add2WishListBtn.click();
    }

    @Step("点击加入对比")
    public void clickCompareBtn() {
        compareBtn.click();
    }

    @Step("点击商品描述标签")
    public void clickDescriptionLink() {
        descriptionLink.click();
    }

    @Step("点击规格标签")
    public void clickSpecificationLink() {
        specificationLink.click();
    }

    @Step("点击评论标签")
    public void clickReviewsLink() {
        reviewsLink.click();
    }

    @Step("输入名称：{0}")
    public void addName(String name) {
        nameBox.fill(name);
    }

    @Step("输入内容：{0}")
    public void addReview(String review) {
        reviewBox.fill(review);
    }

    @Step("选择评分")
    public void chooseStar() {
        highestRating.check();
    }

    @Step("点击提交按钮")
    public void clickContinueBtn() {
        continueBtn.click();
    }

    @Step("点击主图缩略图")
    public void clickImg() {
        getMainThumbnail().click();
    }

    @Step("点击右箭头")
    public void clickRightArrow () {
        rightArrow.click();
    }

    @Step("点击左箭头")
    public void clickLeftArrow () {
        leftArrow.click();
    }
}
