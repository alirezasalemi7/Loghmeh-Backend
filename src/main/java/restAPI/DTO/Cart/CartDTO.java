package restAPI.DTO.Cart;

import java.util.ArrayList;

public class CartDTO {

    private double cost;
    private ArrayList<CartItemDTO> orders;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList<CartItemDTO> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<CartItemDTO> orders) {
        this.orders = orders;
    }
}
