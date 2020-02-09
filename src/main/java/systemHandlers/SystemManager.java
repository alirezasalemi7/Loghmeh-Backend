package systemHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;

import org.graalvm.compiler.lir.LIRInstruction;
import org.javatuples.Pair;
import structures.Food;
import structures.Restaurant;
import structures.User;

import java.io.IOException;
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

    public User getUser() {
        return _dataHandler.getUser();
    }


    public void addFood(Food food) throws RestaurantDoesntExistException, FoodIsRegisteredException {
        Restaurant restaurant = _dataHandler.getRestaurantByName(food.getRestaurantName());
        restaurant.addFood(food);
    }

    public ArrayList getAllRestaurants() {
        return new ArrayList(_dataHandler.getAllRestaurant().keySet());
    }

    public Restaurant getRestaurantByName(String name) throws RestaurantDoesntExistException {
        return _dataHandler.getRestaurantByName(name);
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

    public void addToCart(String jsonData) throws IOException, UnregisteredOrderException, RestaurantDoesntExistException, FoodDoesntExistException {
        JsonNode node = (new ObjectMapper()).readTree(jsonData);
        String foodName = node.get("foodName").asText().trim();
        String restaurantName = node.get("restaurantName").asText().trim();
        if (!_dataHandler.getAllRestaurant().containsKey(restaurantName)) {
            throw new RestaurantDoesntExistException(restaurantName + "doesn't exist in the list of restaurants");
        } else if (!_dataHandler.getAllRestaurant().get(restaurantName).getFoodByName(foodName).equals(foodName)) {
            throw new FoodDoesntExistException(foodName + "doesn't registered in " + restaurantName);
        }
        _dataHandler.getUser().addToCart(foodName, restaurantName);
    }

    public void getCart() throws InvalidToJsonException{
        System.out.println(_dataHandler.getUser().getCart().toJson());
    }

    public void finalizeOrder() throws InvalidToJsonException{
        System.out.println(_dataHandler.getUser().finalizeOrder());
        System.out.println("order finalized.");
    }
}
