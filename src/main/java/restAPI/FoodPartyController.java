package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.FoodDoesntExistException;
import exceptions.InvalidToJsonException;
import models.Restaurant;
import models.SpecialFood;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import systemHandlers.DataHandler;

import java.io.IOException;

@RestController
@RequestMapping(value = "/foodParty/")
public class FoodPartyController {
    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFoods() {
        ArrayNode nodes = factory.arrayNode();
        for (Restaurant restaurant : DataHandler.getInstance().getAllRestaurant().values()) {
            for (SpecialFood food : restaurant.getSpecialMenu().values()) {
                ObjectNode  node = null;
                try {
                    node = (ObjectNode) food.toJsonNode();
                    node.put("restaurantName", restaurant.getName());
                    node.put("restaurantId", restaurant.getId());
                } catch (InvalidToJsonException e) {
                    ObjectNode errorNode = factory.objectNode();
                    errorNode.put("status", "500");
                    errorNode.put("description", "internal server error");
                    return new ResponseEntity<>(errorNode, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                nodes.add(node);
            }
        }
        ObjectNode finalNode = factory.objectNode();
        finalNode.put("foods", nodes);
        finalNode.put("minutes", 3);
        finalNode.put("seconds", 0);
        return new ResponseEntity<>(finalNode, HttpStatus.OK);
    }

    @RequestMapping(value = "/{fid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getSpecialFood(
            @PathVariable(value = "fid") String foodId
    ) {
        SpecialFood food;
        for (Restaurant restaurant : DataHandler.getInstance().getAllRestaurant().values()) {
            try {
                food = restaurant.getSpecialFoodByName(foodId);
            } catch (FoodDoesntExistException e) {
                continue;
            }
            try {
                ObjectNode node = (ObjectNode) food.toJsonNode();
                node.put("restaurantName", restaurant.getName());
                node.put("restaurantId", restaurant.getId());
                return new ResponseEntity<>(node, HttpStatus.OK);
            } catch (InvalidToJsonException e) {
                ObjectNode errorNode = factory.objectNode();
                errorNode.put("status", 500);
                errorNode.put("description", "internal server error");
             }
        }
        ObjectNode errorNode = factory.objectNode();
        errorNode.put("status", 404);
        errorNode.put("description", "Food doesn't exist");
        return new ResponseEntity<>(errorNode, HttpStatus.NOT_FOUND);
    }
}
