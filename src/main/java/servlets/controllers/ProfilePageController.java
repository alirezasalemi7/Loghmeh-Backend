package servlets.controllers;

import models.OrderItem;
import models.User;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/profile")
public class ProfilePageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SystemManager.getInstance().getUser();
        ArrayList<OrderItem> orders = user.getCart().getOrders();
        req.setAttribute("user", user);
        req.setAttribute("orders", orders);
        HttpSession session = req.getSession();
        Object creditIsNegative = session.getAttribute("creditIsNegative");
        session.removeAttribute("creditIsNegative");
        Object successFullAddCredit = session.getAttribute("successFullAddCredit");
        session.removeAttribute("successFullAddCredit");
        session.setAttribute("creditIsNegative", (creditIsNegative == null) || ((boolean) creditIsNegative));
        session.setAttribute("successFullAddCredit", (creditIsNegative == null) || ((boolean) successFullAddCredit));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/profile/Profile.jsp");
        dispatcher.forward(req, resp);
    }
}
