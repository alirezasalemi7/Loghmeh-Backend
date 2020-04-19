import exceptions.*;
import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {
    static User _user;

//    @Before
//    public void setup() throws UnregisteredOrderException {
//        _user = new User(new Location(4, 5), "mammad", "mammadi", "09101010102", "mmmd@gmail.com", 12000.0);
//        try {
//            NormalFood food1 = new NormalFood("gheime", "yummy1", 0.45, 1000.0, "image1", "123");
//            NormalFood food2 = new NormalFood("kabab", "yummy2", 0.45, 1000.0, "image2", "123");
//            NormalFood food3 = new NormalFood("falafel", "yummy2", 0.45, 1000.0, "image3", "123");
//            _user.addToCart(food1,"123");
//            _user.addToCart(food2,"123");
//            _user.addToCart(food1,"123");
//            _user.addToCart(food3,"123");
//            _user.addToCart(food1,"123");
//            _user.addToCart(food2,"123");
//        } catch (InvalidPopularityException | InvalidPriceException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test(expected = UnregisteredOrderException.class)
//    public void testAddToCart() throws UnregisteredOrderException, InvalidPopularityException, InvalidPriceException {
//        Cart cart = _user.getCart();
//        NormalFood food1 = new NormalFood("gheime", "yummy1", 0.45, 1000.0, "image1", "123");
//        assertEquals(3, cart.getOrders().size(), 0.0);
//        assertEquals(3, cart.getOrders().get(0).getCount(), 0.0);
//        assertEquals(2, cart.getOrders().get(1).getCount(), 0.0);
//        assertEquals(1, cart.getOrders().get(2).getCount(), 0.0);
//        _user.addToCart(food1, "124");
//    }
//
//    @Test
//    public void testGetters() {
//        assertEquals(_user.getLocation().getX(), 4, 0.0);
//        assertEquals(_user.getCredit(), 12000.0, 0.0);
//        assertEquals(_user.getEmail(), "mmmd@gmail.com");
//        assertEquals(_user.getName(), "mammad");
//        assertEquals(_user.getFamily(), "mammadi");
//        assertEquals(_user.getPhoneNumber(), "09101010102");
//    }
//
//    @Test
//    public void testFinalizeOrder() throws CartIsEmptyException, CreditIsNotEnoughException {
//        ArrayList<OrderItem> orders = _user.finalizeOrder();
//        assertEquals(3, orders.size(), 0.0);
//    }
//
//    @Test(expected = CreditIsNotEnoughException.class)
//    public void testFinalizeOrderCartIsEmptyException() throws CartIsEmptyException, CreditIsNotEnoughException {
//        _user.setCredit(0.0);
//        ArrayList<OrderItem> orders = _user.finalizeOrder();
//    }
//
//    @Test(expected = CartIsEmptyException.class)
//    public void testFinalizeOrderCreditIsNotEnoughException() throws CartIsEmptyException, CreditIsNotEnoughException {
//        _user.finalizeOrder();
//        ArrayList<OrderItem> orders = _user.finalizeOrder();
//    }
//
//    @After
//    public void teardown() {
//        _user = null;
//    }

}
