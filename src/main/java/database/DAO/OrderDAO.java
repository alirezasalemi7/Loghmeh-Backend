package database.DAO;

import models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class OrderDAO {

    private String id;
    private OrderState state;
    private DeliveryMan deliveryMan;
    private ArrayList<OrderItemDAO> items;
    private String userId;
    private String restaurantId;
    private String restaurantName;
    private Date arrivalDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public ArrayList<OrderItemDAO> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItemDAO> items) {
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public double getTotalCost(){
        double cost = 0;
        for(OrderItemDAO itemDAO : items){
            cost += itemDAO.getCost();
        }
        return cost;
    }
}
