import exceptions.InvalidJsonInputException;
import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import structures.Cart;
import structures.Restaurant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartTest {
    static Cart _cart;
    static Restaurant r1, r2;


    static Restaurant getRestaurantFromJson(String path) throws IOException, InvalidJsonInputException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return Restaurant.deserializeFromJson(content);
    }

    @BeforeClass
    public static void setup() {
        try {
            r1 = getRestaurantFromJson("./src/test/java/com/food/resources/restaurantTest1.json");
            r2 = getRestaurantFromJson("./src/test/java/com/food/resources/restaurantTest2.json");
            _cart = new Cart();
        } catch (IOException | InvalidJsonInputException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnregisteredOrderException.class)
    public void testAddOrder() throws UnregisteredOrderException {
        _cart.addOrder("joj", r2.getName());
        _cart.addOrder("ghafghazi", r2.getName());
        _cart.addOrder("joj", r2.getName());
        _cart.addOrder("bandari", r1.getName());
    }

    @Test
    public void testGetOrder() {
        HashMap<String, Integer> orders = _cart.getOrders();
        assertEquals(2, orders.size());
        assertEquals(orders.get("joj"), 2, 0.0);
        assertEquals(orders.get("ghafghazi"), 1, 0.0);
        assertNull(orders.get("bandari"));
    }

    @Test
    public void testToJson() {
        try {
            assertEquals("{\"foods\":[{\"foodName\":\"ghafghazi\",\"count\":1},{\"foodName\":\"joj\",\"count\":2}]}", _cart.toJson());
        } catch (InvalidToJsonException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void teardown() {
        _cart = null;
        r1 = null;
        r2 = null;
    }

}
