package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.NegativeChargeAmountException;
import exceptions.OrderDoesNotExist;
import exceptions.RestaurantDoesntExistException;
import models.Food;
import models.Order;
import models.OrderItem;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class UserController {
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNodeFactory factory = JsonNodeFactory.instance;

    private ObjectNode generateError(JsonNodeFactory factory, int status, String description) {
        ObjectNode errorNode = factory.objectNode();
        errorNode.put("status", status);
        errorNode.put("description", description);
        return errorNode;
    }

    @RequestMapping(value = "/users/{id}/profile/addCredit", method = RequestMethod.POST)
    public ResponseEntity<Object> addCredit(
            @RequestBody (required = true) JsonNode node
    ) {
        JsonNode increasedValue = node.get("credit");
        if (increasedValue == null)
            return new ResponseEntity<>(generateError(factory, 400, "bad request"), HttpStatus.BAD_REQUEST);
        String amount = increasedValue.toString();
        if (!amount.matches("(-)?[0-9]+(\\.[0-9]+)?"))
            return new ResponseEntity<>(generateError(factory, 400, "amount must be a valid number"), HttpStatus.BAD_REQUEST);
        try {
            SystemManager.getInstance().increaseCredit(SystemManager.getInstance().getUser(), Double.parseDouble(amount));
        } catch (NegativeChargeAmountException e) {
            return new ResponseEntity<>(generateError(factory, 400, "amount must be a positive number"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(generateError(factory, 200, "Increased successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}/orders/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllOrders(
            @PathVariable(value = "id") String userId
    ) {
        ArrayNode response = factory.arrayNode();
        ArrayList<Order> orders = SystemManager.getInstance().getUser().getOrders();
        for (Order order : orders) {
            ObjectNode node = factory.objectNode();
            node.put("orderStatus", order.getState().toString());
            node.put("restaurantName", order.getRestaurant().getName());
            response.add(node);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("users/{id}/orders/{oid}")
    public ResponseEntity<Object> getOrder(
            @PathVariable(value = "id") String userId,
            @PathVariable(value = "oid") String orderId
    ) {
        Order order;
        try {
            order = SystemManager.getInstance().getUser().getOrderById(orderId);
        } catch (OrderDoesNotExist orderDoesNotExist) {
            return new ResponseEntity<>(generateError(factory, 400, orderDoesNotExist.getMessage()), HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(mapper.readTree(order.toJson()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(generateError(factory, 500, "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
