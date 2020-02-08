import exceptions.InvalidJsonInputException;
import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;
import exceptions.InvalidToJsonException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import structures.Food;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FoodTest {

    Food food;
    Food expectedFood1;

    @Before
    public void setup() {
        try {
            expectedFood1 = new Food("gheime", "yummy!", 0.8, 20000, "iranberger");
        } catch (InvalidPopularityException | InvalidPriceException e) {
            e.printStackTrace();
        }
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("./src/test/java/org/kharchal/co/resources/foodTest1.json")));
            food = Food.deserializeFromJson(content);
        } catch (InvalidJsonInputException e) {
            System.err.println(e.getMessage());
            System.err.println(content);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Address is invalid.");
        } catch (IOException e) {
            System.err.println("invalid address");
        }
    }

    @Test
    public void testDeserializeFromJson() {
        assertEquals(expectedFood1.getName(), food.getName());
        assertEquals(expectedFood1.getDescription(), food.getDescription());
        assertEquals(expectedFood1.getPopularity(), food.getPopularity(), 0.000001);
        assertEquals(expectedFood1.getPrice(), food.getPrice(), 0.000001);
        assertEquals(expectedFood1.getRestaurantName(), food.getRestaurantName());
    }

    @Test
    public void testToJson() {
        try {
            String content = "{\"name\":\"gheime\",\"description\":\"yummy!\",\"popularity\":0.8,\"price\":20000.0}";
            assertEquals(content, food.toJson());
        } catch (InvalidToJsonException e) {
            assertEquals("invalid object. cannot convert to json.", e.getMessage());
        }
    }

    @Test
    public void testSetters() {
        food.setRestaurantName("Restoran");
        food.setPrice(10000);
        food.setPopularity(0.9);
        food.setName("ghieme sib zamini");
        food.setDescription("yummi");
        assertEquals("ghieme sib zamini", food.getName());
        assertEquals("yummi", food.getDescription());
        assertEquals(0.9, food.getPopularity(), 0.000001);
        assertEquals(10000, food.getPrice(), 0.000001);
        assertEquals("Restoran", food.getRestaurantName());
    }

    @After
    public void teardown(){
        food = null;
    }
}
