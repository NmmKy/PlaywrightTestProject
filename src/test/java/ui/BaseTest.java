package ui;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;
import helpers.PropertiesHelper;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import configuration.BrowserFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;
    protected LoginPage loginPage;
    protected Faker faker = new Faker();
    protected String browserType;
    protected String login;
    protected String password;

    @BeforeAll
    public void initBrowser() {
        playwright = Playwright.create();
        browserType = PropertiesHelper.getProperty("browser");
        browser = BrowserFactory.getBrowser(playwright, browserType);
        login = PropertiesHelper.getProperty("ui.username");
        password = PropertiesHelper.getProperty("ui.password");
    }

    @BeforeEach
    public void createContext() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
        loginPage = new LoginPage(page);
    }

    @AfterEach
    public void closeContext() {
        browserContext.close();
    }

    @AfterAll
    public void closeBrowser() {
        browser.close();
        playwright.close();
    }
}
