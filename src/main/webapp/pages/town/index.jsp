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
        ${town.count} <span>Homeless <span class="header-information">in ${town.name}!</span></span>
    </p>

    <p class="open-text regular">Dynamics +Gain is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>


    <h3>Locations</h3>
    <ul>
        <c:forEach var="location" items="${locations}">
            <li style="padding:1px 0px;"><a href="/z/location/${location.locationUri}">${location.name}</a></li>
        </c:forEach>
    </ul>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button super yellow">Give &hearts;</a>
    </div>


    <div style="text-align: left">
        <a href="/z/home" class="href-dotted">&larr; Back</a>
    </div>
</body>
</html>
