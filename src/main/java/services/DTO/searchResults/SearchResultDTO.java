package services.DTO.searchResults;

import services.DTO.Restaurant.FoodDTO;
import services.DTO.Restaurant.RestaurantInfoDTO;

import java.util.ArrayList;

public class SearchResultDTO {

    private ArrayList<RestaurantInfoDTO> restaurants;
    private ArrayList<FoodDTO> foods;

    public SearchResultDTO() {}

    public SearchResultDTO(ArrayList<RestaurantInfoDTO> restaurants, ArrayList<FoodDTO> foods) {
        this.restaurants = restaurants;
        this.foods = foods;
    }

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
