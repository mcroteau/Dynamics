<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="dynamics.gain.common.Constants" %>

<html>
<head>
    <title>Removing barriers that prevent those wanting to help others</title>

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
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <div id="header-wrapper">
                <a href="/z" class="logo">Dynamics <br/><strong class="gain">+Gain</strong><br/>
                    <span class="tagline">Goal to Remove Barriers.</span>
                </a>

                <div id="navigation">
                    <span id="welcome">Hello <a href="/z/user/edit/${sessionScope.userId}" class="href-dotted-black zero"><strong>${sessionScope.username}</strong></a>!</span>
                </div>
                <br class="clear"/>
            </div>

            <decorator:body />

            <div id="footer-navigation">
                <%if(Parakeet.hasRole(Constants.ROLE_ADMIN)){%>
                    <a href="/z/admin/towns" class="href-dotted">Towns</a>&nbsp;&nbsp;&nbsp;
                    <a href="/z/admin/locations" class="href-dotted">Locations</a>
                <%}%>
            </div>
        </div>

        <div class="col-sm-1"></div>
    </div>

<div id="footer-wrapper">
    <a href="/z/signout" class="href-dotted">Signout</a>
</div>

</body>
</html>