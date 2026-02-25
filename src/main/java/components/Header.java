package components;

import com.microsoft.playwright.Page;

public class Header {
    private final String hamburgerIconLocator = "#react-burger-menu-btn";
    private final String cartLocator = "//a[@data-test='shopping-cart-link']";
    private Page page;

    public Header(Page page) {
        this.page = page;
    }

    public void clickOnHamburgerIcon() {
        page.click(hamburgerIconLocator);
    }

    public void clickOnCart(){
        page.locator(cartLocator).click();
    }
}
