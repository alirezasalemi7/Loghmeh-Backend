import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import structures.Cart;
import structures.Location;
import structures.User;

import static org.junit.Assert.assertEquals;

public class UserTest {
    static User _user;

    @BeforeClass
    public static void setup() throws UnregisteredOrderException {
        _user = new User(new Location(4, 5));
        _user.addToCart("joje","iranberger");
        _user.addToCart("kebab","iranberger");
        _user.addToCart("joje","iranberger");
        _user.addToCart("falafel","iranberger");
        _user.addToCart("joje","iranberger");
        _user.addToCart("kebab","iranberger");
    }

    @Test(expected = UnregisteredOrderException.class)
    public void testAddToCart() throws UnregisteredOrderException {
        Cart cart = _user.getCart();
        assertEquals(3, cart.getOrders().size(), 0.0);
        assertEquals(3, cart.getOrders().get("joje"), 0.0);
        assertEquals(2, cart.getOrders().get("kebab"), 0.0);
        assertEquals(1, cart.getOrders().get("falafel"), 0.0);
        _user.addToCart("joje", "moslem");
    }

    @Test
    public void testGetLocation() {
        assertEquals(4, _user.getLocation().getX(), 0.0);
        assertEquals(5, _user.getLocation().getY(), 0.0);
    }

    @Test
    public void testFinalizeOrder() {
        String output = null;
        try {
            output = _user.finalizeOrder();
        } catch (InvalidToJsonException e) {
            assertEquals("invalid object. cannot convert to json.", e.getMessage());
        }
        assertEquals("{\"foods\":[{\"foodName\":\"joje\",\"count\":3},{\"foodName\":\"kebab\",\"count\":2},{\"foodName\":\"falafel\",\"count\":1}]}", output);
    }

    @AfterClass
    public static void teardown() {
        _user = null;
    }

}
