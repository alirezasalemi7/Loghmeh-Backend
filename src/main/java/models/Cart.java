package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.FoodDoesntExistException;
import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private HashMap<String, OrderItem> _orders;
    private String _restaurantId;
    private double _sumOfPrices;

    public Cart() {
        this._orders = new HashMap<>();
        this._restaurantId = null;
        this._sumOfPrices = 0.0;
    }

    public String getRestaurantId() {
        return _restaurantId;
    }
    
    public double getSumOfPrices() {
        return _sumOfPrices;
    }

    public ArrayList<OrderItem> getOrders() {
        return new ArrayList<>(_orders.values());
    }

    public void addOrder(Food food, String restaurantId) throws UnregisteredOrderException {
        String foodName = food.getName();
        if (this._restaurantId == null) {
            if(food instanceof SpecialFood){
                foodName = foodName + "@";
            }
            this._restaurantId = restaurantId;
            this._orders.put(foodName, new OrderItem(food, 1));
            this._sumOfPrices += food.getPrice();
        } else if (!this._restaurantId.equals(restaurantId)) {
            throw new UnregisteredOrderException("You have some orders from " + this._restaurantId + "in your cart.");
        } else {
            if(food instanceof SpecialFood){
                foodName = foodName + "@";
            }
            if (_orders.containsKey(foodName)) {
                _orders.get(foodName).setCount(_orders.get(foodName).getCount() + 1);
            } else {
                _orders.put(foodName, new OrderItem(food, 1));
            }
            _sumOfPrices += food.getPrice();
        }
    }

    public Food removeOrder(Food food) throws FoodDoesntExistException{
        String foodName = food.getName();
        if(food instanceof SpecialFood){
            foodName = foodName + "@";
        }
        if (_orders.containsKey(foodName)) {
            OrderItem orderItem = _orders.get(foodName);
            int count = orderItem.getCount();
            _orders.get(foodName).setCount(count - 1);
            _sumOfPrices -= orderItem.getFood().getPrice();
            if(count <= 1){
                _orders.remove(foodName);
                if (_orders.size() == 0) {
                    this._restaurantId = null;
                }
            }
            return orderItem.getFood();
        }
        else {
            throw new FoodDoesntExistException("food dose not exist in cart.");
        }
    }

    public String toJson() throws InvalidToJsonException {
        ObjectNode root = this.toJsonNode();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(root);
        }
        catch (JsonProcessingException e){
            throw new InvalidToJsonException();
        }
    }

    public ObjectNode toJsonNode() {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectNode root = jsonNodeFactory.objectNode();
        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        for(Map.Entry<String,OrderItem> entry : _orders.entrySet()){
            ObjectNode food = jsonNodeFactory.objectNode();
            if(entry.getKey().contains("@")){
                food.put("special", true);
            }
            else{
                food.put("special", false);
            }
            food.put("restaurant", entry.getValue().getFood().getRestaurantId());
            food.put("food", entry.getValue().getFoodName());
            food.put("count", entry.getValue().getCount());
            food.put("cost", entry.getValue().getPrice());
            arrayNode.add(food);
        }
        root.set("orders", arrayNode);
        root.put("cost", this.getSumOfPrices());
        return root;
    }

    public void clearCart() {
        _orders.clear();
        _restaurantId = null;
        _sumOfPrices = 0;
    }
}
