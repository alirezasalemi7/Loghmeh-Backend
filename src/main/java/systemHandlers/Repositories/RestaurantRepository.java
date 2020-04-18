package systemHandlers.Repositories;

import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import database.Mappers.RestaurantMapper;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import exceptions.ServerInternalException;
import models.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantRepository {

    private RestaurantMapper restaurantMapper;
    private static RestaurantRepository instance;

    private RestaurantRepository() throws ServerInternalException {
        try {
            restaurantMapper = new RestaurantMapper();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public static RestaurantRepository getInstance() throws ServerInternalException {
        if (instance == null)
            instance = new RestaurantRepository();
        return instance;
    }

    public RestaurantDAO getRestaurantById(String restaurantId) throws RestaurantDoesntExistException {
        return new RestaurantDAO();
    }

    public HashMap<String, Boolean> getAllRestaurantIds() {
        return new HashMap<String, Boolean>();
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

    public void terminatePreviousFoodParty() {
        return;
    }

    public void addFoods(ArrayList<FoodDAO> foods) {
        return;
    }

    public void addRestaurants(ArrayList<RestaurantDAO> restaurants) {
        return;
    }

}
