package servlets.controllers;

import exceptions.OrderDoesNotExist;
import models.Order;
import systemHandlers.DataHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile/orders")
public class OrderPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("id");
        try {
            if(orderId!=null){
                Order order = DataHandler.getInstance().getOrder(orderId);
                req.setAttribute("order", order);
                req.setAttribute("user", DataHandler.getInstance().getUser().getName());
                RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/profile/FinalizedOrder.jsp");
                dispatcher.forward(req, resp);
            }
            else {
                throw new OrderDoesNotExist();
            }
        }
        catch (OrderDoesNotExist e){
            RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/profile/errors/InvalidOrder.jsp");
            resp.setStatus(404);
            dispatcher.forward(req, resp);
        }
    }
}
