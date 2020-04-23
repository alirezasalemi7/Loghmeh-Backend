package services.DTO.Order;

import java.util.ArrayList;

public class OrderDetailDTO {

    private double totalCost;
    private ArrayList<OrderItemDTO> order;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ArrayList<OrderItemDTO> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderItemDTO> order) {
        this.order = order;
    }
}
