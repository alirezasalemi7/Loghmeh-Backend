package systemHandlers.Repositories;

import database.DAO.OrderDAO;
import database.DAO.OrderState;
import exceptions.OrderDoesNotExist;
import models.Order;

import java.util.ArrayList;
import java.util.Date;

public class OrderRepository {

    private static OrderRepository instance;

    private OrderRepository(){}

    public static OrderRepository getInstance(){
        if(instance==null){
            instance = new OrderRepository();
        }
        return instance;
    }

    public OrderDAO getOrder(String oid)throws OrderDoesNotExist {
        return null;
    }

    public ArrayList<OrderDAO> getOrdersOfUser(String uid){
        return null;
    }

    public void addOrder(OrderDAO order){

    }

    public void updateOrderState(String oid, OrderState state,Date date){

    }
}
