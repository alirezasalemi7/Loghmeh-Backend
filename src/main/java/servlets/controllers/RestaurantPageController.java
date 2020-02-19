package servlets.controllers;

import exceptions.RestaurantDoesntExistException;
import models.Restaurant;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/restaurants/*")
public class RestaurantPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().substring(1);
        try {
            Restaurant restaurant = SystemManager.getInstance().getRestaurantById(id);
            if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                req.setAttribute("restaurant", restaurant);
                RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/restaurants/restaurantInfo.jsp");
                dispatcher.forward(req, resp);
            } else {
//                403 forbidden
            }
        } catch (RestaurantDoesntExistException e) {
//          404 not found
        }
    }
}
