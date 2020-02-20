package servlets.controllers;

import exceptions.NegativeChargeAmountException;
import systemHandlers.SystemManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            } catch (NegativeChargeAmountException e) {
                negativeCredit = true;
                successFulIncrease = false;
            } catch (NumberFormatException e) {
                negativeCredit = false;
                successFulIncrease = false;
            }
            req.setAttribute("negativeCredit", negativeCredit);
            req.setAttribute("successFullAddCredit", successFulIncrease);
            resp.sendRedirect("/pages/profile/Profile.jsp");
        }
    }
}
