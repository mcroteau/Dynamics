<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : An attempt to remove barriers that prevent those wanting to help.</title>
</head>
<body>
    <div id="homepage-wrapper">

        <c:if test="${not empty message}">
            <p class="notify">${message}</p>
        </c:if>

        <p id="welcome-text">
            ${count} <span>Homeless <span class="header-information">Registered in ${towns.size()} Cities!</span></span>
        </p>

        <p class="open-text regular">Dynamics +Gain is a non profit designed with
            the sole purpose of removing the barriers that prevent others from
            donating time, money and resources!</p>

        <div style="margin:0px auto 30px;">
            <a href="/z/donation/select" class="button beauty">Give &hearts;</a>
        </div>

        <h3>Towns/Cities</h3>
        <ul>
            <c:forEach var="town" items="${towns}">
                <li style="padding:1px 0px;"><a href="/z/town/${town.townUri}">${town.name}</a></li>
            </c:forEach>
        </ul>

    </div>

</body>