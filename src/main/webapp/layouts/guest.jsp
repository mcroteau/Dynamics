<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Okay! Website Monitoring</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/o/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/o/assets/css/pastel.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/o/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/o/assets/js/packages/jquery.js"></script>

</head>
<body>

<div id="outer-wrapper">

    <div id="header-wrapper">
        <a href="/o" class="logo logo-basic">Okay<span class="apostrophe">&check;</span>
            <span id="tagline">Website Monitoring</span>
        </a>

        <div id="navigation">
            <a href="/o/pricing" class="href-dotted">Pricing</a>
            <a href="/o/signin" class="href-dotted">Signin</a>
        </div>
        <br class="clear"/>
    </div>

    <div id="content-wrapper">
        <decorator:body />
    </div>

    <div id="footer-wrapper">
        &copy; 2021 Okay!
    </div>

    <style>
        #header-wrapper{
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