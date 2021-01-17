<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain : An attempt to fix Homelessness </title>
</head>
<body>
    <div id="homepage-wrapper">

        <c:if test="${not empty message}">
            <p class="notify">${message}</p>
        </c:if>

        <p id="welcome-text">

        </p>

        <p class="open-text"></p>

        <p class="open-text">Okay! Website Monitoring made easy &amp; Rock Solid!</p>

        <p>We keep it simple.</p>

        <style>
            #home-signup{
                font-size: 32px;
                line-height: 1.7em !important;
                margin:30px 0px 20px !important;
            }
            #home-signup input[type="text"],
            #home-signup input[type="text"]:focus{
                border:none !important;
                background: #eff4f4;
                outline: none;
                font-size:23px;
                border-bottom:solid 1px #ddd !important;
            }
            #home-signup input[type="text"]::placeholder{
                font-size:23px;
                color:#4a6d6d;
            }
            #homepage-wrapper p{
                color:#fff;
                line-height: 1.4em;
                margin:10px 0px;
            }
            #welcome-text{
                line-height: 1.1em !important;
                font-size:42px;
                width:60%;
            }
            #welcome-text span{
                font-size:19px;
                display: block;
            }
            .signup-homepage{
                margin:13px 0px 20px;
                -webkit-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
                -moz-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
                box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
            }
            .open-text{
                width:70%;
            }

            @media screen and (max-width: 690px) {
                #welcome-text,
                .open-text{
                    width:calc(100% - 40px);
                }
            }
        </style>
    </div>

</body>