package models;


import java.util.ArrayList;

public class Order {

    public static enum OrderState {DeliveryManFinding,InRoad,Delivered}
    private static int nextId = 1;
    private int id;
    private OrderState state;
    private DeliveryMan deliveryMan;
    private ArrayList<OrderItem> items;
    private User user;


    public Order(ArrayList<OrderItem> items,User user){
        this.id = nextId;
        nextId++;
        this.state = OrderState.DeliveryManFinding;
        this.deliveryMan = null;
        this.items = items;
        this.user = user;
    }


    public int getId() {
        return id;
    }

    public OrderState getState() {
        return state;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
