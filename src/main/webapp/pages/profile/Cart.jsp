<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="models.OrderItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String username = (String) request.getAttribute("user");
    String restaurant = (String) request.getAttribute("restaurant");
    ArrayList<OrderItem> items = null;
    double total = 0;
    Boolean empty = (Boolean) request.getAttribute("empty");
    Boolean lowCredit = (Boolean) request.getAttribute("lowCredit");
    if(!empty){
        items = (ArrayList<OrderItem>) request.getAttribute("orders");
        total = (Double) request.getAttribute("total");
    }
    if(lowCredit==null){
        lowCredit = false;
    }
%>

<html>
<head>
    <title><%=username%>'s Cart</title>
</head>
<body>
    <c:set var="isemptyv" value="<%=empty%>"></c:set>
    <c:set var="lowcredit" value="<%=lowCredit%>"></c:set>
    <c:if test="${isemptyv eq true}">
        <h2>Your cart is empty</h2>
    </c:if>
    <c:if test="${lowcredit eq true}">
        <h2>your credit is not enough</h2>
    </c:if>
    <c:if test="${not isemptyv}">
        <h3>restaurant : <%=restaurant%></h3>
        <c:forEach items="<%=items%>" var="item">
            <li>${item.foodName} : ${item.count} : ${item.price}$</li>
        </c:forEach>
        <br>
        <h3>
            Total : <%=total%>
        </h3>
        <br>
        <form action="/profile/finalize" method="GET">
            <button type="submit">finalize</button>
        </form>
    </c:if>
</body>
</html>
