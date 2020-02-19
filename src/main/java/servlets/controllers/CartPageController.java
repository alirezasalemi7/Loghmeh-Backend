package servlets.controllers;

import exceptions.RestaurantDoesntExistException;
import models.OrderItem;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class CartPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String restaurantId;
        String restaurantName = null;
        try {
            restaurantId = DataHandler.getInstance().getUser().getCart().getRestaurantId();
            if(restaurantId!=null){
                restaurantName = SystemManager.getInstance().getRestaurantById(restaurantId).getName();
            }
        }
        catch (RestaurantDoesntExistException e){
            // never reach there
        }
        ArrayList<OrderItem> items = DataHandler.getInstance().getUser().getCart().getOrders();
        if(items.size()>0){
            double total = 0;
            for(OrderItem item : items){
                total += item.getPrice();
            }
            req.setAttribute("orders", items);
            req.setAttribute("user", DataHandler.getInstance().getUser().getName());
            req.setAttribute("empty", false);
            req.setAttribute("total", total);
            req.setAttribute("restaurant", restaurantName);
        }
        else{
            req.setAttribute("user", DataHandler.getInstance().getUser().getName());
            req.setAttribute("empty", true);
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/profile/cart.jsp");
        dispatcher.forward(req, resp);
    }
}
