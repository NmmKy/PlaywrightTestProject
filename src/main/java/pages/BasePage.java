package pages;

import com.microsoft.playwright.Page;
import helpers.PropertiesHelper;

public class BasePage {
    protected Page page;
    protected String baseUrl;

    public BasePage(Page page) {
        this.page = page;
        baseUrl = PropertiesHelper.getProperty("ui.url");
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
