package restAPI.DTO.Order;

import java.util.ArrayList;

public class OrderDetailDTO {

    private double totalCost;
    private ArrayList<OrderItem> order;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ArrayList<OrderItem> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderItem> order) {
        this.order = order;
    }
}
