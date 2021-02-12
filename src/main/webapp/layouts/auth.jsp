<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Dynamics +Gain! : Removing barriers that prevent those wanting to help!</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/z/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">

    <link rel="stylesheet" href="/z/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/base.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/z/assets/js/packages/jquery.js"></script>

</head>
<body>

<div id="outer-wrapper">




    <div class="container">

        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <div id="header-wrapper">
                    <a href="/z" class="logo">Dynamics <strong class="gain">+Gain</strong><br/>
                        <span class="tagline">Assisting those wanting to help others!</span>
                    </a>

                    <div id="navigation">
                        <span id="welcome">Hello <a href="/z/user/edit/${sessionScope.userId}" class="href-dotted-black zero"><strong>${sessionScope.username}</strong></a>!</span>
                    </div>
                    <br class="clear"/>
                </div>

                <decorator:body />
            </div>

            <div class="col-sm-1"></div>
        </div>

    <div id="footer-wrapper">
        <a href="/z/signout" class="href-dotted">Signout</a>
    </div>

</div>

</body>
</html>