package services.listeners;

import dataAccess.DAO.OrderDAO;
import dataAccess.DAO.OrderState;
import dataAccess.DAO.UserDAO;
import business.exceptions.RestaurantDoesntExistException;
import business.exceptions.ServerInternalException;
import business.exceptions.UserDoesNotExistException;
import business.Domain.Location;
import business.Domain.OrderTimer;
import dataAccess.Repositories.OrderRepository;
import dataAccess.Repositories.UserRepository;
import business.ServiceManagers.OrderDeliveryManager;
import business.ServiceManagers.RestaurantManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.Date;

@WebListener
public class OrderInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            ArrayList<OrderDAO> orders = OrderRepository.getInstance().getNotDeliveredOrders();
            for (OrderDAO order : orders){
                if(order.getState()== OrderState.InRoad){
                    try {
                        if((new Date()).compareTo(order.getArrivalDate())>0){
                            OrderRepository.getInstance().updateOrderStateAndDate(order.getId(), OrderState.Delivered, order.getArrivalDate());
                        }
                        else{
                            long time = order.getArrivalDate().getTime() - (new Date()).getTime();
                            OrderTimer timer = new OrderTimer(order.getId(), null, null);
                            timer.setArrivalDate(order.getArrivalDate());
                            OrderDeliveryManager.getInstance().putOrderTimer(timer);
                            timer.setDeliveryTask(time);
                        }
                    }
                    catch (ServerInternalException e){
                        System.err.println("mysql is not available.\n exit.");
                        System.exit(1);
                    }
                }
                else{
                    try {
                        UserDAO user = UserRepository.getInstance().getUser(order.getUserId());
                        Location restaurantLocation = RestaurantManager.getInstance().getRestaurantLocation(order.getRestaurantId());
                        OrderDeliveryManager.getInstance().addOrderToDeliver(order, restaurantLocation, user.getLocation());
                    }
                    catch (RestaurantDoesntExistException e){
                        //never reach
                    }catch (ServerInternalException e){
                        System.err.println("mysql is not available.\n exit.");
                        System.exit(1);
                    }
                    catch (UserDoesNotExistException e){/*never reach*/}
                }
            }
        }
        catch (ServerInternalException e){
            System.err.println("mySQL exception.\nexit.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
