import exceptions.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import models.Food;
import models.Location;
import models.Restaurant;
import models.User;
import systemHandlers.SystemManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemManagerTest {

    static SystemManager systemManager = SystemManager.getInstance();
    User user = new User(new Location(0, 0), "aUser", "anonymous", "09100000000", "aa@gmail.com", 1000.0);
    static Restaurant restaurant1,restaurant2,restaurant3,restaurant4,restaurant5;
    static Food food1,food2,food3,food4,food5,food6,food7,food8,food9,food10;

    @BeforeClass
    public static void setup(){
        restaurant1 = new Restaurant("naz","1","logo1", "heaven taste!!", new Location(54,87));
        restaurant2 = new Restaurant("naminoland","2","logo2", "cheap", new Location(154,457));
        restaurant3 = new Restaurant("rafael","3","logo3", "Lux", new Location(20,20));
        restaurant4 = new Restaurant("jenis","4","logo4", "the same!", new Location(210,920));
        restaurant5 = new Restaurant("ayda","5","logo5", "duplex!", new Location(789,20));
        try {
            food1 = new Food("bandari", "inspiring!", 0.99, 12000, "naz1", "1");
            food2 = new Food("felafel", "awful but cheap", 0.51, 7000, "naz2", "1");
            food3 = new Food("olvieh", "good", 0.7, 10000, "naminoland1", "2");
            food4 = new Food("bandari", "awful!!!", 0.2, 10500, "naminoland2", "2");
            food5 = new Food("soltani", "royal", 0.9, 120000, "rafael1", "3");
            food6 = new Food("fish", "see taste", 0.6, 80000, "rafael2", "3");
            food7 = new Food("large pizza", "cheap", 0.8, 18000, "jenis1", "4");
            food8 = new Food("hot dog", "hmmm!", 0.98, 14000, "jenis2", "4");
            food9 = new Food("italian pizza", "italian!!!", 0.7, 38000, "ayda1", "5");
            food10 = new Food("nugget", "delicious", 0.75, 22000, "ayda2", "5");
            restaurant1.addFood(food1);
            restaurant1.addFood(food2);
            restaurant2.addFood(food3);
            restaurant2.addFood(food4);
            restaurant3.addFood(food5);
            restaurant3.addFood(food6);
            restaurant4.addFood(food7);
            restaurant4.addFood(food8);
            restaurant5.addFood(food9);
            restaurant5.addFood(food10);
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
    public void test1AddAndGetRestaurantWithoutException(){
        try {
            systemManager.addRestaurant(restaurant1);
            systemManager.addRestaurant(restaurant2);
            systemManager.addRestaurant(restaurant3);
            systemManager.addRestaurant(restaurant4);
            systemManager.addRestaurant(restaurant5);
            assertEquals(restaurant5, systemManager.getRestaurantById("5"));
            assertEquals(restaurant4, systemManager.getRestaurantById("4"));
            assertEquals(restaurant3, systemManager.getRestaurantById("3"));
            assertEquals(restaurant2, systemManager.getRestaurantById("2"));
            assertEquals(restaurant1, systemManager.getRestaurantById("1"));
        }
        catch (RestaurantIsRegisteredException e){
            System.err.println(e.getMessage());
        }
        catch (RestaurantDoesntExistException e){
            System.err.println(e.getMessage());
        }
    }

    @Test(expected = RestaurantIsRegisteredException.class)
    public void test2AddRestaurantWithException() throws RestaurantIsRegisteredException{
        systemManager.addRestaurant(restaurant2);
    }

    @Test(expected = RestaurantDoesntExistException.class)
    public void test3GetRestaurantWithException() throws RestaurantDoesntExistException{
        systemManager.getRestaurantById("12");
    }

    @Test
    public void test4GetAllRestaurants(){
        assertEquals(5, systemManager.getAllRestaurants().size(), 0.0);
    }

    @Test
    public void test5GetFoodWithoutException() throws FoodDoesntExistException,RestaurantDoesntExistException {
        assertEquals(food1, systemManager.getFood(food1.getRestaurantId(),food1.getName()));
        assertEquals(food2, systemManager.getFood(food2.getRestaurantId(),food2.getName()));
        assertEquals(food3, systemManager.getFood(food3.getRestaurantId(),food3.getName()));
        assertEquals(food4, systemManager.getFood(food4.getRestaurantId(),food4.getName()));
        assertEquals(food5, systemManager.getFood(food5.getRestaurantId(),food5.getName()));
        assertEquals(food6, systemManager.getFood(food6.getRestaurantId(),food6.getName()));
        assertEquals(food7, systemManager.getFood(food7.getRestaurantId(),food7.getName()));
        assertEquals(food8, systemManager.getFood(food8.getRestaurantId(),food8.getName()));
        assertEquals(food9, systemManager.getFood(food9.getRestaurantId(),food9.getName()));
        assertEquals(food10, systemManager.getFood(food10.getRestaurantId(),food10.getName()));
    }

    @Test(expected = RestaurantDoesntExistException.class)
    public void test6GetFoodWithException() throws FoodDoesntExistException,RestaurantDoesntExistException{
        systemManager.getFood("7", "balal");
    }

    @Test
    public void test7GetRecommendedRestaurants(){
        ArrayList<Restaurant> restaurants = systemManager.getRecommendedRestaurants(user);
        assertEquals(restaurant3.getName(), restaurants.get(0).getName());
        assertEquals(restaurant1.getName(), restaurants.get(1).getName());
        assertEquals(restaurant2.getName(), restaurants.get(2).getName());
    }

}
