package restAPI;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.FoodDoesntExistException;
import exceptions.OutOfRangeException;
import exceptions.RestaurantDoesntExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAPI.DTO.Restaurant.FoodDTO;
import restAPI.DTO.Restaurant.RestaurantDTO;
import restAPI.DTO.Restaurant.RestaurantListDTO;
import restAPI.DTO.Restaurant.SpecialFoodDTO;
import systemHandlers.Services.RestaurantManager;

@RestController
public class RestaurantController {

    private final JsonNodeFactory factory = JsonNodeFactory.instance;

    private ObjectNode generateError(JsonNodeFactory factory, int status, String description) {
        ObjectNode errorNode = factory.objectNode();
        errorNode.put("status", status);
        errorNode.put("description", description);
        return errorNode;
    }

    @RequestMapping(value = "/restaurants",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllRestaurants(
            @RequestBody(required = true) JsonNode user
    ){
        JsonNode userId = user.get("id");
        if (userId == null)
            return new ResponseEntity<>(generateError(factory, 400, "user id has not been passed."), HttpStatus.BAD_REQUEST);
        RestaurantListDTO restaurantList = RestaurantManager.getInstance().getInRangeRestaurants(userId.asText());
        return new ResponseEntity<>(restaurantList, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurants/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> getRestaurant(
            @PathVariable(value = "id") String restaurantId,
            @RequestBody(required = true) JsonNode user
    ){
        JsonNode userId = user.get("id");
        if (userId == null)
            return new ResponseEntity<>(generateError(factory, 400, "user id has not been passed."), HttpStatus.BAD_REQUEST);
        try {
            RestaurantDTO restaurant = RestaurantManager.getInstance().getNearbyRestaurantById(restaurantId, userId.asText());
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } catch (RestaurantDoesntExistException e) {
            return new ResponseEntity<>(generateError(factory, 404, "restaurant does not exist"), HttpStatus.NOT_FOUND);
        } catch (OutOfRangeException e) {
            return new ResponseEntity<>(generateError(factory, 403, "restaurant is not in range"), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/restaurants/{id}/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getNormalFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            FoodDTO food = RestaurantManager.getInstance().getRestaurantFoodById(restaurantId, foodId);
            if (food == null)
                return new ResponseEntity<>(generateError(factory, 400, "food is not in restaurant menu"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (FoodDoesntExistException e){
            return new ResponseEntity<>(generateError(factory, 404, "food does not exist"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restaurants/{id}/special/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getSpecialFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            SpecialFoodDTO food = RestaurantManager.getInstance().getRestaurantSpecialFoodById(restaurantId, foodId);
            if (food == null)
                return new ResponseEntity<>(generateError(factory, 400, "food is not in restaurant menu"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (FoodDoesntExistException e) {
            return new ResponseEntity<>(generateError(factory, 403, "food does not exist"), HttpStatus.NOT_FOUND);
        }
    }

}
