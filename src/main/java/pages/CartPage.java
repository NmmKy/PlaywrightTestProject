package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.Header;

import java.util.List;

public class CartPage extends BasePage {
    private final String productsNamesLocator = "//div[@class='cart_list']//div[@class='inventory_item_name']";
    private final String checkoutBtnLocator = "//button[@data-test='checkout']";
    private final String firstNameLocator = "//input[@data-test='firstName']";
    private final String lastNameLocator = "//input[@data-test='lastName']";
    private final String postalCodeLocator = "//input[@data-test='postalCode']";
    private final String continueBtnLocator = "//input[@data-test='continue']";
    private final String finishBtnLocator = "//button[@data-test='finish']";
    private final String headerLocator = "//h2[@data-test='complete-header']";
    private final String removeProductBtnLocator = "//button[.='Remove']";
    private Header header;

    public CartPage(Page page) {
        super(page);
        this.page = page;
        header = new Header(page);
    }

    public List<String> getProductsNames() {
        return page.locator(productsNamesLocator).allTextContents();
    }

    public CartPage clickCheckoutBtn(){
        page.locator(checkoutBtnLocator).click();
        return this;
    }

    public CartPage clickRemoveProductBtn(){
        page.locator(removeProductBtnLocator).click();
        return this;
    }

    public CartPage fillOrderInfo(String firstName, String lastName, String postalCode){
        page.fill(firstNameLocator, firstName);
        page.fill(lastNameLocator, lastName);
        page.fill(postalCodeLocator, postalCode);
        return this;
    }

    public CartPage clickContinueBtn(){
        page.locator(continueBtnLocator).click();
        return this;
    }

    public CartPage clickFinishBtn(){
        page.locator(finishBtnLocator).click();
        return this;
    }

    public Locator getHeader() {
        return page.locator(headerLocator);
    }
}
