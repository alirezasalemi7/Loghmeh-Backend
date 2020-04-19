package systemHandlers;

import exceptions.OrderDoesNotExist;
import exceptions.RestaurantDoesntExistException;
import models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DataHandler {

    private User _user;
    private HashMap<String, Restaurant> _restaurants;
    private HashMap<String, Order> _orders;
    private static DataHandler _instance;

    private DataHandler(){
        _restaurants = new HashMap<>();
        _orders = new HashMap<>();
//        _user = new User(new Location(0, 0), "خرچال", "شاه",  "09196055428", "kharchal@gmail.com", 1000.0);
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

    public void addOrder(Order order){
        _orders.put(String.valueOf(order.getId()), order);
    }

    public Order getOrder(String id) throws OrderDoesNotExist{
        if(_orders.containsKey(id)){
            return _orders.get(id);
        }
        throw new OrderDoesNotExist();
    }
}