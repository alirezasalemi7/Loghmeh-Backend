<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="models.OrderItem" %>
<%@ page import="models.Order" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String username = (String) request.getAttribute("user");
    Boolean valid = (Boolean) request.getAttribute("valid");
    Order order = null;
    if(valid){
        order = (Order) request.getAttribute("order");
    }
    int state = 0;
    if(order!=null){
        switch (order.getState()){
            case InRoad: state = 1;break;
            case Delivered: state = 2;break;
            case DeliveryManFinding: state = 0;break;
        }
    }

%>

<html>
<head>
    <title><%=username%>'s order</title>
</head>
<body>
<c:if test="${isvalid}">
    <h3>restaurant : <%=order.getRestaurant().getName()%></h3>
    <c:forEach items="<%=order.getItems()%>" var="item">
        <li>${item.foodName} : ${item.count} : ${item.price}$</li>
    </c:forEach>
    <br>
    <h3>
        Total : <%=order.getTotalCost()%>
    </h3>
    <br>
    <c:set var="orderState" value="<%=state%>"></c:set>
    <c:if test="${orderState == 0}">
        <h3>
            status : finding delivery
        </h3>
    </c:if>
    <c:if test="${orderState == 1}">
        <h3>
            status : delivering
        </h3>
        <p>
            remaining time : <%=order.getRemainingTime()/60%>min <%=order.getRemainingTime()%60%> sec
        </p>
    </c:if>
    <c:if test="${orderState == 2}">
        <h3>
           status : enjoy your meal
        </h3>
    </c:if>

</c:if>
</body>
</html>
