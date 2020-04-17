package restAPI.DTO.Restaurant;

import java.util.ArrayList;

public class RestaurantDTO {

    private RestaurantInfoDTO restaurantInfo;
    private ArrayList<FoodDTO> menu;

    public RestaurantDTO() {}

    public RestaurantDTO(RestaurantInfoDTO restaurantInfo, ArrayList<FoodDTO> menu) {
        this.restaurantInfo = restaurantInfo;
        this.menu = menu;
    }

    public RestaurantInfoDTO getRestaurantInfo() {
        return restaurantInfo;
    }

    public void setRestaurantInfo(RestaurantInfoDTO restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

    public ArrayList<FoodDTO> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<FoodDTO> menu) {
        this.menu = menu;
    }
}
