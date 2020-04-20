package restAPI.DTO.searchResults;

import restAPI.DTO.Restaurant.FoodDTO;
import restAPI.DTO.Restaurant.RestaurantDTO;
import restAPI.DTO.Restaurant.RestaurantInfoDTO;

import java.util.ArrayList;

public class SearchResultDTO {

    private ArrayList<RestaurantInfoDTO> restaurants;
    private ArrayList<FoodDTO> foods;

    public ArrayList<RestaurantInfoDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<RestaurantInfoDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public ArrayList<FoodDTO> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<FoodDTO> foods) {
        this.foods = foods;
    }
}
