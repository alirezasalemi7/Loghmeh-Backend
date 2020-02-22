import exceptions.*;
import org.junit.*;
import models.NormalFood;
import models.Location;
import models.Restaurant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestaurantTest {

    static Restaurant testRestaurant;

    static Restaurant getRestaurantFromJson(String path) throws IOException, InvalidJsonInputException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return Restaurant.deserializeFromJson(content);
    }

    @Before
    public void setup(){
        try {
            testRestaurant = getRestaurantFromJson("src/test/resources/restaurantTest1.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidJsonInputException e) {
            assertEquals("invalid json input format.", e.getMessage());
        }
    }

    @Test
    public void testDeserializeFromJson() {
        testGetLocation(testRestaurant, 12, 13);
        assertEquals("iranberger", testRestaurant.getName());
        assertEquals("123", testRestaurant.getId());
        assertEquals("logoAddress", testRestaurant.getLogoAddress());
        assertEquals(0.55, testRestaurant.getAveragePopularity(), 0.001);
        assertEquals("image2", testRestaurant.getMenu().get(0).getImageAddress());
    }

    @Test
    public void testToJson(){
        try {
            String content = "{\"name\":\"iranberger\",\"id\":\"123\",\"logo\":\"logoAddress\",\"location\":{\"x\":12.0,\"y\":13.0},\"menu\":[{\"name\":\"bandari\",\"description\":\"hoooot!\",\"popularity\":0.8,\"price\":25000.0,\"image\":\"image2\"},{\"name\":\"kebab\",\"description\":\"tasty!\",\"popularity\":0.3,\"price\":215000.0,\"image\":\"image1\"}]}";
            assertEquals(content, testRestaurant.toJson());
        } catch (InvalidToJsonException e) {
            assertEquals("invalid object. cannot convert to json.", e.getMessage());
        }
    }

    @Test(expected = FoodDoesntExistException.class)
    public void testGetFoodByName() throws FoodDoesntExistException {
        assertNotNull(testRestaurant.getFoodByName("bandari"));
        assertNotNull(testRestaurant.getFoodByName("joje"));
    }

    @Test
    public void testAddFood() {
        NormalFood food = new NormalFood();
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/test/resources/foodTest1.json")));
            food = new NormalFood("gheime", "yummy!", 0.8, 2000000, "img1", "1");
            testRestaurant.addFood(food);
            assertEquals(0.633, testRestaurant.getAveragePopularity(), 0.01);
        } catch (IOException | InvalidPopularityException e) {
            e.printStackTrace();
        } catch (FoodIsRegisteredException e) {
            assertEquals(food.getName() + " is already registered in " + testRestaurant.getName(), e.getMessage());
        } catch (InvalidPriceException e) {
            e.printStackTrace();
        }
    }

    void testGetLocation(Restaurant restaurant, double expectedX, double expectedY) {
        Location location1 = restaurant.getLocation();
        assertEquals(expectedX, location1.getX(), 0.0);
        assertEquals(expectedY, location1.getY(), 0.0);
    }

    @After
    public void teardown(){
        testRestaurant = null;
    }
}
