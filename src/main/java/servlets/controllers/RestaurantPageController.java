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
import java.io.PrintWriter;

@WebServlet("/restaurants/*")
public class RestaurantPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().substring(1);
        RequestDispatcher dispatcher;
        try {
            Restaurant restaurant = SystemManager.getInstance().getRestaurantById(id);
            if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                req.setAttribute("restaurant", restaurant);
                dispatcher = req.getRequestDispatcher("/pages/restaurants/restaurantInfo.jsp");
            } else {
                req.setAttribute("errorCode", "403");
                req.setAttribute("message", "You're not allowed to see this page.");
                dispatcher = req.getRequestDispatcher("/pages/errors/errorPage.jsp");
            }
        } catch (RestaurantDoesntExistException e) {
            req.setAttribute("errorCode", "404");
            req.setAttribute("message", "Page not found.");
            dispatcher = req.getRequestDispatcher("/pages/errors/errorPage.jsp");
        }
        dispatcher.forward(req, resp);
    }
}