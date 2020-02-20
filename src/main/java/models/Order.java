package models;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Order {

    public static enum OrderState {DeliveryManFinding,InRoad,Delivered}
    private static int nextId = 1;
    private int id;
    private OrderState state;
    private DeliveryMan deliveryMan;
    private ArrayList<OrderItem> items;
    private User user;
    private Restaurant restaurant;
    private Timer timer = new Timer();
    private long remainingTime = -1;


    public Order(ArrayList<OrderItem> items,User user,Restaurant restaurant){
        this.id = nextId;
        nextId++;
        this.state = OrderState.DeliveryManFinding;
        this.deliveryMan = null;
        this.items = items;
        this.user = user;
        this.restaurant = restaurant;
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

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
        changeStateToInRoad();
    }

    private class DeliveryTask extends TimerTask{

        @Override
        public void run() {
            state = OrderState.Delivered;
        }
    }

    private class RemainingTimeTask extends TimerTask{

        @Override
        public void run() {
            remainingTime-=1;
            if(remainingTime==0){
                this.cancel();
            }
        }
    }

    private void changeStateToInRoad(){
        this.state = OrderState.InRoad;
        double distance = restaurant.getLocation().getDistance(user.getLocation())+restaurant.getLocation().getDistance(deliveryMan.getLocation());
        long deliveryTime = (long) (distance/deliveryMan.getVelocity());
        this.remainingTime = deliveryTime;
        timer.schedule(new DeliveryTask(), 1000*deliveryTime);
        timer.scheduleAtFixedRate(new RemainingTimeTask(), 0,1000);
    }

    public Restaurant getRestaurant(){
        return this.restaurant;
    }

    public User getUser(){
        return this.getUser();
    }

    public long getRemainingTime(){
        return this.remainingTime;
    }

    public double getTotalCost(){
        double cost = 0;
        for(OrderItem item : items){
            cost += item.getPrice();
        }
        return cost;
    }
}
