<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.OrderItem" %>
<%@ page import="models.User" %>
<%@ page import="models.Order" %><%--
  Created by IntelliJ IDEA.
  User: reza
  Date: 2/20/20
  Time: 2:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
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
    <%@include file="/pages/NavigationBar.jsp"%>
    <ul>
        <c:set var="user" value="<%=user%>"></c:set>
        <li class="horizontal-li">Full name: ${user.name} ${user.family}</li>
        <li class="horizontal-li">Phone number: ${user.phoneNumber}</li>
        <li class="horizontal-li">Email: ${user.email}</li>
        <li class="horizontal-li">Credit: ${user.credit} Toman</li>
    </ul>
    <form action="<%=application.getContextPath()%>/profile/addcredit" method="POST">
        <button type="submit">increase</button>
        <input type="number" name="credit" value="" required/>
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
    <c:set value="<%=orders.size()%>" var="numberOfOrders" scope="session"></c:set>
    <c:choose>
        <c:when test="${numberOfOrders > 0}">
            <ul>
                <c:forEach var="item" items="<%=orders%>">
                    <li>
                        <a href="<%=application.getContextPath()%>/profile/orders?id=${item.id}">order id : ${item.id}</a>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <p>
                you don't have any orders
            </p>
        </c:otherwise>
    </c:choose>

</body>
</html>
