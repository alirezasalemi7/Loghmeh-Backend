package web.html;

import models.Restaurant;
import models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HtmlPageMaker {

    public String makeAllRestaurantsPage(ArrayList<Restaurant> restaurants) {
        String pageContent = null, tableRowContent = null;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/allRestaurantsPage/allRestaurants.txt")));
            tableRowContent = new String(Files.readAllBytes(Paths.get("src/main/resources/allRestaurantsPage/tableRow.txt")));
            for (int i = 0; i < restaurants.size(); i++) {
                pageContent = pageContent.replace("TableRows"
                                , tableRowContent.replace("RestaurantId", restaurants.get(i).getId())
                                    .replace("ImageSource", restaurants.get(i).getLogoAddress())
                                        .replace("RestaurantName", restaurants.get(i).getName())
                                            .replace("RestaurantAddress", "/restaurants/" + restaurants.get(i).getId()) + ((i < (restaurants.size() - 1)) ? "TableRows" : ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageContent;
    }

    public String makeRestaurantPage(Restaurant restaurant){
        return null;
    }

    public String makeProfilePage(User user){
        return null;
    }

    public String makeCartPage(User user){
        return null;
    }

    public String makeRestaurantNotFoundPage(String restaurantId){
        return null;
    }

    public String makeInvalidRestaurantAccessPage(String restaurantId){
        return null;
    }

    public String makeNotEnoughProfitPage(User user){
        return null;
    }

    public String makeCartEmptyErrorPage(){
        return null;
    }

    public String makeMultipleRestaurantAddToCartErrorPage(User user){
        return null;
    }

}
