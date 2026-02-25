package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.Header;

public class ProductPage extends BasePage {
    private final String productNameLocator = "//div[@data-test='inventory-item-name']";
    private final String productImageLocator = "//img[@class='inventory_details_img']";
    private final String productDescriptionLocator = "//div[@data-test='inventory-item-desc']";
    private final String productPriceLocator = "//div[@data-test='inventory-item-price']";
    private final String addToCartBtnLocator = "//button[@name='add-to-cart']";
    private Header header;

    public ProductPage(Page page) {
        super(page);
        this.page = page;
        header = new Header(page);
    }

    public String getProductName() {
        return page.locator(productNameLocator).innerText();
    }

    public String getProductDescription() {
        return page.locator(productDescriptionLocator).innerText();
    }

    public Locator getProductImage() {
        return page.locator(productImageLocator);
    }

    public String getProductPrice() {
        return page.locator(productPriceLocator).innerText();
    }

    public ProductPage clickAddToCartBtn(){
        page.locator(addToCartBtnLocator).click();
        return this;
    }

    public CartPage clickOnCart() {
        header.clickOnCart();
        return new CartPage(page);
    }
}
