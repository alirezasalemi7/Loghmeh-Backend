package structures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private HashMap<String, OrderItem> _orders;
    private String _restaurantId;

    public Cart() {
        this._orders = new HashMap<>();
        this._restaurantId = null;
    }

    public ArrayList<OrderItem> getOrders() {
        return new ArrayList<>(_orders.values());
    }

    public void addOrder(Food food, String restaurantId) throws UnregisteredOrderException {
        String foodName = food.getName();
        if (this._restaurantId == null) {
            this._restaurantId = restaurantId;
            this._orders.put(foodName, new OrderItem(food, 1));
        } else if (!this._restaurantId.equals(restaurantId)) {
            throw new UnregisteredOrderException("You have some orders from " + this._restaurantId + "in your cart.");
        } else {
            if (_orders.containsKey(foodName)) {
                _orders.get(foodName).setCount(_orders.get(foodName).getCount() + 1);
            } else _orders.put(foodName, new OrderItem(food, 1));
        }
    }

    public String toJson() throws InvalidToJsonException {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectNode root = jsonNodeFactory.objectNode();
        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        for(Map.Entry<String,OrderItem> entry : _orders.entrySet()){
            ObjectNode food = jsonNodeFactory.objectNode();
            food.put("foodName", entry.getKey());
            food.put("count", entry.getValue().getCount());
            arrayNode.add(food);
        }
        root.set("foods", arrayNode);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(root);
        }
        catch (JsonProcessingException e){
            throw new InvalidToJsonException();
        }
    }

    public void clearCart() {
        _orders.clear();
        _restaurantId = null;
    }
}
