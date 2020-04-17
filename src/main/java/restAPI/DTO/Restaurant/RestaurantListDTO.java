package restAPI.DTO.Restaurant;

import java.util.ArrayList;

public class RestaurantListDTO {

    private ArrayList<RestaurantInfoDTO> restaurants;

    public ArrayList<RestaurantInfoDTO> getRestaurants() {
        return restaurants;
    }

    public RestaurantListDTO(ArrayList<RestaurantInfoDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public RestaurantListDTO() {}

    public void setRestaurants(ArrayList<RestaurantInfoDTO> restaurants) {
        this.restaurants = restaurants;
    }
}