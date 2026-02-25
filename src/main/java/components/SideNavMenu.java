package components;

import com.microsoft.playwright.Page;

public class SideNavMenu {
    private final String logoutLinkLocator = "#logout_sidebar_link";
    private Page page;

    public SideNavMenu(Page page) {
        this.page = page;
    }

    public void clickOnLogout() {
        page.click(logoutLinkLocator);
    }
}
