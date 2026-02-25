package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.Header;
import components.SideNavMenu;

import java.util.List;

public class ProductsPage extends BasePage {
    private final String productNamesLocator = "//div[@data-test='inventory-item-name']";
    private final String sortFilterLocator = "//select[@data-test='product-sort-container']";
    private final String productBtnLocator = "//div[text()='%s']//following::button[1]";
    private final String pageTitleLocator = ".title";
    private final String productPricesLocator = "//div[@data-test='inventory-item-price']";
    private final String productNameLocator = "//div[@data-test='inventory-item-name'][.='%s']";
    private Header header;
    private SideNavMenu sideNavMenu;

    public ProductsPage(Page page) {
        super(page);
        this.page = page;
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
    }

    public Locator getTitle() {
        return page.locator(pageTitleLocator);
    }

    public List<String> getProductNames() {
        return page.locator(productNamesLocator).allTextContents();
    }

    public List<String> getProductPrices() {
        return page.locator(productPricesLocator).allTextContents();
    }

    public ProductsPage setSortFilter(String sortName){
        page.locator(sortFilterLocator).selectOption(sortName);
        return this;
    }

    public ProductsPage addProductToCart(String itemName){
        page.locator(String.format(productBtnLocator, itemName)).click();
        return this;
    }

    public ProductPage openProduct(String productName) {
        page.locator(String.format(productNameLocator, productName)).click();
        return new ProductPage(page);
    }

    public CartPage clickOnCart() {
        header.clickOnCart();
        return new CartPage(page);
    }

    public LoginPage clickLogout() {
        header.clickOnHamburgerIcon();
        sideNavMenu.clickOnLogout();
        return new LoginPage(page);
    }
}
