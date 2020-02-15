import exceptions.*;
import models.Food;
import org.junit.*;
import org.junit.runners.MethodSorters;
import models.Cart;
import models.OrderItem;
import models.Restaurant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartTest {
    static Cart _cart;

    static Restaurant getRestaurantFromJson(String path) throws IOException, InvalidJsonInputException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return Restaurant.deserializeFromJson(content);
    }

    @Before
    public void setup() {
        _cart = new Cart();
        try {
            Food food1 = new Food("gheime", "yummy1", 0.45, 1000.0, "image1", "123");
            _cart.addOrder(food1, "123");
            _cart.addOrder(new Food("ghorme", "yummy2", 0.55, 2000.0, "image2", "123"), "123");
            _cart.addOrder(food1, "123");
        } catch (InvalidPopularityException | InvalidPriceException | UnregisteredOrderException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnregisteredOrderException.class)
    public void testAddOrder() throws UnregisteredOrderException {
        try {
            _cart.addOrder(new Food("ghorme2", "yummy2", 0.55, 2000.0, "image2", "124"), "124");
        } catch (InvalidPopularityException | InvalidPriceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOrder() {
        ArrayList<OrderItem> orders = _cart.getOrders();
        assertEquals(2, orders.size());
        assertEquals(orders.get(0).getCount(), 2, 0.0);
        assertEquals(orders.get(1).getCount(), 1, 0.0);
    }

    @Test
    public void testToJson() {
        try {
            assertEquals("{\"foods\":[{\"foodName\":\"gheime\",\"count\":2},{\"foodName\":\"ghorme\",\"count\":1}]}", _cart.toJson());
        } catch (InvalidToJsonException e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        _cart = null;
    }

}
