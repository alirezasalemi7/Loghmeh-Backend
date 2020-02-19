package systemHandlers;

import exceptions.*;

import models.*;
import org.javatuples.Pair;

import java.util.*;

public class SystemManager {

    private static SystemManager _instance;
    private DataHandler _dataHandler;
    private Queue<Order> _notAssignedOrders = new LinkedList<>();

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
        if (_dataHandler.getAllRestaurant().containsKey(restaurant.getId())) {
            throw new RestaurantIsRegisteredException("Restaurant " + restaurant.getId() + " is already registered.");
        } else {
            _dataHandler.getAllRestaurant().put(restaurant.getId(), restaurant);
        }
    }

    public User getUser() {
        return _dataHandler.getUser();
    }


    public void addFood(Food food) throws RestaurantDoesntExistException, FoodIsRegisteredException {
        Restaurant restaurant = _dataHandler.getRestaurantById(food.getRestaurantId());
        restaurant.addFood(food);
    }

    public ArrayList getAllRestaurants() {
        return new ArrayList(_dataHandler.getAllRestaurant().keySet());
    }

    public ArrayList<Restaurant> getInRangeRestaurants(User user) {
        ArrayList<Restaurant> nearbyRestaurants = new ArrayList<>();
        for (HashMap.Entry<String, Restaurant> entry : _dataHandler.getAllRestaurant().entrySet()) {
            if (user.getLocation().getDistance(entry.getValue().getLocation()) <= 170) {
                nearbyRestaurants.add(entry.getValue());
            }
        }
        return nearbyRestaurants;
    }

    public void increaseCredit(User user, Double chargeAmount) throws NegativeChargeAmountException {
        if (chargeAmount <= 0) {
            throw new NegativeChargeAmountException("Your charge amount must be positive.");
        }
        user.setCredit(user.getCredit() + chargeAmount);
    }

    public Restaurant getUserNearbyRestaurants(User user, String restaurantId) throws RestaurantDoesntExistException, OutOfRangeException {
        if (isRestaurantInRange(user, restaurantId)) {
            return _dataHandler.getRestaurantById(restaurantId);
        }
        else throw new OutOfRangeException("The restaurant is not in your region.");
    }

    public Restaurant getRestaurantById(String id) throws RestaurantDoesntExistException {
        return _dataHandler.getRestaurantById(id);
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

    public Food getFood(String restaurantId, String foodName)
            throws RestaurantDoesntExistException, FoodDoesntExistException {
        Restaurant restaurant = _dataHandler.getRestaurantById(restaurantId);
        return restaurant.getFoodByName(foodName);
    }

    public void addToCart(Food food, User user) throws UnregisteredOrderException, RestaurantDoesntExistException, FoodDoesntExistException {
        String restaurantId = food.getRestaurantId();
        String foodName = food.getName();
        if (!_dataHandler.getAllRestaurant().containsKey(restaurantId)) {
            throw new RestaurantDoesntExistException(restaurantId + " doesn't exist in the list of restaurants");
        }
        user.addToCart(_dataHandler.getAllRestaurant().get(restaurantId).getFoodByName(foodName), restaurantId);
    }

    public Order finalizeOrder(User user) throws CartIsEmptyException, CreditIsNotEnoughException {
        ArrayList<OrderItem> items = user.finalizeOrder();
        try {
            Restaurant restaurant = this.getRestaurantById(items.get(0).getFood().getRestaurantId());
            Order order = new Order(items,user,restaurant);
            _notAssignedOrders.add(order);
            DataHandler.getInstance().addOrder(order);
            user.addOrder(order);
            return order;
        }
        catch (RestaurantDoesntExistException e){
            //never reach there
        }
        return null;
    }

    public void assignOrdersToDeliveries(){
        ArrayList<DeliveryMan> deliveryMEN = DataHandler.getInstance().getDeliveries();
        if(deliveryMEN.size()==0){
            return;
        }
        for(Order order : _notAssignedOrders){
            DeliveryMan minDistanceDeliveryMan = null;
            double minTime = 0;
            for (DeliveryMan deliveryMan : deliveryMEN){
                double distance = deliveryMan.getLocation().getDistance(order.getRestaurant().getLocation())+order.getRestaurant().getLocation().getDistance(order.getUser().getLocation());
                double tempTime = distance/deliveryMan.getVelocity();
                if(tempTime<minTime){
                    minDistanceDeliveryMan = deliveryMan;
                }
            }
            order.setDeliveryMan(minDistanceDeliveryMan);
        }
        _notAssignedOrders.clear();
    }

    public Boolean isRestaurantInRange(User user, String restaurantId) throws RestaurantDoesntExistException {
        Restaurant targetRestaurant = _dataHandler.getRestaurantById(restaurantId);
        return (user.getLocation().getDistance(targetRestaurant.getLocation()) <= 170);
    }
}
