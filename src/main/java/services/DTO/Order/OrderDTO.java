package services.DTO.Order;

import dataAccess.DAO.OrderState;

public class OrderDTO {

    private String id;
    private OrderState orderStatus;
    private String restaurantName;
    private OrderDetailDTO details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderState getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderState orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public OrderDetailDTO getDetails() {
        return details;
    }

    public void setDetails(OrderDetailDTO details) {
        this.details = details;
    }
}
