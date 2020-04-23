package dataAccess.Repositories;

import dataAccess.DAO.OrderDAO;
import dataAccess.DAO.OrderState;
import dataAccess.Mappers.OrderMapper;
import business.exceptions.OrderDoesNotExist;
import business.exceptions.ServerInternalException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class OrderRepository {

    private static OrderRepository instance;
    private OrderMapper orderMapper;

    private OrderRepository() throws ServerInternalException{
        try {
            orderMapper = new OrderMapper();
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public static OrderRepository getInstance() throws ServerInternalException{
        if(instance==null){
            instance = new OrderRepository();
        }
        return instance;
    }

    public OrderDAO getOrder(String oid)throws OrderDoesNotExist , ServerInternalException {
        try {
            OrderDAO order = orderMapper.find(oid);
            if(order==null){
                throw new OrderDoesNotExist();
            }
            return order;
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public ArrayList<OrderDAO> getOrdersOfUser(String uid) throws ServerInternalException{
        try {
            return orderMapper.getUserOrders(uid);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void addOrder(OrderDAO order) throws ServerInternalException{
        try {
            orderMapper.insert(order);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw new ServerInternalException();
        }
    }

    public void updateOrderStateAndDate(String oid, OrderState state, Date date) throws ServerInternalException {
        try {
            orderMapper.updateStateAndDate(oid, state, date);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public ArrayList<OrderDAO> getNotDeliveredOrders() throws ServerInternalException{
        try {
            return orderMapper.getNotDeliveredOrders();
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }
}
