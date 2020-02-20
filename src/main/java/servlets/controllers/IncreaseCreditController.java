package servlets.controllers;

import exceptions.NegativeChargeAmountException;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile/addcredit")
public class IncreaseCreditController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String credit = req.getParameter("credit");
        if (credit == null) {
            req.setAttribute("errorCode", 400);
            req.setAttribute("message", "Bad request.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/errors/errorPage.jsp");
            dispatcher.forward(req, resp);
        } else {
            boolean negativeCredit;
            boolean successFulIncrease;
            try {
                double creditValue = Double.parseDouble(credit);
                SystemManager.getInstance().increaseCredit(SystemManager.getInstance().getUser(), creditValue);
                negativeCredit = false;
                successFulIncrease = true;
                resp.setStatus(400);
            } catch (NegativeChargeAmountException e) {
                negativeCredit = true;
                successFulIncrease = false;
                resp.setStatus(400);
            } catch (NumberFormatException e) {
                negativeCredit = false;
                successFulIncrease = false;
                resp.setStatus(400);
            }
            HttpSession session = req.getSession();
            session.setAttribute("creditIsNegative", negativeCredit);
            session.setAttribute("successFullAddCredit", successFulIncrease);
            resp.sendRedirect(req.getRequestURL().toString().replace(req.getServletPath(), "") + "/profile");
        }
    }
}
