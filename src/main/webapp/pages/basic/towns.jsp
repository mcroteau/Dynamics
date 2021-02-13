<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : Removing barriers that prevent those wanting to help!.</title>
</head>
<body>
    <h1>Towns/Cities</h1>
    <p>Below is a list of towns and cities with data available.</p>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:1px 0px;"><a href="/z/towns/${town.townUri}" class="href-dotted">${town.name}</a></li>
        </c:forEach>
    </ul>
</body>
</html>
