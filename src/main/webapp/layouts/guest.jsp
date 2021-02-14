<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Dynamics + Gain: <decorator:title default="The Goal is to Remove Barriers"/></title>

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

        <div class="col-sm-12">

            <div id="guest-content">
                <decorator:body />
            </div>

            <div id="footer-navigation">
                <%if(!request.getServletPath().equals("/home")){%>
                    <a href="/z/home" class="href-dotted">Home</a>
                <%}%>
                <%if(!request.getServletPath().equals("/towns")){%>
                &nbsp;&nbsp;
                <a href="/z/signin" class="href-dotted">Signin</a>&nbsp;&nbsp;
                <a href="https://github.com/mcroteau/Dynamics" class="href-dotted" target="_blank">Source Code</a>
                <%}%>
            </div>

            <div id="footer-wrapper">
                <span class="regular">&copy; 2021 <br/>Dynamics<br/> <strong class="gain">+Gain</strong></span>
            </div>

            <style>
                #guest-content{
                    margin-top:30px;
                }
                #footer-navigation{
                    text-align:center;
                    margin-top:70px;
                }
                #footer-wrapper{
                    margin:103px auto 200px;
                    text-align: center;
                }

            </style>

        </div>

    </div>
</div>

</body>
</html>