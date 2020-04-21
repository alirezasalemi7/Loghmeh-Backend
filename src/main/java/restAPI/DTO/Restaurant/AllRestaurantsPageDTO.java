package restAPI.DTO.Restaurant;

import java.util.ArrayList;

public class AllRestaurantsPageDTO {

    private ArrayList<RestaurantInfoDTO> restaurants;
    private int totalPages;
    private int currentPage;

    public ArrayList<RestaurantInfoDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<RestaurantInfoDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
