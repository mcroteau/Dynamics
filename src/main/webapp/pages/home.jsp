<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : The Goal is to remove barriers.</title>
</head>
<body>
<style>
    #homepage-wrapper p{
        text-align: left;
    }
    h3{
        margin:30px auto 3px ;
    }
    #welcome-text{
        margin-bottom:10px;
    }
</style>
<div id="homepage-wrapper">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>

    <p style="text-align: center">Please help!</p>

    <h1 id="welcome-text" class="help">
        ${count} <span>Homeless <span class="header-information"> in
            &nbsp;<a href="/z/locations" class="counts href-dotted-black">${towns.size()}</a>&nbsp;
            <c:if test="${towns.size() == 1}">City!</c:if>
            <c:if test="${towns.size() > 1}">Cities!</c:if>
            </span></span>
    </h1>

    <div style="margin:0px auto 30px;">
        <a href="/z/locations" class="button super yellow">Give &hearts;</a>
    </div>

    <p class="open-text" style="margin-bottom:10px;">
        Dynamics <strong class="gain">+Gain</strong>
        is an organization designed to remove barriers
        that prevent people from giving time, money and resources to those
        in need! Our goal is to set up Booths with near real time homeless
        data displayed allowing people to stay up to date on the reality of
        homelessness in their community.
    </p>


    <h3 style="margin-top:20px;">How does it work?</h3>
    <p>Kiosks/booths would be set up at your local community shopping centers
    where they would display the number of people in need in your community, and
    in addition allowing you to donate right there on the spot.
        100% of your donation goes toward the organization you select
        or to Dynamics <strong>+Gain</strong>, your choice. The organization
        has signed up to be on our list and has gone through the
        registration process to link their account.
        </p>


    <h3>What does Dynamics <strong>+Gain do?</h3>
    <p>We will work closely
        local homeless shelters to learn of their needs,
        but majority of your donations will go
        towards funding for local Community Housing, Walmart Gift Cards &amp;
        Prepaid Cellphones.
    </p>

    <h3 id="towns">Towns/Cities</h3>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:4px 0px;"><a href="/z/towns/${town.townUri}" class="href-dotted" style="font-size:27px;">${town.name}</a></li>
        </c:forEach>
    </ul>

</div>

</body>