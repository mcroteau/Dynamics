<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Select an Organization</title>
</head>
<body>

    <style>
        h3{
            margin:40px auto 4px !important;
        }
        table{
            margin:0px auto 40px auto;
        }
    </style>

    <p>Please help!</p>

    <h1>Organizations</h1>

    <p>Please select an organization or give directly to Dynamics +Gain</p>

    <c:forEach var="town" items="${locations}">
        <h3>${town.name}</h3>
        <table>
            <c:forEach var="location" items="${town.locations}">
                <tr>
                    <td class="center" style="padding-left:10px;width:50%">
                        <a href="/z/donate/${location.id}" class="href-dotted">${location.name} ${location.count} in need</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:forEach>

    <p>or</p>

    <p>Give directly to Dynamics <strong>+Gain</strong></p>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button yellow small">Give Now &hearts;</a>
    </div>

</body>
</html>
