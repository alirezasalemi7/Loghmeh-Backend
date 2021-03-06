package services.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import business.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.DTO.Cart.CartDTO;
import services.DTO.Error.ErrorDTO;
import services.DTO.HandShakes.CartSuccessFulFinalize;
import services.DTO.HandShakes.ChangeInCartSuccess;
import services.DTO.Order.OrderDetailDTO;
import business.ServiceManagers.UserManager;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/users")
public class CartController {

    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/cart",method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<Object> addToCart(
            HttpServletRequest request,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        JsonNode numberOfOrders = payload.get("food_count");
        String userId = (String) request.getAttribute("userId");
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
                int newCount = UserManager.getInstance().addToCart(userId, foodName, restaurantId, specialFood, count);
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
            HttpServletRequest request,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        String userId = (String) request.getAttribute("userId");
        if (restaurantIdJson == null || foodNameJson == null || specialFoodJson == null) {
            return new ResponseEntity<>(new ErrorDTO("bad request",40002), HttpStatus.BAD_REQUEST);
        }
        try {
            String foodName = foodNameJson.asText();
            String restaurantId = restaurantIdJson.asText();
            boolean specialFood = specialFoodJson.asBoolean();
            int count = UserManager.getInstance().removeFromCart(userId, foodName, restaurantId, specialFood);
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
            HttpServletRequest request)
    {
        String userId = (String) request.getAttribute("userId");
        try {
            OrderDetailDTO order = UserManager.getInstance().finalizeCart(userId);
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
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/cart",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> getCart(
            HttpServletRequest request)
    {
        String userId = (String) request.getAttribute("userId");
        try {
            CartDTO cart = UserManager.getInstance().getUserCart(userId);
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
