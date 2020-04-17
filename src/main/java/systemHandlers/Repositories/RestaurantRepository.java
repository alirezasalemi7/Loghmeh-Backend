package systemHandlers.Repositories;

import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import models.Location;

import java.util.ArrayList;

public class RestaurantRepository {

    private static RestaurantRepository instance;

    private RestaurantRepository() {}

    public static RestaurantRepository getInstance() {
        if (instance == null)
            instance = new RestaurantRepository();
        return instance;
    }

    public RestaurantDAO getRestaurantById(String restaurantId) throws RestaurantDoesntExistException {
        return new RestaurantDAO();
    }

    public ArrayList<RestaurantDAO> getAllRestaurants() {
        return new ArrayList<RestaurantDAO>();
    }

    public FoodDAO getFoodById(String restaurantId, String foodId) throws FoodDoesntExistException {
        return new FoodDAO();
    }

    public ArrayList<FoodDAO> getSpecialFoods() {
        return new ArrayList<FoodDAO>();
    }

    public void setFoodCount(String restaurantId, String foodId, int count) {
        return;
    }

    public int getFoodCount(String restaurantId, String foodId) {
        return 0;
    }

    public Location getRestaurantLocation(String restaurantId) {
        return new Location();
    }

}
