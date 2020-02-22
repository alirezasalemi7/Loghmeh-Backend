package servlets.controllers;

import exceptions.FoodCountIsNegativeException;
import exceptions.FoodDoesntExistException;
import exceptions.RestaurantDoesntExistException;
import exceptions.UnregisteredOrderException;
import models.*;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        String foodType = req.getParameter("foodType");
        RequestDispatcher dispatcher;
        if (restaurantId == null || foodName == null || foodType == null) {
            resp.setStatus(400);
            dispatcher = dispatchErrorPage("400", "Bad request.", req);
        } else {
            foodName = new String(foodName.getBytes(StandardCharsets.ISO_8859_1),"UTF-8");
            try {
                Restaurant restaurant = SystemManager.getInstance().getRestaurantById(restaurantId);
                User user = SystemManager.getInstance().getUser();
                if (foodType.equals("special")) {
                    SpecialFood food = restaurant.getSpecialFoodByName(foodName);
                    SystemManager.getInstance().addToCart(food, SystemManager.getInstance().getUser());
                    food.setCount(food.getCount() - 1);
                    resp.setStatus(200);
                    resp.sendRedirect(req.getRequestURL().toString().replace(req.getServletPath(), "") + "/restaurants/foodparty");
                    return;
                } else if (foodType.equals("normal")) {
                    NormalFood food = restaurant.getNormalFoodByName(foodName);
                    if (restaurant.getLocation().getDistance(SystemManager.getInstance().getUser().getLocation()) <= 170) {
                        SystemManager.getInstance().addToCart(food, user);
                        resp.setStatus(200);
                        resp.sendRedirect(req.getRequestURL().toString().replace(req.getServletPath(), "") + "/restaurants/" + restaurantId);
                        return;
                    } else {
                        resp.setStatus(403);
                        dispatcher = dispatchErrorPage("403", "You're not allowed to see this page.", req);
                    }
                } else {
                    resp.setStatus(400);
                    System.err.println("here");
                    dispatcher = dispatchErrorPage("400", "Bad request.", req);
                }
            } catch (FoodDoesntExistException e) {
                resp.setStatus(404);
                dispatcher = dispatchErrorPage("404", "Food not found.", req);
            } catch (RestaurantDoesntExistException e) {
                resp.setStatus(404);
                dispatcher = dispatchErrorPage("404", "Restaurant not found.", req);
            } catch (UnregisteredOrderException e) {
                resp.setStatus(400);
                dispatcher = dispatchErrorPage("400", "Bad request. You have some unregistered orders in your cart.", req);
            } catch (FoodCountIsNegativeException e) {
                dispatcher = dispatchErrorPage("400", "Bad request.", req);
            }
        }
        dispatcher.forward(req, resp);
    }
}
