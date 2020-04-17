package systemHandlers.Services;


import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import database.DAO.UserDAO;
import exceptions.FoodDoesntExistException;
import exceptions.OutOfRangeException;
import exceptions.RestaurantDoesntExistException;
import exceptions.UserDoesNotExistException;
import models.Location;
import restAPI.DTO.Restaurant.*;
import systemHandlers.Repositories.RestaurantRepository;
import systemHandlers.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RestaurantManager {

    private static RestaurantManager instance;
    private Date _foodPartyStartTime;

    private RestaurantManager() {}

    public static RestaurantManager getInstance() {
        if (instance == null)
            instance = new RestaurantManager();
        return instance;
    }

    public void setFoodPartStartTime(Date date){
        this._foodPartyStartTime = date;
    }

    public Date getFoodPartyStartTime(){
        return _foodPartyStartTime;
    }

    public RestaurantListDTO getInRangeRestaurants(String userId) throws UserDoesNotExistException {
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

    public RestaurantDTO getNearbyRestaurantById(String restaurantId, String userId) throws RestaurantDoesntExistException, UserDoesNotExistException,OutOfRangeException {
        RestaurantDAO restaurant = RestaurantRepository.getInstance().getRestaurantById(restaurantId);
        UserDAO user = UserRepository.getInstance().getUser(userId);
        if (!this.isInRange(user.getLocation(), restaurant.getLocation()))
            throw new OutOfRangeException("Restaurant " + restaurant.getName() + "is not in range.");
        HashMap<String, Boolean> menu = restaurant.getMenu();
        ArrayList<FoodDTO> foods = new ArrayList<>();
        for (String foodId : menu.keySet()) {
            if (!menu.get(foodId)) { //means its normal food
                FoodDAO food = null;
                try {
                    food = RestaurantRepository.getInstance().getFoodById(restaurantId, foodId);
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
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(restaurantId, foodId);
        return new FoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo()
                , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription());
    }

    public SpecialFoodDTO getSpecialFoodById(String restaurantId, String foodId) throws FoodDoesntExistException {
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(restaurantId, foodId);
        return new SpecialFoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo(), food.getPopularity()
                , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice());
    }

    public boolean isInRange(Location user, Location restaurant) {
        return user.getDistance(restaurant) <= 170;
    }

    public void setFoodCount(String restaurantId, String foodId, int newCount) {
        RestaurantRepository.getInstance().setFoodCount(restaurantId, foodId, newCount);
    }

    public int getFoodCount(String restaurantId, String foodId) {
        return RestaurantRepository.getInstance().getFoodCount(restaurantId, foodId);
    }

    public ArrayList<SpecialFoodDTO> getAllSpecialFoods() {
        ArrayList<FoodDAO> foods = RestaurantRepository.getInstance().getSpecialFoods();
        ArrayList<SpecialFoodDTO> specialFoods = new ArrayList<>();
        for (FoodDAO food : foods)
            specialFoods.add(new SpecialFoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo(), food.getPopularity()
                    , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice()));
        return specialFoods;
    }

    public Location getRestaurantLocation(String restaurantId) {
        return RestaurantRepository.getInstance().getRestaurantLocation(restaurantId);
    }

}
