package systemHandlers;

import exceptions.RestaurantDoesntExistException;
import structures.Location;
import structures.Restaurant;
import structures.User;

import java.util.HashMap;

public class DataHandler {

    private User _user = new User(new Location(0,0));
    private HashMap<String, Restaurant> _restaurants;
    private static DataHandler _instance;

    private DataHandler(){}

    public static DataHandler getInstance(){
        if(_instance==null){
            _instance = new DataHandler();
        }
        return _instance;
    }

    public User getUser(){return _user;}

    public HashMap<String, Restaurant> getAllRestaurant(){ return _restaurants;}

    public Restaurant getRestaurantByName(String name) throws RestaurantDoesntExistException {
        if (!_restaurants.containsKey(name)) {
            throw new RestaurantDoesntExistException("Restaurant" + name + "doesn't exist.");
        }
        return _restaurants.get(name);
    }


}