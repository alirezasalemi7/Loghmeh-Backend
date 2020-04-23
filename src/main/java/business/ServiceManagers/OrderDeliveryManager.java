package business.ServiceManagers;

import dataAccess.DAO.OrderDAO;
import dataAccess.DAO.OrderState;
import business.exceptions.ServerInternalException;
import business.Domain.Location;
import business.Domain.OrderTimer;
import dataAccess.Repositories.OrderRepository;

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
