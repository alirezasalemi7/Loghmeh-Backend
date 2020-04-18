package systemHandlers.Services;

import database.DAO.OrderDAO;
import database.DAO.OrderState;
import exceptions.ServerInternalException;
import models.Location;
import systemHandlers.Domain.OrderTimer;
import systemHandlers.Repositories.OrderRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class OrderDeliveryManager {

    private static OrderDeliveryManager instance;
    private HashMap<String,OrderTimer> notDeliveredOrders = new HashMap<>();

    public static OrderDeliveryManager getInstance(){
        if(instance==null){
            instance = new OrderDeliveryManager();
        }
        return instance;
    }

    private OrderDeliveryManager(){}

    public void updateOrderState(String orderId, OrderState state, Date date) throws ServerInternalException {
        OrderRepository.getInstance().updateOrderStateAndDate(orderId, state, date);
    }

    public void putOrderTimer(OrderTimer timer){
        notDeliveredOrders.put(timer.getOrderId(), timer);
    }

    public void clearOrderTimer(String orderId){
        notDeliveredOrders.remove(orderId);
    }

    public void addOrderToDeliver(OrderDAO order, Location restaurant,Location user){
        OrderTimer timer = new OrderTimer(order.getId(), user, restaurant);
        notDeliveredOrders.put(order.getId(), timer);
        timer.start();
    }
}
