package web.html;

import models.Food;
import models.OrderItem;
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
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/AllRestaurantsPage/allRestaurants.html")));
            tableRowContent = new String(Files.readAllBytes(Paths.get("src/main/resources/AllRestaurantsPage/tableRow.txt")));
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

    private String makeMenuContents(String content, ArrayList<Food> foods) {
        String foodInfoContent = null, pageContent = content;
        try {
            foodInfoContent = new String(Files.readAllBytes(Paths.get("src/main/resources/RestaurantPage/foodInfo.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < foods.size(); i++) {
            pageContent = pageContent.replace("FoodInfo", foodInfoContent.replace("FoodImage", foods.get(i).getImageAddress())
                    .replace("FoodName", foods.get(i).getName()).replace("FoodPrice", foods.get(i).getPrice() + "").replace("RestaurantId", foods.get(i).getRestaurantId())
                    + ((i < (foods.size() - 1)) ? "FoodInfo" : ""));
        }
        return pageContent;
    }

    public String makeRestaurantPage(Restaurant restaurant){
        String pageContent = null;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/RestaurantPage/restaurantInfo.html")));
            pageContent = pageContent.replace("RestauratnId", restaurant.getId()).replace("RestaurantName", restaurant.getName())
                            .replace("xLocation", restaurant.getLocation().getX() + "").replace("yLocation", restaurant.getLocation().getY() + "")
                                .replace("RestaurantImage", restaurant.getLogoAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return makeMenuContents(pageContent, restaurant.getMenu());
    }

    public String makeProfilePage(User user,boolean negCredit,boolean successFullAddCredit){
        String pageContent = null;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/userPage.html")));
            pageContent = pageContent.replace("FirstName", user.getName()).replace("LastName", user.getFamily()).replace("PhoneNumber", user.getPhoneNumber())
                            .replace("EmailAddress", user.getEmail()).replace("Credit", user.getCredit() + "");
            if (negCredit) {
                pageContent = pageContent.replace("ErrorMessage", "<p>" + "You enterd a wrong credit" + "</p>");
            } else if (successFullAddCredit) {
                pageContent = pageContent.replace("ErrorMessage", "<p>" + "Increasing credit was successful" + "</p>");
            } else {
                pageContent = pageContent.replace("ErrorMessage", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageContent;
    }

    public String makeCartPage(User user, String restaurantName){
        String pageContent = null, orderContent;
        try {
            if (restaurantName == null || user.getCart().getOrders().size() == 0) {
                return new String(Files.readAllBytes(Paths.get("src/main/resources/CartPages/emptyCartPage.html"))).replace("Message", "Your cart is empty.");
            }
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/CartPages/cartPage.html")));
            orderContent = new String(Files.readAllBytes(Paths.get("src/main/resources/CartPages/order.txt")));
            ArrayList<OrderItem> orders = user.getCart().getOrders();
            for (int i = 0; i < orders.size(); i++) {
                pageContent = pageContent.replace("OrderItem", orderContent.replace("FoodName", orders.get(i).getFoodName())
                                .replace("FoodCount", orders.get(i).getCount() + "") + ((i < (orders.size() - 1)) ? "OrderItem" : ""));
            }
            pageContent = pageContent.replace("RestaurantName", restaurantName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageContent;
    }

    public String makeOrderFinalizedPage(ArrayList<OrderItem> orderItems, User user) {
        String pageContent = null, orderContent;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get("src/main/resources/CartPages/cartPage.txt")));
            orderContent = new String(Files.readAllBytes(Paths.get("src/main/resources/CartPages/order.txt")));
            ArrayList<OrderItem> orders = user.getCart().getOrders();
            for (int i = 0; i < orders.size(); i++)
                pageContent = pageContent.replace("OrderItem", orderContent.replace("FoodName", orders.get(i).getFoodName())
                        .replace("FoodCount", orders.get(i).getCount() + "") + ((i < (orders.size() - 1)) ? "OrderItem" : ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageContent.replace("FinalPrice", user.getCart().getSumOfPrices() + "");
    }

    public String makeRestaurantNotFoundPage(String restaurantId){
        return createErrorPage("Restaurant not found.", "404", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeInvalidRestaurantAccessPage(String restaurantId){
        return createErrorPage("Access to " + restaurantId + " is not allowed.", "403", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeNotEnoughCreditPage(User user){
        return createErrorPage("Your credit(" + user.getCredit() + ") is not enough.", "400", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeCartEmptyErrorPage(){
        return createErrorPage("Your cart empty.", "400", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeMultipleRestaurantAddToCartErrorPage(User user){
        return createErrorPage("You should order from only one restaurant.", "400", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeInvalidRequestPage(String address){
        return createErrorPage(address + " not found.", "400", "src/main/resources/ErrorPages/errorPage.txt");
    }

    public String makeFoodNotFoundPage(String foodName,String restaurantName,String restaurantId){
        return createErrorPage(foodName + " is not found in " + restaurantName, "404", "src/main/resources/ErrorPages/errorPage.txt");
    }

    private String createErrorPage(String message, String errorCode, String filePath) {
        String pageContent = null;
        try {
            pageContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return pageContent.replace("ErrorCode", errorCode).replace("Message", message);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
