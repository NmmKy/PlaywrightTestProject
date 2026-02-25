package ui;

import org.junit.jupiter.api.Test;
import pages.ProductsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest extends BaseTest {

    @Test
    public void loginTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        assertEquals("Products", productsPage.getTitle().innerText(),
                "Страница Products не загрузилась");
    }

    @Test
    public void fakeUserLoginTest() {
        loginPage.login("625574", "ncjdenbsf4fg");
        assertEquals(loginPage.getErrorMessage().innerText(), "Epic sadface: " +
                "Username and password do not match any user in this service",
                "Сообщение об ошибке не появилось");
    }

    @Test
    public void logoutTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        productsPage.clickLogout();
        assertEquals(loginPage.getBaseUrl() + "/", page.url(),
                "Выход из системы не произошел");
    }
}
