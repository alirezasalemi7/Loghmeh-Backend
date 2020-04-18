package servlets.listeners;

import database.DAO.OrderDAO;
import database.DAO.OrderState;
import database.DAO.UserDAO;
import database.Mappers.OrderMapper;
import exceptions.RestaurantDoesntExistException;
import exceptions.ServerInternalException;
import exceptions.UserDoesNotExistException;
import models.Location;
import systemHandlers.Domain.OrderTimer;
import systemHandlers.Repositories.OrderRepository;
import systemHandlers.Repositories.UserRepository;
import systemHandlers.Services.OrderDeliveryManager;
import systemHandlers.Services.RestaurantManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class OrderInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            OrderMapper mapper = new OrderMapper();
            ArrayList<OrderDAO> orders = mapper.getNotDeliveredOrders();
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
        catch (SQLException e){
            System.err.println("SQL exception.\nexit.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
