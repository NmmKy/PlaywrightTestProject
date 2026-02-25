package ui;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.ProductPage;
import pages.ProductsPage;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest extends BaseTest {

    @Test
    public void productPageTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        String firstItemName = productsPage.getProductNames().get(0);
        ProductPage productPage = productsPage.openProduct(firstItemName);
        page.waitForFunction("img => img.complete && img.naturalHeight !== 0",
                productPage.getProductImage().elementHandle(),
                new Page.WaitForFunctionOptions().setTimeout(5000));

        assertEquals(firstItemName, productPage.getProductName(),
                "Продукт не был добавлен в корзину");
        assertFalse(productPage.getProductDescription().isEmpty(),
                "Описание продукта не загрузилось");
        assertFalse(productPage.getProductPrice().isEmpty(),
                "Цена продукта не загрузилась");

        CartPage cartPage = productPage.clickAddToCartBtn().clickOnCart();
        assertEquals(firstItemName, cartPage.getProductsNames().get(0),
                "Продукт не был добавлен в корзину");
    }
}
