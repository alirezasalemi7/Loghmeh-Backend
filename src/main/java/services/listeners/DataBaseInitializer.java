package services.listeners;

import dataAccess.Mappers.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class DataBaseInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            UserMapper userMapper = new UserMapper();
            CartMapper cartMapper = new CartMapper();
            OrderMapper orderMapper = new OrderMapper();
            CartItemMapper cartItemMapper = new CartItemMapper();
            OrderItemMapper orderItemMapper = new OrderItemMapper();
            RestaurantMapper restaurantMapper = new RestaurantMapper();
            FoodMapper foodMapper = new FoodMapper();
        }
        catch (SQLException e){
            System.err.println("database error.\nexit.");
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
