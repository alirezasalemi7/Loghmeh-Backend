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
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/users/{id}/cart")
public class CartController {

    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public ResponseEntity<Object> addToCart(
            @PathVariable(value = "id",required = true) String userId,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        JsonNode numberOfOrders = payload.get("food_count");
        ObjectNode answerJson = factory.objectNode();
        if (restaurantIdJson == null || foodNameJson == null || specialFoodJson == null) {
            answerJson.put("status", 40002);
            answerJson.put("description", "bad request");
            return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
        } else {
            int count = 1;
            if(numberOfOrders!=null){
                count = numberOfOrders.asInt();
            }
            String foodName = foodNameJson.asText();
            String restaurantId = restaurantIdJson.asText();
            Boolean specialFood = specialFoodJson.asBoolean();
            try {
                Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
                if (specialFood) {
                    SpecialFood food = restaurant.getSpecialFoodByName(foodName);
                    food.setCount(food.getCount() - count);
                    for(int i=0;i<count;i++){
                        SystemManager.getInstance().addToCart(food, SystemManager.getInstance().getUser());
                    }
                    answerJson.put("status", 200);
                    answerJson.put("food", foodName);
                    answerJson.put("count", food.getCount());
                    return new ResponseEntity<>(answerJson, HttpStatus.OK);
                } else {
                    NormalFood food = restaurant.getNormalFoodByName(foodName);
                    if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                        for(int i=0;i<count;i++){
                            SystemManager.getInstance().addToCart(food, SystemManager.getInstance().getUser());
                        }
                        answerJson.put("status", 200);
                        answerJson.put("food", foodName);
                        answerJson.put("count", "inf");
                        return new ResponseEntity<>(answerJson, HttpStatus.OK);
                    } else {
                        answerJson.put("status", 403);
                        answerJson.put("description", "restaurant not in range");
                        return new ResponseEntity<>(answerJson, HttpStatus.FORBIDDEN);
                    }
                }
            } catch (FoodDoesntExistException e) {
                answerJson.put("status", 40401);
                answerJson.put("description", "food does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (RestaurantDoesntExistException e) {
                answerJson.put("status", 40402);
                answerJson.put("description", "restaurant does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (UnregisteredOrderException e) {
                answerJson.put("status", 40004);
                answerJson.put("description", "unregistered order");
                return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
            } catch (FoodCountIsNegativeException e) {
                answerJson.put("status", 40001);
                answerJson.put("description", "food count negative");
                return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value="/remove",method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeFromCart(
            @PathVariable(value = "id",required = true) String userId,
            @RequestBody (required = true) JsonNode payload)
    {
        JsonNode foodNameJson = payload.get("food");
        JsonNode restaurantIdJson = payload.get("restaurant");
        JsonNode specialFoodJson = payload.get("special");
        ObjectNode answerJson = factory.objectNode();
        if (restaurantIdJson == null || foodNameJson == null || specialFoodJson == null) {
            answerJson.put("status", 40002);
            answerJson.put("description", "bad request");
            return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
        } else {
            try {
                String foodName = foodNameJson.asText();
                String restaurantId = restaurantIdJson.asText();
                Boolean specialFood = specialFoodJson.asBoolean();
                Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
                User user = SystemManager.getInstance().getUser();
                if (specialFood) {
                    SpecialFood food = restaurant.getSpecialFoodByName(foodName);
                    user.getCart().removeOrder(food);
                    food.setCount(food.getCount() - 1);
                    answerJson.put("status", 200);
                    answerJson.put("food", foodName);
                    answerJson.put("count", food.getCount());
                    return new ResponseEntity<>(answerJson, HttpStatus.OK);
                } else {
                    NormalFood food = restaurant.getNormalFoodByName(foodName);
                    if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                        user.getCart().removeOrder(food);
                        answerJson.put("status", 200);
                        answerJson.put("food", foodName);
                        answerJson.put("count", "inf");
                        return new ResponseEntity<>(answerJson, HttpStatus.OK);
                    } else {
                        answerJson.put("status", 403);
                        answerJson.put("description", "restaurant not in range");
                        return new ResponseEntity<>(answerJson, HttpStatus.FORBIDDEN);
                    }
                }
            } catch (FoodDoesntExistException e) {
                answerJson.put("status", 40401);
                answerJson.put("description", "food does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (RestaurantDoesntExistException e) {
                answerJson.put("status", 40402);
                answerJson.put("description", "restaurant does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (FoodCountIsNegativeException e) {
                answerJson.put("status", 40001);
                answerJson.put("description", "food count negative");
                return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value="/finalize",method = RequestMethod.POST)
    public ResponseEntity<Object> finalize(
            @PathVariable(value = "id",required = true) String userId)
    {
        ObjectNode answerJson = factory.objectNode();
        User user = DataHandler.getInstance().getUser();
        try {
            Order order = SystemManager.getInstance().finalizeOrder(user);
            answerJson.set("order", order.toJsonNode());
            answerJson.put("status", 200);
            return new ResponseEntity<>(answerJson,HttpStatus.OK);
        }
        catch (CartIsEmptyException e){
            answerJson.put("status", 40001);
            answerJson.put("description", "cart is empty");
            return new ResponseEntity<>(answerJson,HttpStatus.BAD_REQUEST);
        }
        catch (CreditIsNotEnoughException e){
            answerJson.put("status", 40002);
            answerJson.put("description", "credit not enough");
            return new ResponseEntity<>(answerJson,HttpStatus.BAD_REQUEST);
        }
//        catch (IOException e){
//            answerJson.put("status", 500);
//            answerJson.put("description", "internal server error");
//            return new ResponseEntity<>(answerJson,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    @RequestMapping(value="/view",method = RequestMethod.GET)
    public ResponseEntity<Object> getCart(
            @PathVariable(value = "id",required = true) String userId)
    {
        User user = DataHandler.getInstance().getUser();
//        try {
        JsonNode answerJson = user.getCart().toJsonNode();
        return new ResponseEntity<>(answerJson,HttpStatus.OK);
//        }
//        catch (IOException | InvalidToJsonException e){
//            ObjectNode answerJson = factory.objectNode();
//            answerJson.put("status", 500);
//            answerJson.put("description", "internal server error");
//            return new ResponseEntity<>(answerJson,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

}
