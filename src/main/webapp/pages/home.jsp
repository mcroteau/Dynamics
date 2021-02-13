<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : Removing barriers that prevent those wanting to help!.</title>
</head>
<body>
<style>
    #homepage-wrapper p{
        text-align: left;
    }
    h3{
        margin:30px auto 3px ;
    }
</style>
<div id="homepage-wrapper">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>

    <h1 id="welcome-text" class="">
        ${count} <span>Homeless <span class="header-information">counted in ${towns.size()} Cities!</span></span>
    </h1>

    <div style="margin:0px auto 30px;">
        <a href="/z/donate" class="button super yellow">Give &hearts;</a>
    </div>

    <p class="open-text" style="margin-bottom:10px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Dynamics <strong class="gain">+Gain</strong>
        is a charitable organization designed to removing barriers
        that prevent people from giving time, money and resources to those
        in need! Our goal is to set up Booths with near real time homeless
        data displayed allowing people to stay up to date on the reality of
        homelessness in their community.
    </p>

    <h3>Where your donations go</h3>
    <p>We will work closely local homeless shelters to learn of
        the needs of others, but majority of your donations will go
        towards Walmart Gift Cards, Prepaid Cellphones, Laptops &
        funding towards local community housing.
    </p>

    <h3>Services we provide</h3>
    <p>As a non-profile, in addition to fundraising, we provide our
        donation platform as a service, all you need your Stripe key and
        a little javascript know-how... we also provide custom
        software services to the private and public sector.</p>


    <h3>Towns/Cities</h3>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:1px 0px;"><a href="/z/towns/${town.townUri}" class="href-dotted">${town.name}</a></li>
        </c:forEach>
    </ul>


</div>

</body>