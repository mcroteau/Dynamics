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
        <tr>
            <th></th>
            <th>Town ID</th>
            <th>Count</th>
            <th></th>
        </tr>
        <c:forEach var="location" items="${locations}">
            <tr>
                <td><a href="/z/admin/locations/edit/${location.id}" class="href-dotted-black">${location.name}</a></td>
                <td class="center">${location.townId}</td>
                <td class="center"><a href="/z/admin/count/${location.id}" class="href-dotted-black">${location.count}</a></td>
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
