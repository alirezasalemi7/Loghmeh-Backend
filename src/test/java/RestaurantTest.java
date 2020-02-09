package org.kharchal.co.resources;

import exceptions.FoodDoesntExistException;
import exceptions.FoodIsRegisteredException;
import exceptions.InvalidJsonInputException;
import exceptions.InvalidToJsonException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import structures.Food;
import structures.Location;
import structures.Restaurant;

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

    @BeforeClass
    public static void setup(){
        String content1, content2, content3;
        try {
            testRestaurant = getRestaurantFromJson("src/test/java/org/kharchal/co/resources/restaurantTest1.json");
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
        assertEquals("awsome!", testRestaurant.getDescription());
        assertEquals(0.633, testRestaurant.getAveragePopularity(), 0.001);
    }

    @Test
    public void testToJson(){
        try {
            String content = "{\"name\":\"iranberger\",\"description\":\"awsome!\",\"location\":{\"x\":12.0,\"y\":13.0},\"menu\":[{\"name\":\"gheime\",\"popularity\":0.8,\"price\":20000.0,\"description\":\"yummy!\"},{\"name\":\"bandari\",\"popularity\":0.8,\"price\":25000.0,\"description\":\"hoooot!\"},{\"name\":\"kebab\",\"popularity\":0.3,\"price\":215000.0,\"description\":\"tasty!\"}]}";
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
        Food food = new Food();
        try {
            food = Food.deserializeFromJson(new String(Files.readAllBytes(Paths.get("./src/test/java/com/food/resources/foodTest1.json"))));
            testRestaurant.addFood(food);
            assertEquals(0.633, testRestaurant.getAveragePopularity(), 0.01);
        } catch (InvalidJsonInputException | IOException e) {
            e.printStackTrace();
        } catch (FoodIsRegisteredException e) {
            assertEquals(food.getName() + " is already registered in " + testRestaurant.getName(), e.getMessage());
        }
    }

    void testGetLocation(Restaurant restaurant, double expectedX, double expectedY) {
        Location location1 = restaurant.getLocation();
        assertEquals(expectedX, location1.getX(), 0.0);
        assertEquals(expectedY, location1.getY(), 0.0);
    }

    @Test
    public void convertFromJsonTest(){
    }

    @AfterClass
    public static void teardown(){
        testRestaurant = null;
    }
}
