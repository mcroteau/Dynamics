<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain: ${town.name} Locations</title>
</head>
<body>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <p id="welcome-text">
        ${town.count} <span>Homeless <br/>in <br/>${town.name}!</span>
    </p>

    <p class="open-text regular">Dynamics +Gain is a non profit designed with
        the sole purpose of removing the barriers that prevent others from
        donating time, money and resources!</p>

    <div style="margin-bottom:70px;">
        <a href="/z/donation/select" class="button beauty">Donate &hearts;</a>
    </div>


    <div style="text-align: left">
        <a href="/z/home" class="href-dotted">&larr; Back</a>
    </div>
</body>
</html>
