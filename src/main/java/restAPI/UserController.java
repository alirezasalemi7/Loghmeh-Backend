package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.NegativeChargeAmountException;
import exceptions.OrderDoesNotExist;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAPI.DTO.Order.OrderDTO;
import restAPI.DTO.Order.OrderDetailDTO;
import restAPI.DTO.User.UserProfileDTO;
import systemHandlers.Services.UserServices;
import systemHandlers.SystemManager;

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

    @RequestMapping(value = "users/{id}/profile", method = RequestMethod.GET)
    public ResponseEntity<Object> getProfileInfo(
            @PathVariable(value = "id") String userId
    ) {
        User user = SystemManager.getInstance().getUser();
        UserProfileDTO profileDTO = UserServices.getInstance().getUserProfile(userId);
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}/profile", method = RequestMethod.PUT)
    public ResponseEntity<Object> addCredit(
            @PathVariable(value = "id") String userId,
            @RequestBody (required = true) JsonNode node
    ) {
        JsonNode increasedValue = node.get("credit");
        if (increasedValue == null)
            return new ResponseEntity<>(generateError(factory, 4003, "bad request"), HttpStatus.BAD_REQUEST);
        String amount = increasedValue.toString().replace("\"", "");
        if (!amount.matches("(-)?[0-9]+(\\.[0-9]+)?"))
            return new ResponseEntity<>(generateError(factory, 4002, "amount must be a valid number"), HttpStatus.BAD_REQUEST);
        try {
            UserServices.getInstance().increaseCredit(userId, Double.parseDouble(amount));
        } catch (NegativeChargeAmountException e) {
            return new ResponseEntity<>(generateError(factory, 4001, "amount must be a positive number"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(generateError(factory, 200, "Increased successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}/orders", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllOrders(
            @PathVariable(value = "id") String userId
    ) {
        ArrayNode response = factory.arrayNode();
        ArrayList<OrderDTO> orders = UserServices.getInstance().getAllOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}/orders/{oid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOrder(
            @PathVariable(value = "id") String userId,
            @PathVariable(value = "oid") String orderId
    ) {
        OrderDetailDTO order;
        try {
            order = UserServices.getInstance().getOrder(orderId);
        } catch (OrderDoesNotExist orderDoesNotExist) {
            return new ResponseEntity<>(generateError(factory, 400, orderDoesNotExist.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
