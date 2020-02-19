<%--
  Created by IntelliJ IDEA.
  User: reza
  Date: 2/20/20
  Time: 2:10 AM
  To change this template use File | Settings | File Templates.
--%>

<%
    String errorCode = (String) request.getAttribute("errorCode");
    String message = (String) request.getAttribute("message");
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%=errorCode%></title>
    <link rel="stylesheet" type="text/css" href="<%=application.getContextPath()%>/css/main.css">
</head>
<body>
    <h3><%=errorCode%></h3>
    <p class="error"><%=message%></p>
</body>
</html>
