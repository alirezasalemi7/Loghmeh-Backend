package servlets.listeners;

import database.OrderMapper;
import models.Order;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            OrderMapper mapper = new OrderMapper();
            ArrayList<Order> orders = mapper.getNotDeliveredOrders();
            for (Order order : orders){
                if(order.getState()==Order.OrderState.InRoad){
                    //todo : change state
                }
                else{
                    order.searchForDelivery();
                }
            }
        }
        catch (SQLException e){
            System.err.println("SQL exception.\nexit.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
