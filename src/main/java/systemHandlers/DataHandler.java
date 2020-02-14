package systemHandlers;

import exceptions.RestaurantDoesntExistException;
import models.Location;
import models.Restaurant;
import models.User;

import java.util.HashMap;

public class DataHandler {

    private User _user;
    private HashMap<String, Restaurant> _restaurants;
    private static DataHandler _instance;

    private DataHandler(){
        _restaurants = new HashMap<>();
        _user = new User(new Location(0, 0), "ali", "mammadi",  "09196055428", "am@gmail.com", 1000.0);
    }

    public static DataHandler getInstance() {
        if(_instance==null){
            _instance = new DataHandler();
        }
        return _instance;
    }

    public User getUser(){return _user;}

    public HashMap<String, Restaurant> getAllRestaurant(){ return _restaurants;}

    public Restaurant getRestaurantById(String id) throws RestaurantDoesntExistException {
        if (!_restaurants.containsKey(id)) {
            throw new RestaurantDoesntExistException("Restaurant" + id + "doesn't exist.");
        }
        return _restaurants.get(id);
    }
}