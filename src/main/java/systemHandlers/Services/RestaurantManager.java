package systemHandlers.Services;

import com.sun.org.apache.bcel.internal.generic.RET;
import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import database.DAO.UserDAO;
import exceptions.FoodDoesntExistException;
import exceptions.OutOfRangeException;
import exceptions.RestaurantDoesntExistException;
import models.Location;
import models.Restaurant;
import restAPI.DTO.Restaurant.*;
import systemHandlers.Repositories.RestaurantRepository;
import systemHandlers.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantManager {

    private static RestaurantManager instance;

    private RestaurantManager() {}

    public static RestaurantManager getInstance() {
        if (instance == null)
            instance = new RestaurantManager();
        return instance;
    }

    public RestaurantListDTO getInRangeRestaurants(String userId) {
        ArrayList<RestaurantDAO> restaurants = RestaurantRepository.getInstance().getAllRestaurants();
        ArrayList<RestaurantInfoDTO> restaurantList = new ArrayList<>();
        UserDAO user = UserRepository.getInstance().getUser(userId);
        for (RestaurantDAO restaurant : restaurants) {
            if (this.isInRange(user.getLocation(), restaurant.getLocation())) {
                restaurantList.add(new RestaurantInfoDTO(restaurant.getName(), restaurant.getLogoAddress(), restaurant.getId()));
            }
        }
        return new RestaurantListDTO(restaurantList);
    }

    public RestaurantDTO getNearbyRestaurantById(String restaurantId, String userId) throws RestaurantDoesntExistException, OutOfRangeException {
        RestaurantDAO restaurant = RestaurantRepository.getInstance().getRestaurantById(restaurantId);
        UserDAO user = UserRepository.getInstance().getUser(userId);
        if (!this.isInRange(user.getLocation(), restaurant.getLocation()))
            throw new OutOfRangeException("Restaurant " + restaurant.getName() + "is not in range.");
        HashMap<String, Boolean> menu = restaurant.getMenu();
        ArrayList<FoodDTO> foods = new ArrayList<>();
        for (String foodId : menu.keySet()) {
            if (menu.get(foodId)) {
                FoodDAO food = null;
                try {
                    food = RestaurantRepository.getInstance().getFoodById(foodId);
                    foods.add(new FoodDTO(restaurantId, food.getRestaurantName(), food.getLogo()
                            , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription()));
                } catch (FoodDoesntExistException e) {
                    //never occurs here
                }
            }
        }
        return new RestaurantDTO(new RestaurantInfoDTO(restaurant.getName(), restaurant.getLogoAddress(), restaurant.getId()), foods);
    }

    public FoodDTO getFoodById(String restaurantId, String foodId) throws FoodDoesntExistException {
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(foodId);
        if (food.getRestaurantId().equals(restaurantId))
            return new FoodDTO(restaurantId, food.getRestaurantName(), food.getLogo()
                    , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription());
        return null;
    }

    public SpecialFoodDTO getSpecialFoodById(String restaurantId, String foodId) throws FoodDoesntExistException {
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(foodId);
        if (food.getRestaurantId().equals(restaurantId))
            return new SpecialFoodDTO(food.getId(), food.getRestaurantId(), food.getRestaurantName(), food.getLogo(), food.getPopularity()
                    , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice());
        return null;
    }

    public boolean isInRange(Location user, Location restaurant) {
        return user.getDistance(restaurant) <= 170;
    }

}
