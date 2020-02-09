package systemHandlers;

import exceptions.FoodDoesntExistException;
import exceptions.FoodIsRegisteredException;
import exceptions.RestaurantDoesntExistException;
import exceptions.RestaurantIsRegisteredException;

import org.javatuples.Pair;
import structures.Food;
import structures.Restaurant;
import structures.User;

import java.util.*;

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

    public Food getFood(String restaurantName, String foodName)
            throws RestaurantDoesntExistException, FoodDoesntExistException {
        Restaurant restaurant = _dataHandler.getRestaurantByName(restaurantName);
        return restaurant.getFoodByName(foodName);
    }

    public ArrayList<Restaurant> getRecommendedRestaurants(User user){
        PriorityQueue<Pair<Double,Restaurant>> maxHeap = new PriorityQueue<>(new Comparator<Pair<Double, Restaurant>>() {
            @Override
            public int compare(Pair<Double, Restaurant> doubleRestaurantPair, Pair<Double, Restaurant> t1) {
                if(doubleRestaurantPair.getValue0() > t1.getValue0()){
                    return 1;
                }
                else if(doubleRestaurantPair.getValue0() < t1.getValue0()){
                    return -1;
                }
                return 0;
            }
        });
        for (Map.Entry<String,Restaurant> entry : _dataHandler.getAllRestaurant().entrySet()){
            double rate = entry.getValue().getAveragePopularity()*user.getLocation().getDistance(entry.getValue().getLocation());
            Pair<Double,Restaurant> pair = new Pair<>(rate,entry.getValue());
            maxHeap.add(pair);
        }
        ArrayList<Restaurant> recommended = new ArrayList<>();
        for(int i=0;i<3;i++){
            if(maxHeap.size()>0){
                recommended.add(maxHeap.remove().getValue1());
            }
        }
        return recommended;
    }
}
