package services.Controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import business.exceptions.FoodDoesntExistException;
import business.exceptions.ServerInternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.DTO.Error.ErrorDTO;
import services.DTO.Restaurant.SpecialFoodDTO;
import business.ServiceManagers.RestaurantManager;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class FoodPartyController {

    private final JsonNodeFactory factory = JsonNodeFactory.instance;

    @RequestMapping(value = "/foodParty",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFoods() {
        ArrayList<SpecialFoodDTO> foods = null;
        try {
            foods = RestaurantManager.getInstance().getAllSpecialFoods();
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @RequestMapping(value = "/foodParty/time", method = RequestMethod.GET)
    public ResponseEntity<Object> getFoodPartyTime() {
        ObjectNode node = factory.objectNode();
        Date start = RestaurantManager.getInstance().getFoodPartyStartTime();
        Date current = new Date();
        long diff = current.getTime() - start.getTime();
        diff = 180000 - diff;
        node.put("minutes", (int) (diff / (60 * 1000)));
        node.put("seconds", (int) ((diff / (1000)) % 60));
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @RequestMapping(value = "/foodParty/{fid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getSpecialFood(
            @PathVariable(value = "fid") String foodId,
            @RequestParam(required = true, value = "restaurant_id") String restaurantId
    ) {
        if (restaurantId == null)
            return new ResponseEntity<>(new ErrorDTO("restaurant doesn't exist", 400), HttpStatus.NOT_FOUND);
        try {
            SpecialFoodDTO food = RestaurantManager.getInstance().getSpecialFoodById(restaurantId, foodId);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (FoodDoesntExistException e) {
            return new ResponseEntity<>(new ErrorDTO("Food doesn't exist", 404), HttpStatus.NOT_FOUND);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
