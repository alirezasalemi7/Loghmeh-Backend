package servlets.controllers;

import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import exceptions.UnregisteredOrderException;
import models.Food;
import models.OrderItem;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@WebServlet("/profile/addtocart")
public class AddToCartController extends HttpServlet {

    private RequestDispatcher dispatchErrorPage(String errorCode, String message, HttpServletRequest req) {
        req.setAttribute("errorCode", errorCode);
        req.setAttribute("message", message);
        return req.getRequestDispatcher("/pages/errors/errorPage.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String restaurantId = req.getParameter("restaurantId");
        String foodName = req.getParameter("foodName");
        RequestDispatcher dispatcher;
        if (restaurantId == null || foodName == null) {
            resp.setStatus(400);
            dispatcher = dispatchErrorPage("400", "Bad request.", req);
        } else {
            foodName = new String(foodName.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
            Restaurant restaurant = null;
            try {
                restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
                if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                    Food food = restaurant.getFoodByName(foodName);
                    User user = SystemManager.getInstance().getUser();
                    SystemManager.getInstance().addToCart(food, user);
                    resp.sendRedirect("/restaurants/" + restaurantId);
                    return;
                } else {
                    resp.setStatus(403);
                    dispatcher = dispatchErrorPage("403", "You're not allowed to see this page.", req);
                }
            } catch (FoodDoesntExistException e) {
                resp.setStatus(404);
                dispatcher = dispatchErrorPage("404", "Food not found.", req);
            } catch (RestaurantDoesntExistException e) {
                resp.setStatus(404);
                dispatcher = dispatchErrorPage("404", "Restaurant not found.", req);
            } catch (UnregisteredOrderException e) {
                resp.setStatus(400);
                dispatcher = dispatchErrorPage("400", "Bad request. unregistered order", req);
            }
        }
        dispatcher.forward(req, resp);
    }
}
