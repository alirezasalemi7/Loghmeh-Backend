import exceptions.InvalidJsonInputException;
import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;
import exceptions.InvalidToJsonException;
import models.NormalFood;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Food;

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
            expectedFood1 = new NormalFood("gheime", "yummy!", 0.8, 20000, "image1", "123");
        } catch (InvalidPopularityException | InvalidPriceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNewFood() {
        assertEquals(expectedFood1.getName(), "gheime");
        assertEquals(expectedFood1.getDescription(), "yummy!");
        assertEquals(expectedFood1.getPopularity(), 0.8, 0.000001);
        assertEquals(expectedFood1.getPrice(), 20000, 0.000001);
    }

    @Test
    public void testSetters() {
        expectedFood1.setRestaurantId("1234");
        expectedFood1.setPrice(10000);
        expectedFood1.setPopularity(0.9);
        expectedFood1.setName("ghieme sib zamini");
        expectedFood1.setDescription("yummi");
        assertEquals("ghieme sib zamini", expectedFood1.getName());
        assertEquals("yummi", expectedFood1.getDescription());
        assertEquals(0.9, expectedFood1.getPopularity(), 0.000001);
        assertEquals(10000, expectedFood1.getPrice(), 0.000001);
        assertEquals("1234", expectedFood1.getRestaurantId());
    }

    @After
    public void teardown(){
        expectedFood1 = null;
    }
}
