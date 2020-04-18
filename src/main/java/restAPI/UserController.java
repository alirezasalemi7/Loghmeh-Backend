package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.NegativeChargeAmountException;
import exceptions.OrderDoesNotExist;
import exceptions.ServerInternalException;
import exceptions.UserDoesNotExistException;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAPI.DTO.Error.ErrorDTO;
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

    private ErrorDTO generateError(int status, String description) {
        return new ErrorDTO(description, status);
    }

    @RequestMapping(value = "users/{id}/profile", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getProfileInfo(
            @PathVariable(value = "id") String userId
    ) {
        try {
            UserProfileDTO profileDTO = UserServices.getInstance().getUserProfile(userId);
            return new ResponseEntity<>(profileDTO, HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/{id}/profile", method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<Object> addCredit(
            @PathVariable(value = "id") String userId,
            @RequestBody (required = true) JsonNode node
    ) {
        JsonNode increasedValue = node.get("credit");
        if (increasedValue == null)
            return new ResponseEntity<>(generateError(4003, "bad request"), HttpStatus.BAD_REQUEST);
        String amount = increasedValue.toString().replace("\"", "");
        if (!amount.matches("(-)?[0-9]+(\\.[0-9]+)?"))
            return new ResponseEntity<>(generateError(4002, "amount must be a valid number"), HttpStatus.BAD_REQUEST);
        try {
            UserServices.getInstance().increaseCredit(userId, Double.parseDouble(amount));
        } catch (NegativeChargeAmountException e) {
            return new ResponseEntity<>(generateError(4001, "amount must be a positive number"), HttpStatus.BAD_REQUEST);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(generateError(200, "Increased successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}/orders", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getAllOrders(
            @PathVariable(value = "id") String userId
    ) {
        try {
            ArrayList<OrderDTO> orders = UserServices.getInstance().getAllOrders(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "users/{id}/orders/{oid}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getOrder(
            @PathVariable(value = "id") String userId,
            @PathVariable(value = "oid") String orderId
    ) {
        OrderDetailDTO order;
        try {
            order = UserServices.getInstance().getOrder(orderId);
        } catch (OrderDoesNotExist orderDoesNotExist) {
            return new ResponseEntity<>(generateError(400, orderDoesNotExist.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
