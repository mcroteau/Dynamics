<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Dynamics +Gain: <decorator:title default="An attempt to fix Homelessness"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/z/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/base.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/z/assets/js/packages/jquery.js"></script>

</head>
<body>

<div class="container">

    <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8">

            <div id="guest-content">
                <decorator:body />
            </div>

            <div id="footer-wrapper">
                <span class="regular">&copy; 2021 <br/>Dynamics<br/> <span class="fun">+Gain</span></span>
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

        <div class="col-sm-2"></div>
    </div>
</div>

</body>
</html>