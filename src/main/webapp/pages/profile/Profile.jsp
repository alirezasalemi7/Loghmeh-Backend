<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.OrderItem" %>
<%@ page import="models.User" %><%--
  Created by IntelliJ IDEA.
  User: reza
  Date: 2/20/20
  Time: 2:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<OrderItem> orders = (ArrayList<OrderItem>) request.getAttribute("orders");
    User user = (User) request.getAttribute("user");
    boolean creditIsNegative = (boolean) session.getAttribute("creditIsNegative");
    session.removeAttribute("creditIsNegative");
    boolean successFullAddCredit = (boolean) session.getAttribute("successFullAddCredit");
    session.removeAttribute("successFullAddCrediti");
%>

<html>
<head>
    <title>User profile</title>
    <link rel="stylesheet" href="<%=application.getContextPath()%>/css/main.css">
</head>
<body>
    <ul>
        <c:set var="user" value="<%=user%>"></c:set>
        <li>Full name: ${user.name} ${user.family}</li>
        <li>Phone number: ${user.phoneNumber}</li>
        <li>Email: ${user.email}</li>
        <li>Credit: ${user.credit} Toman</li>
        <c:set value="<%=orders.size()%>" var="numberOfOrders" scope="session"></c:set>
<%--        TODO: change address and name after merging.--%>
<%--        <c:choose>--%>
<%--            <c:when test="${numberOfOrders} == 0">--%>
<%--                <li>--%>
<%--                    Orders :<br>--%>
<%--                    <c:forEach items="<%=orders%>" var="order">--%>
<%--                        <ul>--%>
<%--                            <li>--%>
<%--                                    ${order.foodName} : ${order.count}--%>
<%--                            </li>--%>
<%--                        </ul>--%>
<%--                    </c:forEach>--%>
<%--                    <form action="<%=application.getContextPath()%>/profile/cart" method="GET">--%>
<%--                        <button type="submit">Go to cart</button>--%>
<%--                    </form>--%>
<%--                </li>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <p>You doesn't order anything.</p>--%>
<%--                <form action="<%=application.getContextPath()%>/restaurants" method="GET">--%>
<%--                    <button type="submit">Go to restaurants page</button>--%>
<%--                </form>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
    </ul>
    <form action="<%=application.getContextPath()%>/profile/addcredit" method="POST">
        <button type="submit">increase</button>
        <input type="number" name="credit" value="" />
    </form>
    <c:set var="success" value="<%=successFullAddCredit%>"></c:set>
    <c:set var="creditIsNegative" value="<%=creditIsNegative%>"></c:set>
    <c:choose>
        <c:when test="${creditIsNegative eq true}">
            <c:if test="${success eq false}">
                <p class="error">Creidt must be positive</p>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${success eq true}">
                    <p class="success">Increased successfully.</p>
                </c:when>
                <c:otherwise>
                    <p class="error">Input format is wrong</p>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

</body>
</html>
