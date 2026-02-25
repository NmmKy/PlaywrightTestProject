package configuration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {
    public static Browser getBrowser(Playwright playwright, String browserType) {
        return switch (browserType) {
            case "firefox" -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)
                    .setSlowMo(2000));
            case "safari" -> playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)
                    .setSlowMo(2000));
            default -> playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
                    .setSlowMo(2000));
        };
    }
}
