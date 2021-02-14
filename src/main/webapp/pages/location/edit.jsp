<%@ page import="dynamics.gain.model.Town" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dynamics.gain.model.Location" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Location</title>
</head>
<body>
<h1>Edit Location</h1>
<form action="/z/admin/locations/update" method="post">

    <input type="hidden" name="id" value="${location.id}"/>

    <label>Name</label>
    <input type="text" name="name" value="${location.name}"/>

    <label>Uri</label>
    <input type="text" name="locationUri" value="${location.locationUri}" />

    <label>Town</label>
    <select name="townId" style="display: block">
        <c:forEach items="${towns}" var="town">
            <%
                String selected = "";
                Location location = (Location) request.getAttribute("location");
            %>
            <c:if test="${location.townId == town.id}">
                <%selected = "selected";%>
            </c:if>

            <option value="${town.id}" <%=selected%>>${town.name}</option>
        </c:forEach>
    </select>

    <label>Description</label>
    <textarea name="description">${location.description}</textarea>

<%--    <label>Needs</label>--%>
<%--    <textarea name="needs">${location.needs}</textarea>--%>

    <input type="submit" class="button retro" value="Save" style="display:inline-block;margin:30px auto;"/>
</form>
</body>
</html>
