import exceptions.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import structures.Food;
import structures.Location;
import structures.Restaurant;
import structures.User;
import systemHandlers.SystemManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemManagerTest {

    static SystemManager systemManager = SystemManager.getInstance();
    User user = new User(new Location(0, 0));
    static Restaurant r1,r2,r3,r4,r5;
    static Food f1,f2,f3,f4,f5,f6,f7,f8,f9,f10;

    @BeforeClass
    public static void setup(){
        r1 = new Restaurant("naz","1","logo1", "heaven taste!!", new Location(54,87));
        r2 = new Restaurant("naminoland","2","logo2", "cheap", new Location(154,457));
        r3 = new Restaurant("rafael","3","logo3", "Lux", new Location(20,20));
        r4 = new Restaurant("jenis","4","logo4", "the same!", new Location(210,920));
        r5 = new Restaurant("ayda","5","logo5", "duplex!", new Location(789,20));
        try {
            f1 = new Food("bandari", "inspiring!", 0.99, 12000, "naz");
            f2 = new Food("felafel", "awful but cheap", 0.51, 7000, "naz");
            f3 = new Food("olvieh", "good", 0.7, 10000, "naminoland");
            f4 = new Food("bandari", "awful!!!", 0.2, 10500, "naminoland");
            f5 = new Food("soltani", "royal", 0.9, 120000, "rafael");
            f6 = new Food("fish", "see taste", 0.6, 80000, "rafael");
            f7 = new Food("large pizza", "cheap", 0.8, 18000, "jenis");
            f8 = new Food("hot dog", "hmmm!", 0.98, 14000, "jenis");
            f9 = new Food("italian pizza", "italian!!!", 0.7, 38000, "ayda");
            f10 = new Food("nugget", "delicious", 0.75, 22000, "ayda");
            r1.addFood(f1);
            r1.addFood(f2);
            r2.addFood(f3);
            r2.addFood(f4);
            r3.addFood(f5);
            r3.addFood(f6);
            r4.addFood(f7);
            r4.addFood(f8);
            r5.addFood(f9);
            r5.addFood(f10);
        }
        catch (InvalidPopularityException e){
            System.err.println(e.getMessage());
        }
        catch (InvalidPriceException e){
            System.err.println(e.getMessage());
        }
        catch (FoodIsRegisteredException e){
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void _1_addandGetRestaurantWithoutExceptionTest(){
        try {
            System.err.println(r1);
            System.err.println("-----------------------------------------------");
            systemManager.addRestaurant(r1);
            systemManager.addRestaurant(r2);
            systemManager.addRestaurant(r3);
            systemManager.addRestaurant(r4);
            systemManager.addRestaurant(r5);
            System.err.println(systemManager.getRestaurantByName("ayda"));
            System.err.println("-----------------------------------------------");
            assertEquals(r5, systemManager.getRestaurantByName("ayda"));
            assertEquals(r4, systemManager.getRestaurantByName("jenis"));
            assertEquals(r3, systemManager.getRestaurantByName("rafael"));
            assertEquals(r2, systemManager.getRestaurantByName("naminoland"));
            assertEquals(r1, systemManager.getRestaurantByName("naz"));
        }
        catch (RestaurantIsRegisteredException e){
            System.err.println(e.getMessage());
        }
        catch (RestaurantDoesntExistException e){
            System.err.println(e.getMessage());
        }
    }

    @Test(expected = RestaurantIsRegisteredException.class)
    public void _2_addRestaurantWithExceptionTest() throws RestaurantIsRegisteredException{
        systemManager.addRestaurant(r2);
    }

    @Test(expected = RestaurantDoesntExistException.class)
    public void _3_getRestaurantWithExceptionTest() throws RestaurantDoesntExistException{
        systemManager.getRestaurantByName("mehran");
    }

    @Test
    public void _4_getAllRestaurantsTest(){
        ArrayList restaurants = systemManager.getAllRestaurants();
        assertEquals(restaurants.size(), 5);
    }

    @Test
    public void _5_getFoodWithoutExceptionTest() throws FoodDoesntExistException,RestaurantDoesntExistException {
        assertEquals(f1, systemManager.getFood(f1.getRestaurantName(),f1.getName()));
        assertEquals(f2, systemManager.getFood(f2.getRestaurantName(),f2.getName()));
        assertEquals(f3, systemManager.getFood(f3.getRestaurantName(),f3.getName()));
        assertEquals(f4, systemManager.getFood(f4.getRestaurantName(),f4.getName()));
        assertEquals(f5, systemManager.getFood(f5.getRestaurantName(),f5.getName()));
        assertEquals(f6, systemManager.getFood(f6.getRestaurantName(),f6.getName()));
        assertEquals(f7, systemManager.getFood(f7.getRestaurantName(),f7.getName()));
        assertEquals(f8, systemManager.getFood(f8.getRestaurantName(),f8.getName()));
        assertEquals(f9, systemManager.getFood(f9.getRestaurantName(),f9.getName()));
        assertEquals(f10, systemManager.getFood(f10.getRestaurantName(),f10.getName()));
    }

    @Test(expected = RestaurantDoesntExistException.class)
    public void _6_getFoodWithExceptionTest() throws FoodDoesntExistException,RestaurantDoesntExistException{
        systemManager.getFood("naser", "balal");
    }

    @Test
    public void _7_getRecommendedRestaurantsTest(){
        ArrayList<Restaurant> restaurants = systemManager.getRecommendedRestaurants(user);
        assertEquals(r3.getName(), restaurants.get(0).getName());
        assertEquals(r1.getName(), restaurants.get(1).getName());
        assertEquals(r2.getName(), restaurants.get(2).getName());
    }




}
