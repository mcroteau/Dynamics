<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Location</title>
</head>
<body>
<h1>Create Location</h1>
<form action="/z/admin/locations/save" method="post">
    <input type="text" name="name" />
    <select name="townId">
        <c:forEach items="${towns}" var="town">
            <option value="${town.id}">${town.name}</option>
        </c:forEach>
    </select>
    <input type="submit" class="button retro" value="Save"/>
</form>
</body>
</html>
