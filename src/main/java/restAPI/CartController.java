package restAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.FoodCountIsNegativeException;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import exceptions.UnregisteredOrderException;
import models.NormalFood;
import models.Restaurant;
import models.SpecialFood;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systemHandlers.SystemManager;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(value = "/users/{id}/cart")
public class CartController {

    private JsonNodeFactory factory = JsonNodeFactory.instance;

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public ResponseEntity<Object> addToCart(
            @PathVariable(value = "id",required = true) String userId,
            @RequestParam(value = "food",required = true) String foodName,
            @RequestParam(value = "restaurant",required = true) String restaurantId,
            @RequestParam(value = "special",required = true) Boolean specialFood)
    {
        ObjectNode answerJson = factory.objectNode();
        if (restaurantId == null || foodName == null || specialFood == null) {
            answerJson.put("status", 400);
            answerJson.put("description", "bad request");
            return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
        } else {
            try {
                Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
                User user = SystemManager.getInstance().getUser();
                if (specialFood) {
                    SpecialFood food = restaurant.getSpecialFoodByName(foodName);
                    SystemManager.getInstance().addToCart(food, SystemManager.getInstance().getUser());
                    food.setCount(food.getCount() - 1);
                    answerJson.put("status", 200);
                    answerJson.put("food", foodName);
                    answerJson.put("count", food.getCount());
                    return new ResponseEntity<>(answerJson, HttpStatus.OK);
                } else {
                    NormalFood food = restaurant.getNormalFoodByName(foodName);
                    if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                        SystemManager.getInstance().addToCart(food, user);
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
                answerJson.put("status", 404);
                answerJson.put("description", "food does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (RestaurantDoesntExistException e) {
                answerJson.put("status", 404);
                answerJson.put("description", "restaurant does not exist");
                return new ResponseEntity<>(answerJson, HttpStatus.NOT_FOUND);
            } catch (UnregisteredOrderException e) {
                answerJson.put("status", 400);
                answerJson.put("description", "unregistered order");
                return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
            } catch (FoodCountIsNegativeException e) {
                answerJson.put("status", 400);
                answerJson.put("description", "food count negative");
                return new ResponseEntity<>(answerJson, HttpStatus.BAD_REQUEST);
            }
        }
    }


}
