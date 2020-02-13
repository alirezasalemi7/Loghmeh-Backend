import models.Location;
import models.Restaurant;
import org.junit.BeforeClass;
import org.junit.Test;
import web.html.HtmlPageMaker;

import java.util.ArrayList;

public class HtmlPageMakerTest {
    static ArrayList<Restaurant> restaurants;
    static HtmlPageMaker pageMaker;
    @BeforeClass
    public static void setup() {
        Restaurant restaurant = new Restaurant("naminoland", "1", "https://picsum.photos/536/354", "good!", new Location(0, 0));
        restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        pageMaker = new HtmlPageMaker();
    }

    @Test
    public void testMakeAllRestaurantsPage() {
        System.out.println(pageMaker.makeAllRestaurantsPage(restaurants));
    }
}
