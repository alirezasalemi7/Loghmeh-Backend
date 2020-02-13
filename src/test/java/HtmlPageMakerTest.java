import exceptions.FoodIsRegisteredException;
import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;
import exceptions.UnregisteredOrderException;
import models.Food;
import models.Location;
import models.Restaurant;
import models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import web.html.HtmlPageMaker;

import java.util.ArrayList;

public class HtmlPageMakerTest {
    static ArrayList<Restaurant> restaurants;
    static HtmlPageMaker pageMaker;
    static User user;
    @BeforeClass
    public static void setup() {
        Restaurant restaurant = new Restaurant("naminoland", "1", "https://picsum.photos/536/354", "good!", new Location(0, 0));
        user = new User(new Location(0, 0), "ali", "alizade", "09110958496", "a.aliz@gmail.com", 13000.0);
        try {
            Food food1 = new Food("gheime", "yummy", 0.6, 13000, "https://picsum.photos/536/354", restaurant.getId());
            user.addToCart(food1, restaurant.getId());
            restaurant.addFood(food1);
        } catch (InvalidPopularityException | InvalidPriceException | FoodIsRegisteredException | UnregisteredOrderException e) {
            e.printStackTrace();
        }
        restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        pageMaker = new HtmlPageMaker();
    }

    @Test
    public void testMakeAllRestaurantsPage() {
        System.out.println(pageMaker.makeAllRestaurantsPage(restaurants));
    }

    @Test
    public void testMakeRestaurantPage() {
        System.out.println(pageMaker.makeRestaurantPage(restaurants.get(0)));
    }

    @Test
    public void testMakeProfilePage() {
        System.out.println(pageMaker.makeProfilePage(user));
    }

    @Test
    public void testmakeCartPage() {
        System.out.println(pageMaker.makeCartPage(user));
    }

    @AfterClass
    public static void teardown() {
        user = null;
        restaurants.clear();
        pageMaker = null;
    }
}
