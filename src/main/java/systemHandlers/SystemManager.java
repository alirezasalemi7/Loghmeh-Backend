package systemHandlers;

import exceptions.FoodIsRegisteredException;
import exceptions.RestaurantDoesntExistException;
import exceptions.RestaurantIsRegisteredException;
import structures.Food;
import structures.Restaurant;

import java.util.ArrayList;

public class SystemManager {


    private static SystemManager _instance;
    private DataHandler _dataHandler;

    private SystemManager() {
        _dataHandler = DataHandler.getInstance();
    }

    public static SystemManager getInstance() {
        if (_instance == null) {
            _instance = new SystemManager();
        }
        return _instance;
    }

    public void addRestaurant(Restaurant restaurant) throws RestaurantIsRegisteredException {
        if (_dataHandler.getAllRestaurant().containsKey(restaurant.getName())) {
            throw new RestaurantIsRegisteredException("Restaurant " + restaurant.getName() + " is already registered.");
        } else {
            _dataHandler.getAllRestaurant().put(restaurant.getName(), restaurant);
        }
    }


    public void addFood(Food food) throws RestaurantDoesntExistException, FoodIsRegisteredException {
        Restaurant restaurant = _dataHandler.getRestaurantByName(food.getRestaurantName());
        restaurant.addFood(food);
    }

    public ArrayList getAllRestaurants() {
        return new ArrayList(_dataHandler.getAllRestaurant().keySet());
    }
}
