<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Okay! : Website Monitoring</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/o/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
	<link rel="stylesheet" href="/o/assets/css/pastel.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/o/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/o/assets/js/packages/jquery.js"></script>

</head>
<body>

<div id="outer-wrapper">

    <div id="header-wrapper">
        <a href="/o" class="logo">Okay<span class="black apostrophe">&check;</span>
            <span id="tagline">Website Monitoring</span>
        </a>

        <div id="navigation">
            <span id="welcome">Hello <a href="/o/user/edit/${sessionScope.userId}" class="href-dotted-black zero"><strong>${sessionScope.username}</strong></a>!
            </span>
            <a href="/o/project/overview" class="href-dotted">Overview</a>
            <a href="/o/project/create" class="href-dotted">Add Website</a>
        </div>
        <br class="clear"/>
    </div>

    <div id="content-wrapper">
        <decorator:body />
    </div>

    <div id="footer-wrapper">
        <a href="/o/signout" class="href-dotted">Signout</a>
    </div>

</div>

</body>
</html>