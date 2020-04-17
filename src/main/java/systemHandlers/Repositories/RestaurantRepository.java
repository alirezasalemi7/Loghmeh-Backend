package systemHandlers.Repositories;

import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import models.Restaurant;
import org.graalvm.compiler.hotspot.stubs.OutOfBoundsExceptionStub;

import java.util.ArrayList;
import java.util.HashMap;

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

    public FoodDAO getFoodById(String foodId) throws FoodDoesntExistException {
        return new FoodDAO();
    }

}
