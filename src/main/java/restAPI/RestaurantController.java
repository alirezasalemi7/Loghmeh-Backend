package restAPI;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.FoodDoesntExistException;
import exceptions.InvalidToJsonException;
import exceptions.RestaurantDoesntExistException;
import models.Food;
import models.NormalFood;
import models.Restaurant;
import models.SpecialFood;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/restaurants/")
public class RestaurantController {

    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllRestaurants(){
        ArrayNode answerJson = factory.arrayNode();
        ArrayList<Restaurant> restaurants = SystemManager.getInstance().getInRangeRestaurants(DataHandler.getInstance().getUser());
        for (Restaurant restaurant : restaurants){
            ObjectNode node = factory.objectNode();
            node.put("name", restaurant.getName());
            node.put("img", restaurant.getLogoAddress());
            node.put("id", restaurant.getId());
            answerJson.add(node);
        }
        return new ResponseEntity<>(answerJson, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> getRestaurant(
            @PathVariable(value = "id") String restaurantId
    ){
        try {
            Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
            if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                JsonNode answerJson = mapper.readTree(restaurant.toJson());
                return new ResponseEntity<>(answerJson, HttpStatus.OK);
            } else {
                ObjectNode answerJson = factory.objectNode();
                answerJson.put("status", 403);
                answerJson.put("description", "restaurant not in range");
                return new ResponseEntity<>(answerJson, HttpStatus.FORBIDDEN);
            }
        } catch (RestaurantDoesntExistException e) {
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 404);
            answerJson.put("description", "restaurant does not exist");
            return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
        }
        catch (InvalidToJsonException| IOException e){
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 500);
            answerJson.put("description", "internal server error");
            return new ResponseEntity<>(answerJson,HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }

    @RequestMapping(value = "/{id}/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getNormalFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
            if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                NormalFood food = restaurant.getNormalFoodByName(foodId);
                JsonNode answerJson = mapper.readTree(food.toJson());
                return new ResponseEntity<>(answerJson, HttpStatus.OK);
            } else {
                ObjectNode answerJson = factory.objectNode();
                answerJson.put("status", 403);
                answerJson.put("description", "restaurant not in range");
                return new ResponseEntity<>(answerJson, HttpStatus.FORBIDDEN);
            }
        } catch (RestaurantDoesntExistException e) {
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 404);
            answerJson.put("description", "restaurant does not exist");
            return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
        }
        catch (FoodDoesntExistException e){
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 404);
            answerJson.put("description", "food does not exist");
            return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
        }
        catch (InvalidToJsonException| IOException e){
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 500);
            answerJson.put("description", "internal server error");
            return new ResponseEntity<>(answerJson,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/special/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getSpecialFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
            if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                SpecialFood food = restaurant.getSpecialFoodByName(foodId);
                JsonNode answerJson = mapper.readTree(food.toJson());
                return new ResponseEntity<>(answerJson, HttpStatus.OK);
            } else {
                ObjectNode answerJson = factory.objectNode();
                answerJson.put("status", 403);
                answerJson.put("description", "restaurant not in range");
                return new ResponseEntity<>(answerJson, HttpStatus.FORBIDDEN);
            }
        } catch (RestaurantDoesntExistException e) {
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 404);
            answerJson.put("description", "restaurant does not exist");
            return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
        }
        catch (FoodDoesntExistException e){
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 404);
            answerJson.put("description", "food does not exist");
            return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
        }
        catch (InvalidToJsonException| IOException e){
            ObjectNode answerJson = factory.objectNode();
            answerJson.put("status", 500);
            answerJson.put("description", "internal server error");
            return new ResponseEntity<>(answerJson,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
