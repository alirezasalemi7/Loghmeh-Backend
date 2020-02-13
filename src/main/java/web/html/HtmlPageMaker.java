package web.html;

import models.Food;
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
                pageContent = pageContent.replace("TableRows", tableRowContent.replace("RestaurantId", restaurants.get(i).getId())
                                .replace("ImageSource", restaurants.get(i).getLogoAddress())
                                    .replace("RestaurantName", restaurants.get(i).getName())
                                        .replace("RestaurantAddress", "/restaurants/" + restaurants.get(i).getId()) + ((i < (restaurants.size() - 1)) ? "TableRows" : ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageContent;
    }

    private String makeMenuContents(String pageContent, ArrayList<Food> foods) {
        String foodInfoContent = null;
        try {
            foodInfoContent = new String(Files.readAllBytes(Paths.get("src/main/resources/RestaurantPage/foodInfo.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < foods.size(); i++) {
            pageContent = pageContent.replace("FoodInfo", foodInfoContent.replace("FoodImage", foods.get(i).getImageAddress())
                    .replace("FoodName", foods.get(i).getName()).replace("FoodPrice", foods.get(i).getPrice() + "")
                    + (((i < (foods.size() - 1)) ? "FoodInfo" : "")));
        }
        return pageContent;
    }

    public String makeRestaurantPage(Restaurant restaurant){
        String pageContent = null;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/RestaurantPage/restaurantInfo.txt")));
            pageContent = pageContent.replace("RestauratnId", restaurant.getId()).replace("RestaurantName", restaurant.getName())
                            .replace("xLocation", restaurant.getLocation().getX() + "").replace("yLocation", restaurant.getLocation().getY() + "")
                                .replace("RestaurantImage", restaurant.getLogoAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return makeMenuContents(pageContent, restaurant.getMenu());
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
