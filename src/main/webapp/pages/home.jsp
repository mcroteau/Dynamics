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

    <p class="open-text regular" style="margin-bottom:10px;">Dynamics <strong class="gain">+Gain</strong> is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need! Our goal is to
        set up Booths with near real time homeless data displayed
        everywhere you go in addition a virtual gateway allowing people
        the ability to accept donations on behalf of their own Charity
        Organization.
    </p>

    <h3>Where your donations go</h3>
    <p>A few major ways in which we would like to give include
        Walmart Gift Cards, Prepaid Cellphones, Laptops and Bus Passes
        and sometimes just plain old money!</p>

    <h3>Services we provide</h3>
    <p>As a non-profile, in addition to fundraising, we provide our
        donation platform as a service, all you need your Stripe key and
        a little javascript know-how... we also provide custom
        software services to the private and public sector.</p>

    <h3>Interested in setting up your own Charitable Organization?</h3>
    <p>We are here to help. You can either take our codebase and modify it
    to fit your needs or use our donation api for free. All you need is a Stripe
    account. Here is how to user our <a href="/setup" class="href-dotted">donation platform.</a>
    For access to Dynamics +Gain source code, the code that runs this site
    and the API.
        <a href="https://github.com/mcroteau/Dynamics">https://github.com/mcroteau/Dynamics</a></p>

    <h3>Towns/Cities</h3>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:1px 0px;"><a href="/z/town/${town.townUri}">${town.name}</a></li>
        </c:forEach>
    </ul>

</div>

</body>