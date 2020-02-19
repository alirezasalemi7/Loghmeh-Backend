<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Restaurant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("restaurants");
%>

<html>
<head>
    <title>All restaurants</title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath()%>/css/main.css">
</head>
<body>
    <table>
        <thead>
            <th>Id</th>
            <th>Logo</th>
            <th>Name</th>
            <th>Page</th>
        </thead>
        <tbody>
            <c:forEach items="<%=restaurants%>" var="item">
                <tr>
                    <td>${item.id}</td>
                    <td><img class="logo" src="${item.logoAddress}" alt="logo"></td>
                    <td>${item.name}</td>
                    <td>
                        <form action="restaurants/${item.id}">
                            <input type="submit" value="Resaturant Page" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
