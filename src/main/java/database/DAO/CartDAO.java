package database.DAO;

import java.util.HashMap;

public class CartDAO {

    private String restaurantId;
    private String userId;
    private HashMap<String,CartItemDAO> items;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getSumOfPrices() {
        double sumOfPrices = 0;
        for(CartItemDAO item : items.values()){
            sumOfPrices += item.getCost();
        }
        return sumOfPrices;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, CartItemDAO> getItems() {
        return items;
    }

    public void setItems(HashMap<String, CartItemDAO> items) {
        this.items = items;
    }
}
