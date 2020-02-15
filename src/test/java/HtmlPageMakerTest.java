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
import static org.junit.Assert.*;
import web.html.HtmlPageMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void testMakeAllRestaurantsPage() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/allRestaurants.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeAllRestaurantsPage(restaurants).replaceAll("[\t\n ]", ""));
    }

    @Test
    public void testMakeRestaurantPage() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/restaurantPage.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeRestaurantPage(restaurants.get(0)).replaceAll("[\t\n ]", ""));
    }

    @Test
    public void testMakeProfilePage() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/profilePage.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeProfilePage(user, false, true).replaceAll("[\t\n ]", ""));
    }

    @Test
    public void testMakeCartPage() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/cartPage.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeCartPage(user).replaceAll("[\t\n ]", ""));
    }

    @Test
    public void testMakeOrderFinalizedPage() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/finalizeOrderPage.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeOrderFinalizedPage(user.getCart().getOrders(), user).replaceAll("[\t\n ]", ""));
    }

    @Test
    public void testErrorPages() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/htmlResources/errorPage.html")));
        assertEquals(content.replaceAll("[\t\n ]", ""), pageMaker.makeNotEnoughCreditPage(user).replaceAll("[\t\n ]", ""));
    }

    @AfterClass
    public static void teardown() {
        user = null;
        restaurants.clear();
        pageMaker = null;
    }
}
