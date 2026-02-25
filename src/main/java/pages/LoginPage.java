package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {
    private final String usernameLocator = "id=user-name";
    private final String passwordLocator = "id=password";
    private final String loginBtnLocator = "id=login-button";
    private final String errorMsgLocator = ".error-message-container h3";

    public LoginPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator getErrorMessage() {
        return page.locator(errorMsgLocator);
    }

    public ProductsPage login(String username, String password) {
        page.navigate(baseUrl);
        page.fill(usernameLocator, username);
        page.fill(passwordLocator, password);
        page.click(loginBtnLocator);
        return new ProductsPage(page);
    }
}
