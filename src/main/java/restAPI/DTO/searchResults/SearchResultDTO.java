package restAPI.DTO.searchResults;

import restAPI.DTO.Restaurant.FoodDTO;
import restAPI.DTO.Restaurant.RestaurantDTO;
import restAPI.DTO.Restaurant.RestaurantInfoDTO;

import java.util.ArrayList;

public class SearchResultDTO {

    private ArrayList<RestaurantInfoDTO> restaurants;
    private ArrayList<FoodDTO> foods;
    private int pageCount;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
