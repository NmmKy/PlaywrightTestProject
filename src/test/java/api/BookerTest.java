package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class BookerTest extends BaseTest {
    private String token;
    private final String createdFirstName = faker.letterify("??????");
    private final String createdLastName = faker.letterify("????????");
    private final Long createdTotalPrice = faker.number().randomNumber(4, true);
    private final Boolean createdDepositPaid = faker.bool().bool();
    private final String createdCheckin = new SimpleDateFormat("yyyy-MM-dd").format(faker.date()
            .past(1,TimeUnit.DAYS));
    private final String createdCheckout = new SimpleDateFormat("yyyy-MM-dd").format(faker.date()
            .future(1, TimeUnit.DAYS));
    private final String createdAdditionalNeeds = faker.letterify("?????????");
    private final String regEx = "^\"|\"$";

    @Test
    public void healthCheckTest() {
        APIResponse apiResponse = requestContext.get("/ping");
        int statusCode = apiResponse.status();
        assertEquals(201, statusCode, "Код ответа " + statusCode);
    }

    @Test
    public void authTest() {
        APIResponse apiResponse = requestContext.post("/auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(authData));
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        token = getToken(apiResponse);
        assertFalse(token.isEmpty(), "Получен пустой токен");
    }

    @Test
    public void getBookingIdsTest() {
        APIResponse apiResponse = requestContext.get("/booking");
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        String responseBody = apiResponse.text();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        assertFalse(jsonArray.isEmpty(), "Получен пустой массив");

        Stream<JsonElement> jsonArrayStream = StreamSupport.stream(jsonArray.spliterator(), false);
        assertTrue(jsonArrayStream.allMatch(jsonElement -> jsonElement.getAsJsonObject().has("bookingid")),
                "В ответе нет поля bookingid");
    }

    @Test
    public void createBookingTest() {
        Map<String, Object> bookingData = setBookingData();
        APIResponse apiResponse = requestContext.post("/booking", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(bookingData));

        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);
        String responseBody = apiResponse.text();

        JsonObject bookingObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String bookingId = bookingObject.get("bookingid").toString();
        JsonObject booking = bookingObject.getAsJsonObject("booking");

        String firstName = booking.get("firstname").toString().replaceAll(regEx, "");
        String lastName = booking.get("lastname").toString().replaceAll(regEx, "");
        String totalPrice = booking.get("totalprice").toString().replaceAll(regEx, "");
        String depositPaid = booking.get("depositpaid").toString().replaceAll(regEx, "");

        JsonObject bookingDates = booking.get("bookingdates").getAsJsonObject();
        String checkin = bookingDates.get("checkin").toString().replaceAll(regEx, "");
        String checkout = bookingDates.get("checkout").toString().replaceAll(regEx, "");
        String additionalNeeds = booking.get("additionalneeds").toString().replaceAll(regEx, "");

        assertFalse(bookingId.isEmpty(), "Поле bookingId пустое");
        assertEquals(createdFirstName, firstName, "Поле firstname не ожидаемое");
        assertEquals(createdLastName, lastName, "Поле lastname пустое");
        assertEquals(createdTotalPrice.toString(), totalPrice, "Поле totalprice не ожидаемое");
        assertEquals(createdDepositPaid.toString(), depositPaid, "Поле depositpaid не ожидаемое");
        assertEquals(createdCheckin, checkin, "Поле checkin не ожидаемое");
        assertEquals(createdCheckout, checkout, "Поле checkout не ожидаемое");
        assertEquals(createdAdditionalNeeds, additionalNeeds, "Поле additionalneeds не ожидаемое");
    }

    @Test
    public void getBookingTest() {
        APIResponse apiResponse = requestContext.get("/booking");
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        String responseBody = apiResponse.text();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        assertFalse(jsonArray.isEmpty(), "Получен пустой массив");

        List<JsonElement> bookingIds = setBookingIds(jsonArray);
        apiResponse = requestContext.get("/booking/" + bookingIds.get(0));
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        responseBody = apiResponse.text();
        JsonObject booking = JsonParser.parseString(responseBody).getAsJsonObject();
        String firstName = booking.get("firstname").toString().replaceAll(regEx, "");
        String lastName = booking.get("lastname").toString().replaceAll(regEx, "");
        String totalPrice = booking.get("totalprice").toString().replaceAll(regEx, "");
        String depositPaid = booking.get("depositpaid").toString().replaceAll(regEx, "");

        JsonObject bookingDates = booking.get("bookingdates").getAsJsonObject();
        String checkin = bookingDates.get("checkin").toString().replaceAll(regEx, "");
        String checkout = bookingDates.get("checkout").toString().replaceAll(regEx, "");
        String additionalNeeds = booking.get("additionalneeds").toString().replaceAll(regEx, "");

        assertFalse(firstName.isEmpty(), "Поле firstname пустое");
        assertFalse(lastName.isEmpty(), "Поле lastname пустое");
        assertFalse(totalPrice.isEmpty(), "Поле totalprice пустое");
        assertFalse(depositPaid.isEmpty(), "Поле depositpaid пустое");
        assertFalse(checkin.isEmpty(), "Поле checkin пустое");
        assertFalse(checkout.isEmpty(), "Поле checkout пустое");
        assertFalse(additionalNeeds.isEmpty(), "Поле additionalneeds пустое");
    }

    @Test
    public void updateBookingTest() {
        APIResponse apiResponse = requestContext.post("/auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(authData));
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);
        token = getToken(apiResponse);
        assertFalse(token.isEmpty(), "Получен пустой токен");

        apiResponse = requestContext.get("/booking");
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        String responseBody = apiResponse.text();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        assertFalse(jsonArray.isEmpty(), "Получен пустой массив");

        List<JsonElement> bookingIds = setBookingIds(jsonArray);
        Map<String, Object> bookingData = setBookingData();
        apiResponse = requestContext.put("/booking/" + bookingIds.get(0), RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Cookie", "token=" + token)
                .setData(bookingData));
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        responseBody = apiResponse.text();
        JsonObject booking = JsonParser.parseString(responseBody).getAsJsonObject();
        String firstName = booking.get("firstname").toString().replaceAll(regEx, "");
        String lastName = booking.get("lastname").toString().replaceAll(regEx, "");
        String totalPrice = booking.get("totalprice").toString().replaceAll(regEx, "");
        String depositPaid = booking.get("depositpaid").toString().replaceAll(regEx, "");

        JsonObject bookingDates = booking.get("bookingdates").getAsJsonObject();
        String checkin = bookingDates.get("checkin").toString().replaceAll(regEx, "");
        String checkout = bookingDates.get("checkout").toString().replaceAll(regEx, "");
        String additionalNeeds = booking.get("additionalneeds").toString().replaceAll(regEx, "");

        assertEquals(createdFirstName, firstName, "Поле firstname не ожидаемое");
        assertEquals(createdLastName, lastName, "Поле lastname пустое");
        assertEquals(createdTotalPrice.toString(), totalPrice, "Поле totalprice не ожидаемое");
        assertEquals(createdDepositPaid.toString(), depositPaid, "Поле depositpaid не ожидаемое");
        assertEquals(createdCheckin, checkin, "Поле checkin не ожидаемое");
        assertEquals(createdCheckout, checkout, "Поле checkout не ожидаемое");
        assertEquals(createdAdditionalNeeds, additionalNeeds, "Поле additionalneeds не ожидаемое");
    }

    @Test
    public void partialUpdateBookingTest() {
        APIResponse apiResponse = requestContext.post("/auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(authData));
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);
        token = getToken(apiResponse);
        assertFalse(token.isEmpty(), "Получен пустой токен");

        apiResponse = requestContext.get("/booking");
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        String responseBody = apiResponse.text();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        assertFalse(jsonArray.isEmpty(), "Получен пустой массив");

        List<JsonElement> bookingIds = setBookingIds(jsonArray);
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", createdFirstName);
        data.put("lastname", createdLastName);

        apiResponse = requestContext.patch("/booking/" + bookingIds.get(bookingIds.size() - 1),
                RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Cookie", "token=" + token)
                .setData(data));
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        responseBody = apiResponse.text();
        JsonObject booking = JsonParser.parseString(responseBody).getAsJsonObject();
        String firstName = booking.get("firstname").toString().replaceAll(regEx, "");
        String lastName = booking.get("lastname").toString().replaceAll(regEx, "");
        assertEquals(createdFirstName, firstName, "Поле firstname не ожидаемое");
        assertEquals(createdLastName, lastName, "Поле lastname не ожидаемое");
    }

    @Test
    public void deleteBookingTest() {
        APIResponse apiResponse = requestContext.post("/auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(authData));
        int statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);
        token = getToken(apiResponse);
        assertFalse(token.isEmpty(), "Получен пустой токен");

        apiResponse = requestContext.get("/booking");
        statusCode = apiResponse.status();
        assertEquals(200, statusCode, "Код ответа " + statusCode);

        String responseBody = apiResponse.text();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        assertFalse(jsonArray.isEmpty(), "Получен пустой массив");

        List<JsonElement> bookingIds = setBookingIds(jsonArray);
        apiResponse = requestContext.delete("/booking/" + bookingIds.get(0), RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Cookie", "token=" + token));
        statusCode = apiResponse.status();
        assertEquals(201, statusCode, "Код ответа " + statusCode);
    }

    private String getToken(APIResponse apiResponse) {
        String responseBody = apiResponse.text();
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        return jsonResponse.get("token").getAsString();
    }

    private List<JsonElement> setBookingIds(JsonArray jsonArray) {
        List<JsonElement> bookingIds = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            bookingIds.add(jsonObject.get("bookingid"));
        }
        return bookingIds;
    }

    private Map<String, Object> setBookingData() {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("firstname", createdFirstName);
        bookingData.put("lastname", createdLastName);
        bookingData.put("totalprice", createdTotalPrice);
        bookingData.put("depositpaid", createdDepositPaid);

        Map<String, String> createdBookingDates = new HashMap<>();
        createdBookingDates.put("checkin", createdCheckin);
        createdBookingDates.put("checkout", createdCheckout);

        bookingData.put("bookingdates", createdBookingDates);
        bookingData.put("additionalneeds", createdAdditionalNeeds);
        return bookingData;
    }

}
