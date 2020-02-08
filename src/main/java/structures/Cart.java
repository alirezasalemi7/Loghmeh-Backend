package structures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private HashMap<String, OrderItem> _orders;
    private String _restaurantName;

    public Cart() {
        this._orders = new HashMap<>();
        this._restaurantName = null;
    }

    public HashMap<String, OrderItem> getOrders() {
        return _orders;
    }

    public void addOrder(String foodName, String restaurantName) throws UnregisteredOrderException {
        if (this._restaurantName == null) {
            this._restaurantName = restaurantName;
            this._orders.put(foodName, new OrderItem(foodName,1));
        } else if (!this._restaurantName.equals(restaurantName)) {
            throw new UnregisteredOrderException("You have some orders from " +  this._restaurantName + "in your cart.");
        } else {
            if(_orders.containsKey(foodName)){
                _orders.get(foodName).setCount(_orders.get(foodName).getCount()+1);
            }
            else _orders.put(foodName, new OrderItem(foodName, 1));
        }
    }

    public String toJson() throws InvalidToJsonException {
        return null;
    }

    public void clearCart() {
        _orders.clear();
        _restaurantName = null;
    }
}
