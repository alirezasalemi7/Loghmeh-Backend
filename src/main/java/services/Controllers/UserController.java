package services.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import business.exceptions.NegativeChargeAmountException;
import business.exceptions.OrderDoesNotExist;
import business.exceptions.ServerInternalException;
import business.exceptions.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.DTO.Error.ErrorDTO;
import services.DTO.Order.OrderDTO;
import services.DTO.Order.OrderDetailDTO;
import services.DTO.User.UserProfileDTO;
import business.ServiceManagers.UserManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class UserController {
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNodeFactory factory = JsonNodeFactory.instance;

    private ErrorDTO generateError(int status, String description) {
        return new ErrorDTO(description, status);
    }

    @RequestMapping(value = "users/profile", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getProfileInfo(
            HttpServletRequest request
    ) {
        String userId = (String) request.getAttribute("userId");
        try {
            UserProfileDTO profileDTO = UserManager.getInstance().getUserProfile(userId);
            return new ResponseEntity<>(profileDTO, HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<Object> addCredit(
            HttpServletRequest request,
            @RequestBody (required = true) JsonNode node
    ) {
        String userId = (String) request.getAttribute("userId");
        JsonNode increasedValue = node.get("credit");
        if (increasedValue == null)
            return new ResponseEntity<>(generateError(4003, "bad request"), HttpStatus.BAD_REQUEST);
        String amount = increasedValue.toString().replace("\"", "");
        if (!amount.matches("(-)?[0-9]+(\\.[0-9]+)?"))
            return new ResponseEntity<>(generateError(4002, "amount must be a valid number"), HttpStatus.BAD_REQUEST);
        try {
            UserManager.getInstance().increaseCredit(userId, Double.parseDouble(amount));
        } catch (NegativeChargeAmountException e) {
            return new ResponseEntity<>(generateError(4001, "amount must be a positive number"), HttpStatus.BAD_REQUEST);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(generateError(200, "Increased successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/orders", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getAllOrders(
            HttpServletRequest request
    ) {
        String userId = (String) request.getAttribute("userId");
        try {
            ArrayList<OrderDTO> orders = UserManager.getInstance().getAllOrders(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "users/orders/{oid}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getOrder(
            HttpServletRequest request,
            @PathVariable(value = "oid") String orderId
    ) {
        OrderDetailDTO order;
        String userId = (String) request.getAttribute("userId");
        try {
            order = UserManager.getInstance().getOrder(orderId);
        } catch (OrderDoesNotExist orderDoesNotExist) {
            return new ResponseEntity<>(generateError(400, orderDoesNotExist.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
