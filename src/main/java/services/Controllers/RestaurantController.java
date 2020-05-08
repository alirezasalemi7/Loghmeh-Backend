package services.Controllers;


import business.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.DTO.Error.ErrorDTO;
import services.DTO.Restaurant.*;
import business.ServiceManagers.RestaurantManager;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestaurantController {

    @RequestMapping(value = "/restaurants",method = RequestMethod.GET)
    public ResponseEntity<Object> getAllRestaurants(
            HttpServletRequest request,
            @RequestParam(required = true,value = "page_number") int pageNumber,
            @RequestParam(required = true,value = "page_size") int pageSize
    ){
        String userId = (String) request.getAttribute("userId");
        if (userId == null)
            return new ResponseEntity<>(new ErrorDTO("user id has not been passed.", 400), HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(RestaurantManager.getInstance().getInRangeRestaurants(userId,pageNumber,pageSize), HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage(), 404), HttpStatus.OK);
        } catch (ServerInternalException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurants/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> getRestaurant(
            @PathVariable(value = "id") String restaurantId,
            HttpServletRequest request
    ){
        String userId = (String) request.getAttribute("userId");
        if (userId == null)
            return new ResponseEntity<>(new ErrorDTO("user id has not been passed.", 400), HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(RestaurantManager.getInstance().getNearbyRestaurantById(restaurantId, userId), HttpStatus.OK);
        } catch (RestaurantDoesntExistException e) {
            return new ResponseEntity<>(new ErrorDTO("restaurant does not exist", 404), HttpStatus.NOT_FOUND);
        } catch (OutOfRangeException e) {
            return new ResponseEntity<>(new ErrorDTO("restaurant is not in range", 403), HttpStatus.FORBIDDEN);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage(), 404), HttpStatus.NOT_FOUND);
        } catch (ServerInternalException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurants/{id}/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getNormalFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            FoodDTO food = RestaurantManager.getInstance().getFoodById(restaurantId, foodId);
            if (food == null)
                return new ResponseEntity<>(new ErrorDTO("food is not in restaurant menu", 400), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (FoodDoesntExistException e){
            return new ResponseEntity<>(new ErrorDTO("food does not exist", 404), HttpStatus.NOT_FOUND);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurants/{id}/special/{fid}",method = RequestMethod.GET)
    public ResponseEntity<Object> getSpecialFood(
            @PathVariable(value = "id",required = true) String restaurantId,
            @PathVariable(value = "fid",required = true) String foodId
    ){
        try {
            SpecialFoodDTO food = RestaurantManager.getInstance().getSpecialFoodById(restaurantId, foodId);
            if (food == null)
                return new ResponseEntity<>(new ErrorDTO("food is not in restaurant menu", 400), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(food, HttpStatus.OK);
        } catch (FoodDoesntExistException e) {
            return new ResponseEntity<>(new ErrorDTO("food does not exist", 404), HttpStatus.NOT_FOUND);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
