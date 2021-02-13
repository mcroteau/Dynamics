<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Towns</title>
</head>
<body>
<h1>Locations</h1>
<a href="/z/admin/locations/create" class="href-dotted">+ New Location</a>
<c:if test="${locations.size() > 0}">
    <table>
        <c:forEach var="location" items="${locations}">
            <tr>
                <td>${location.name}</td>
                <td class="right">
                    <form action="/z/admin/locations/delete/${location.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete Location?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${locations.size() == 0}">
    <p>No towns created yet.</p>
</c:if>
</body>
</html>
