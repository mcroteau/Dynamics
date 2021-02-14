<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain: ${town.name} Locations</title>
</head>
<body>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <p>Please help!</p>

    <p id="welcome-text" class="help">
        ${town.count} <span>Homeless <span class="header-information">in
            <strong class="highlight">${town.name}</strong></span></span>
    </p>

    <p class="open-text regular">Dynamics +Gain is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>


    <h3>Organizations &amp; Shelters</h3>
    <table>
        <c:forEach var="location" items="${locations}">
            <tr>
                <td class="center">
                    <a href="/z/donate/${location.id}" class="href-dotted">${location.name} ${location.count} in need</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <p>or</p>

    <p>Give directly to Dynmaics <strong>+Gain</strong></p>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button yellow small">Give &hearts;</a>
    </div>

</body>
</html>
