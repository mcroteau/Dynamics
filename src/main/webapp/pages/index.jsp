<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : An attempt to remove barriers that prevent those wanting to help.</title>
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

        <h1 id="welcome-text">
            ${count} <span>Homeless <span class="header-information">counted in ${towns.size()} Cities!</span></span>
        </h1>

        <p class="open-text regular" style="margin-bottom:10px;">Dynamics <strong>+Gain</strong> is a non profit designed with
            the sole purpose of removing barriers that prevent people from
            giving time, money and resources to those in need! Our goal is to
            set up Kiosks with near real time homeless data included.</p>

        <div style="margin:0px auto 30px;">
            <a href="/z/donate" class="button super yellow">Give &hearts;</a>
        </div>

        <h3>Where your donations go</h3>
        <p>100% of your donations go towards Dynamics +Gain and the people
            they serve. A few major ways in which we give include
            Walmart Gift Cards, Prepaid Cellphones, Laptops and bus passes.</p>

        <h3>Services we provide</h3>

        <p>As a non-profile, in addition to fundraising, we provide custom software services
        to the private and public sector.</p>


        <h3>Towns/Cities</h3>
        <ul>
            <c:forEach var="town" items="${towns}">
                <li style="padding:1px 0px;"><a href="/z/town/${town.townUri}">${town.name}</a></li>
            </c:forEach>
        </ul>

    </div>

</body>