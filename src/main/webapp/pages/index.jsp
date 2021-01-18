<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : An attempt to fix Homelessness </title>
</head>
<body>
    <div id="homepage-wrapper">

        <c:if test="${not empty message}">
            <p class="notify">${message}</p>
        </c:if>

        <p id="welcome-text">
            ${count} <span>Homeless!</span>
        </p>

        <p class="open-text">Dynamics +Gain is a non profit designed with
            the sole purpose of removing the barriers to assist those in need.</p>

        <div style="margin:0px auto 30px;">
            <a href="/z/donation/select" class="button beauty">Give &hearts;</a>
        </div>

        <h3>Towns/Cities</h3>
        <ul>
            <c:forEach var="town" items="${towns}">
                <li><a href="/z/town/${town.town_uri}">${town.name}</a></li>
            </c:forEach>
        </ul>

    </div>

</body>