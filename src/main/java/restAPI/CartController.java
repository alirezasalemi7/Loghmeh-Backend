package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.*;
import models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAPI.DTO.Cart.CartDTO;
import restAPI.DTO.Error.ErrorDTO;
import restAPI.DTO.HandShakes.CartSuccessFulFinalize;
import restAPI.DTO.HandShakes.ChangeInCartSuccess;
import restAPI.DTO.Order.OrderDetailDTO;
import systemHandlers.DataHandler;
import systemHandlers.Services.UserServices;
import systemHandlers.SystemManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/users/{id}")
public class CartController {

    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/cart",method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<Object> addToCart(
            @PathVariable(value = "id",required = true) String userId,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        JsonNode numberOfOrders = payload.get("food_count");
        if (restaurantIdJson == null || foodNameJson == null || specialFoodJson == null) {
            return new ResponseEntity<>(new ErrorDTO("bad request",40002), HttpStatus.BAD_REQUEST);
        } else {
            int count = 1;
            if(numberOfOrders!=null){
                count = numberOfOrders.asInt();
            }
            try {
                String foodName = foodNameJson.asText();
                String restaurantId = restaurantIdJson.asText();
                Boolean specialFood = specialFoodJson.asBoolean();
                int newCount = UserServices.getInstance().addToCart(userId, foodName, restaurantId, specialFood, count);
                return new ResponseEntity<>(new ChangeInCartSuccess(200, foodName, newCount), HttpStatus.OK);
            } catch (FoodDoesntExistException e) {
                return new ResponseEntity<>(new ErrorDTO("food does not exist",40401), HttpStatus.NOT_FOUND);
            } catch (RestaurantDoesntExistException e) {
                return new ResponseEntity<>(new ErrorDTO("restaurant does not exist",40402), HttpStatus.NOT_FOUND);
            } catch (UnregisteredOrderException e) {
                return new ResponseEntity<>(new ErrorDTO("unregistered order",40004), HttpStatus.BAD_REQUEST);
            } catch (FoodCountIsNegativeException e) {
                return new ResponseEntity<>(new ErrorDTO("food count negative",40001), HttpStatus.BAD_REQUEST);
            } catch (UserDoesNotExistException e){
                return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
            } catch (RestaurantOutOfRangeException e){
                return new ResponseEntity<>(new ErrorDTO("restaurant not in range",403), HttpStatus.FORBIDDEN);
            } catch (ServerInternalException e){
                return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(value="/cart",method = RequestMethod.DELETE,produces = "application/json")
    public ResponseEntity<Object> removeFromCart(
            @PathVariable(value = "id",required = true) String userId,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        ObjectNode answerJson = factory.objectNode();
        if (restaurantIdJson == null || foodNameJson == null || specialFoodJson == null) {
            return new ResponseEntity<>(new ErrorDTO("bad request",40002), HttpStatus.BAD_REQUEST);
        }
        try {
            String foodName = foodNameJson.asText();
            String restaurantId = restaurantIdJson.asText();
            boolean specialFood = specialFoodJson.asBoolean();
            int count = UserServices.getInstance().removeFromCart(userId, foodName, restaurantId, specialFood);
            return new ResponseEntity<>(new ChangeInCartSuccess(200, foodName, count), HttpStatus.OK);
        } catch (FoodDoesntExistException e) {
            return new ResponseEntity<>(new ErrorDTO("food does not exist",40401), HttpStatus.NOT_FOUND);
        } catch (RestaurantDoesntExistException e) {
            return new ResponseEntity<>(new ErrorDTO("restaurant does not exist",40402), HttpStatus.NOT_FOUND);
        }catch (RestaurantOutOfRangeException e){
            return new ResponseEntity<>(new ErrorDTO("restaurant not in range",403), HttpStatus.FORBIDDEN);
        }catch (FoodNotExistInCartException e){
            return new ResponseEntity<>(new ErrorDTO("food does not exist in cart",40401), HttpStatus.FORBIDDEN);
        }catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/cart",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<Object> finalize(
            @PathVariable(value = "id",required = true) String userId)
    {
        try {
            OrderDetailDTO order = UserServices.getInstance().finalizeCart(userId);
            return new ResponseEntity<>(new CartSuccessFulFinalize(200,order),HttpStatus.OK);
        }
        catch (CartIsEmptyException e){
            return new ResponseEntity<>(new ErrorDTO("cart is empty",40001),HttpStatus.BAD_REQUEST);
        }
        catch (CreditIsNotEnoughException e){
            return new ResponseEntity<>(new ErrorDTO("credit not enough",40002),HttpStatus.BAD_REQUEST);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/cart",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getCart(
            @PathVariable(value = "id",required = true) String userId)
    {
        try {
            CartDTO cart = UserServices.getInstance().getUserCart(userId);
            return new ResponseEntity<>(cart,HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001),HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
