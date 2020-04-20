package restAPI;

import exceptions.ServerInternalException;
import exceptions.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restAPI.DTO.Error.ErrorDTO;
import restAPI.DTO.searchResults.SearchResultDTO;
import systemHandlers.Services.RestaurantManager;

@RestController
public class SearchController {

    @RequestMapping(value = "search/restaurants", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> searchByRestaurantName(
            @RequestParam(required = true,value = "user_id") String userId,
            @RequestParam(required = true,value = "restaurant_name") String restaurantName,
            @RequestParam(required = true,value = "page_number") int pageNumber,
            @RequestParam(required = true,value = "page_size") int pageSize
    ){
        try {
            SearchResultDTO result = RestaurantManager.getInstance().findRestaurantsByNameMatch(userId, restaurantName, pageNumber, pageSize);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001), HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "search/foods", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> searchByFoodName(
            @RequestParam(required = true,value = "user_id") String userId,
            @RequestParam(required = true,value = "food_name") String foodName,
            @RequestParam(required = true,value = "page_number") int pageNumber,
            @RequestParam(required = true,value = "page_size") int pageSize
    ){
        try {
            SearchResultDTO result = RestaurantManager.getInstance().findFoodsByNameMatch(userId, foodName, pageNumber, pageSize);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001), HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "search/foods_and_restaurants", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<Object> searchByFoodNameAndRestaurantName(
            @RequestParam(required = true,value = "user_id") String userId,
            @RequestParam(required = true,value = "food_name") String foodName,
            @RequestParam(required = true,value = "restaurant_name") String restaurantName,
            @RequestParam(required = true,value = "page_number") int pageNumber,
            @RequestParam(required = true,value = "page_size") int pageSize
    ){
        try {
            SearchResultDTO result = RestaurantManager.getInstance().findFoodsByNameAndRestaurantNameMatch(userId, foodName, restaurantName, pageNumber, pageSize);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        catch (UserDoesNotExistException e){
            return new ResponseEntity<>(new ErrorDTO("user not found", 4040001), HttpStatus.NOT_FOUND);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
