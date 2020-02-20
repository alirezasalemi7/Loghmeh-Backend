package servlets.controllers;

import models.Restaurant;
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
        req.setAttribute("restaurants", restaurants);
        resp.setStatus(200);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/restaurants/allRestaurants.jsp");
        dispatcher.forward(req, resp);
    }
}
