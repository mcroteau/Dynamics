<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Town</title>
</head>
<body>
<h1>Create Town</h1>
<form action="/z/admin/towns/save" method="post">

    <label>Name</label>
    <input type="text" name="name" />

    <input type="submit" class="button retro" value="Save"/>
</form>
</body>
</html>
