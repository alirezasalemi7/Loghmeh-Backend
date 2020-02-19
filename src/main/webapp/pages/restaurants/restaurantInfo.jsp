<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="models.Restaurant" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Food" %><%--
  Created by IntelliJ IDEA.
  User: reza
  Date: 2/20/20
  Time: 12:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
    ArrayList<Food> menu = restaurant.getMenu();
%>

<html>
<head>
    <title>Restaurant Info</title>
</head>
<body>
    <ul>
        <li>Id: <%=restaurant.getId()%></li>
        <li>Name: <%=restaurant.getName()%></li>
        <li>Location: (<%=restaurant.getLocation().getX()%>, <%=restaurant.getLocation().getY()%>)</li>
        <li>
            Logo:
            <img src="<%=restaurant.getLogoAddress()%>" alt="logo">
        </li>
        <li>
            Menu: <br>
            <c:set var="numberOfFoods" scope="session" value="<%=menu.size()%>"></c:set>
            <c:choose>
                <c:when test="${numberOfFoods == 0}">
                    <p>Menu is empty!</p>
                </c:when>
                <c:otherwise>
                    <ol>
                        <c:forEach items="<%=menu%>" var="food">
                            <li>
                                <img src="${food.imageAddress}" alt="logo">
                                <div>${food.name}</div>
                                <div>${food.price} Toman</div>
                                <form action="/profile/addtocart" method="post">
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
