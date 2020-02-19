package servlets.controllers;

import exceptions.CartIsEmptyException;
import exceptions.CreditIsNotEnoughException;
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

@WebServlet("profile/finalize")
public class FinalizeOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String restaurantId;
        String restaurantName = null;
        try {
            restaurantId = DataHandler.getInstance().getUser().getCart().getRestaurantId();
            if(restaurantId!=null){
                restaurantName = SystemManager.getInstance().getRestaurantById(restaurantId).getName();
            }
            SystemManager.getInstance().finalizeOrder(DataHandler.getInstance().getUser());
            ArrayList<OrderItem> items = (ArrayList<OrderItem>) DataHandler.getInstance().getUser().getCart().getOrders().clone();
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
        catch (RestaurantDoesntExistException e){
            // never reach there
        }
        catch (CartIsEmptyException e){
            req.setAttribute("user", DataHandler.getInstance().getUser().getName());
            req.setAttribute("empty", true);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/profile/Cart.jsp");
            dispatcher.forward(req, resp);
        }
        catch (CreditIsNotEnoughException e){
            req.setAttribute("lowCredit", true);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/profile/cart");
            dispatcher.forward(req, resp);
        }
    }
}
