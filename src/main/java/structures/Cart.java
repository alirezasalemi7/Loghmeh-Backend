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

    }

    public String toJson() throws InvalidToJsonException {
        return null;
    }

    public void clearCart() {
        _orders.clear();
        _restaurantName = null;
    }
}
