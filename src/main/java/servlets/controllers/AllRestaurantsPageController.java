package servlets.controllers;

import exceptions.RestaurantDoesntExistException;
import models.Restaurant;
import models.User;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/restaurants")
public class AllRestaurantsPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Restaurant> restaurants = SystemManager.getInstance().getInRangeRestaurants(SystemManager.getInstance().getUser());
        ArrayList<Long> estimates = new ArrayList<>();
        User user = SystemManager.getInstance().getUser();
        for (Restaurant restaurant : restaurants) {
            try {
                estimates.add(SystemManager.getInstance().estimateDeliveryTime(user, restaurant));
            } catch (RestaurantDoesntExistException e) {
                //Never reach here.
            }
        }
        req.setAttribute("restaurants", restaurants);
        req.setAttribute("estimates", estimates);
        resp.setStatus(200);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/restaurants/allRestaurants.jsp");
        dispatcher.forward(req, resp);
    }
}
