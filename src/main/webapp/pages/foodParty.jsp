<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Restaurant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("foodPartyRestaurants");
%>

<html>
<head>
    <title>Food Party</title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath()%>/css/main.css">
</head>
<body>
<%--TODO: Check view of a restaurant when we have more than 2 food in it & Recover css file in inappropriate view--%>
    <ol>
        <c:forEach items="<%=restaurants%>" var="restaurant">
            <li>
                <div>${restaurant.name}</div>
            <img src="${restaurant.logoAddress}" alt="logo">
                <c:forEach items="${restaurant.specialMenu}" var="food">
                    <li class="horizontal-li">
                        <img src="${food.value.imageAddress}" alt="logo">
                        <div>${food.value.name}</div>
                        <div>${food.value.description}</div>
                        <div class="old-price">${food.value.oldPrice} Tomsn</div>
                        <div>${food.value.price} Toman</div>
                        <div>Remaining count: ${food.value.count}</div>
                        <div>Popularity: ${food.value.popularity}</div>
                        <form action="<%=application.getContextPath()%>/profile/addtocart" accept-charset="UTF-8" method="post">
                            <input type="hidden" name="foodName" value="${food.value.name}">
                            <input type="hidden" name="restaurantId" value="${food.value.restaurantId}">
                            <button type="submit">Add to cart</button>
                        </form>
                    </li>
                </c:forEach>
            </li>
        </c:forEach>
    </ol>
</body>
</html>
