package servlets.controllers;

import models.Restaurant;
import systemHandlers.DataHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/foodParty")
public class FoodPartyController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Restaurant> foodPartyRestaurants = new ArrayList<>();
        for (Restaurant restaurant : DataHandler.getInstance().getAllRestaurant().values()) {
            if (restaurant.getSpecialMenu().size() != 0) {
                foodPartyRestaurants.add(restaurant);
            }
        }
        req.setAttribute("foodPartyRestaurants", foodPartyRestaurants);
        resp.setStatus(200);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/foodParty.jsp");
        dispatcher.forward(req, resp);
    }
}
