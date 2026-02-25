package ui;

import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.ProductsPage;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsTest extends BaseTest {

    @Test
    public void buyProductTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        String firstItemName = productsPage.getProductNames().get(0);
        CartPage cartPage = productsPage.addProductToCart(firstItemName).clickOnCart();
        assertEquals(firstItemName, cartPage.getProductsNames().get(0),
                "Продукт не был добавлен в корзину");

        cartPage.clickCheckoutBtn()
                .fillOrderInfo(faker.letterify("?????"), faker.letterify("????"),
                        String.valueOf(faker.number().randomNumber(4, true)))
                .clickContinueBtn()
                .clickFinishBtn();

        assertEquals("Thank you for your order!", cartPage.getHeader().innerText(),
                "Заказ не был выполнен");
    }

    @Test
    public void removeProductFromCartTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        String firstItemName = productsPage.getProductNames().get(0);
        CartPage cartPage = productsPage.addProductToCart(firstItemName).clickOnCart();
        assertEquals(firstItemName, cartPage.getProductsNames().get(0),
                "Продукт не был добавлен в корзину");

        cartPage.clickRemoveProductBtn();
        assertTrue(cartPage.getProductsNames().isEmpty(), "Продукт не был удален из корзины");
    }

    @Test
    public void sortByNameAscProductsTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        assertEquals("Sauce Labs Backpack", productsPage.getProductNames().get(0),
                "Порядок сортировки не задан по умолчанию");

        List<String> sortedPrices = productsPage.getProductNames()
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        productsPage.setSortFilter("Name (Z to A)");
        List<String> sortedFilterPrices = productsPage.getProductNames();

        assertIterableEquals(sortedPrices, sortedFilterPrices,
                "Продукты не отсортированы");
    }

    @Test
    public void sortByNameDescProductsTest() {
        ProductsPage productsPage = loginPage.login(login, password);
        assertEquals("Sauce Labs Backpack", productsPage.getProductNames().get(0),
                "Порядок сортировки не задан по умолчанию");

        List<String> sortedNames = productsPage.getProductNames()
                .stream()
                .sorted()
                .toList();
        productsPage.setSortFilter("Name (A to Z)");
        List<String> sortedFilterNames = productsPage.getProductNames();

        assertIterableEquals(sortedNames, sortedFilterNames,
                "Продукты не отсортированы");
    }

    @Test
    public void sortByPriceLowToHighTest() {
        ProductsPage productsPage = loginPage.login(login, password);

        List<Double> sortedPrices = productsPage.getProductPrices()
                .stream()
                .map(price -> price.replaceAll("\\$", ""))
                .map(Double::parseDouble)
                .sorted()
                .toList();

        productsPage.setSortFilter("Price (low to high)");

        List<Double> sortedFilterPrices = productsPage.getProductPrices()
                .stream()
                .map(price -> price.replaceAll("\\$", ""))
                .map(Double::parseDouble)
                .toList();

        assertIterableEquals(sortedPrices, sortedFilterPrices,
                "Продукты не отсортированы");
    }

    @Test
    public void sortByPriceHighToLowTest() {
        ProductsPage productsPage = loginPage.login(login, password);

        List<Double> sortedPrices = productsPage.getProductPrices()
                .stream()
                .map(price -> price.replaceAll("\\$", ""))
                .map(Double::parseDouble)
                .sorted(Comparator.reverseOrder())
                .toList();

        productsPage.setSortFilter("Price (high to low)");

        List<Double> sortedFilterPrices = productsPage.getProductPrices()
                .stream()
                .map(price -> price.replaceAll("\\$", ""))
                .map(Double::parseDouble)
                .toList();

        assertIterableEquals(sortedPrices, sortedFilterPrices,
                "Продукты не отсортированы");
    }
}
