<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Dynamics +Gain: <decorator:title default="An attempt to fix Homelessness"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/z/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/base.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/z/assets/js/packages/jquery.js"></script>

    <script type="text/javascript" src="/o/assets/js/packages/jquery.js"></script>

    <style>
        body{
            background: #16282d;
        }
    </style>

</head>
<body>

<div id="guest-wrapper">

    <div id="guest-content">
        <decorator:body />
    </div>

    <div id="footer-wrapper">
        <span>&copy; 2021 Dynamics +Gain!</span>
    </div>

    <style>
        #guest-wrapper{
            margin-bottom:30px;
        }
        #footer-wrapper{
            margin:103px auto 200px;
            text-align: center;
        }
    </style>
</div>

</body>
</html>