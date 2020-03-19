package restAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.InvalidToJsonException;
import models.Restaurant;
import models.SpecialFood;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import javax.swing.text.html.ObjectView;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/foodParty/")
public class FoodPartyController {
    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllSpecialRestaurants() {
        ArrayNode nodes = factory.arrayNode();
        for (Restaurant restaurant : DataHandler.getInstance().getAllRestaurant().values()) {
            for (SpecialFood food : restaurant.getSpecialMenu().values()) {
                JsonNode node = null;
                try {
                    node = mapper.readTree(food.toJson());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidToJsonException e) {
                    ObjectNode errorNode = factory.objectNode();
                    errorNode.put("status", "500");
                    errorNode.put("description", "internal server error");
                    return new ResponseEntity<>(errorNode, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                nodes.add(node);
            }
        }
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

}
