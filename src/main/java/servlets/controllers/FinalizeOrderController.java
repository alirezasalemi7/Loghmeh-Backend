package servlets.controllers;

import exceptions.CartIsEmptyException;
import exceptions.CreditIsNotEnoughException;
import models.Order;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile/finalize")
public class FinalizeOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Order order = SystemManager.getInstance().finalizeOrder(DataHandler.getInstance().getUser());
            resp.sendRedirect("/profile/orders?id="+order.getId());
            return;
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
