package systemHandlers.Repositories;

import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import database.Mappers.FoodMapper;
import database.Mappers.RestaurantMapper;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import exceptions.ServerInternalException;
import models.Location;
import org.javatuples.Triplet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantRepository {

    private final RestaurantMapper restaurantMapper;
    private final FoodMapper foodMapper;

    private static RestaurantRepository instance;

    private RestaurantRepository() throws ServerInternalException {
        try {
            foodMapper = new FoodMapper();
            restaurantMapper = new RestaurantMapper();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public static RestaurantRepository getInstance() throws ServerInternalException {
        if (instance == null)
            instance = new RestaurantRepository();
        return instance;
    }

    public RestaurantDAO getRestaurantById(String restaurantId) throws RestaurantDoesntExistException, ServerInternalException {
        try {
            RestaurantDAO restaurant = restaurantMapper.find(restaurantId);
            if (restaurant == null)
                throw new RestaurantDoesntExistException("Restaurant does not exist.");
            restaurant.setMenu(this.getNormalFoods(restaurantId));
            return restaurant;
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public HashMap<String, Boolean> getAllRestaurantIds() throws ServerInternalException {
        try {
            return restaurantMapper.getRestaurantsId();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public ArrayList<RestaurantDAO> getAllRestaurants() throws ServerInternalException {
        try {
            return restaurantMapper.getAllRestaurantsInfo();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public ArrayList<RestaurantDAO> getAllRestaurantsInRange(int pageNumber,int pageSize,Location location) throws ServerInternalException {
        try {
            return restaurantMapper.getAllRestaurantsInRangePageByPage(pageSize, pageNumber, 170, location);
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public ArrayList<RestaurantDAO> getRestaurantsMatchNameInRange(int pageNumber,int pageSize,Location location,String name) throws ServerInternalException {
        try {
            return restaurantMapper.getAllRestaurantsMatchNameAndInRange(name,170, location, pageNumber, pageSize);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public ArrayList<FoodDAO> getFoodsMatchNameInRange(int pageNumber,int pageSize,Location location,String name) throws ServerInternalException {
        try {
            return foodMapper.getFoodsMatchNameInUserRange(name,location, 170, pageNumber, pageSize);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public ArrayList<FoodDAO> getFoodsMatchNameAndRestaurantNameInRange(int pageNumber,int pageSize,Location location,String foodName,String restaurantName) throws ServerInternalException {
        try {
            return foodMapper.getFoodsMatchNameAndRestaurantNameInUserRange(foodName, restaurantName, location, 170, pageNumber, pageSize);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public FoodDAO getFoodById(String restaurantId, String foodId, Boolean isSpecial) throws FoodDoesntExistException, ServerInternalException {
        try {
            FoodDAO food = foodMapper.find(new Triplet<>(restaurantId, foodId, isSpecial));
            if (food == null)
                throw new FoodDoesntExistException(foodId + " does not exist in " + restaurantId);
            return food;
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public ArrayList<FoodDAO> getSpecialFoods() throws ServerInternalException {
        try {
            return foodMapper.getAllSpecialFoods();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public void setFoodCount(String restaurantId, String foodId, int count) throws ServerInternalException {
        try {
            foodMapper.updateFoodCount(restaurantId, foodId, count);
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public int getFoodCount(String restaurantId, String foodId) throws ServerInternalException {
        try {
            FoodDAO food = foodMapper.find(new Triplet<>(restaurantId, foodId, true));
            return food.getCount();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public Location getRestaurantLocation(String restaurantId) throws ServerInternalException, RestaurantDoesntExistException {
        try {
            RestaurantDAO restaurant = restaurantMapper.find(restaurantId);
            if (restaurant == null)
                throw new RestaurantDoesntExistException(restaurantId + "does not exist.");
            return restaurant.getLocation();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void terminatePreviousFoodParty() throws ServerInternalException {
        try {
            foodMapper.deleteRedundantSpecialFoods();
            foodMapper.changeSpecialFoodsToNormal();
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public HashMap<String, Boolean> getNormalFoodIds(String restaurantId) throws ServerInternalException {
        try {
            return foodMapper.getNormalFoodIds(restaurantId);
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    private ArrayList<FoodDAO> getNormalFoods(String restaurantId) throws ServerInternalException {
        try {
            return foodMapper.getNormalFoods(restaurantId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void addFoods(ArrayList<FoodDAO> foods) throws ServerInternalException {
        try {
            foodMapper.insertAllFoods(foods);
        } catch (SQLException e) {
            throw new ServerInternalException();
        }
    }

    public void addRestaurants(ArrayList<RestaurantDAO> restaurants) throws ServerInternalException {
        try {
            restaurantMapper.insertAllRestaurants(restaurants);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

}
