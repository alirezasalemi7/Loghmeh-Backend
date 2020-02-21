<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="models.Restaurant" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Food" %>
<%@ page import="models.NormalFood" %>
<%@ page pageEncoding="utf-8" %>
<%--
  Created by IntelliJ IDEA.
  User: reza
  Date: 2/20/20
  Time: 12:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
    Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
    ArrayList<NormalFood> menu = new ArrayList<>(restaurant.getNormalMenu().values());
%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant Info</title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath()%>/css/main.css">
</head>
<body>
    <ul>
        <li class="horizontal-li">Id: <%=restaurant.getId()%></li>
        <li class="horizontal-li">Name: <%=restaurant.getName()%></li>
        <li class="horizontal-li">Location: (<%=restaurant.getLocation().getX()%>, <%=restaurant.getLocation().getY()%>)</li>
        <li class="horizontal-li">
            Logo:
            <img src="<%=restaurant.getLogoAddress()%>" alt="logo">
        </li>
        <li class="horizontal-li">
            Menu: <br>
            <c:set var="numberOfFoods" scope="session" value="<%=menu.size()%>"></c:set>
            <c:choose>
                <c:when test="${numberOfFoods == 0}">
                    <p>Menu is empty!</p>
                </c:when>
                <c:otherwise>
                    <ol>
                        <c:forEach items="<%=menu%>" var="food">
                            <li class="horizontal-li">
                                <img src="${food.imageAddress}" alt="logo">
                                <div>${food.name}</div>
                                <div>${food.price} Toman</div>
                                <form action="<%=application.getContextPath()%>/profile/addtocart" accept-charset="UTF-8" method="post">
                                    <input type="hidden" name="foodName" value="${food.name}">
                                    <input type="hidden" name="restaurantId" value="${food.restaurantId}">
                                    <button type="submit">Add to cart</button>
                                </form>
                            </li>
                        </c:forEach>
                    </ol>
                </c:otherwise>
            </c:choose>
        </li>
    </ul>
</body>
</html>
