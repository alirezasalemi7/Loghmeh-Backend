package systemHandlers.Repositories;

import models.Order;

import java.util.ArrayList;

public class OrderRepository {

    private static OrderRepository instance;

    private OrderRepository(){}

    public static OrderRepository getInstance(){
        if(instance==null){
            instance = new OrderRepository();
        }
        return instance;
    }

    public Order getOrder(String oid){
        return null;
    }

    ArrayList<Order> getOrdersOfUser(String uid){
        return null;
    }

    public void addOrder(Order order){

    }

    public void updateOrderState(String oid,Order.OrderState state){

    }
}
