package api;

import com.github.javafaker.Faker;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import helpers.PropertiesHelper;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected String baseUrl;
    protected Faker faker = new Faker();
    protected Playwright playwright;
    protected APIRequestContext requestContext;
    protected Map<String, String> authData = new HashMap<>();

    @BeforeAll
    public void initEnv() {
        playwright = Playwright.create();
        baseUrl = PropertiesHelper.getProperty("api.url");
        authData.put("username", PropertiesHelper.getProperty("api.username"));
        authData.put("password", PropertiesHelper.getProperty("api.password"));
    }

    @BeforeEach
    public void createContext() {
        requestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl));
    }

    @AfterEach
    public void closeContext() {
        requestContext.dispose();
    }

    @AfterAll
    public void closePlaywright() {
        playwright.close();
    }
}
