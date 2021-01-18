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
            ${count} <span>Homeless!</span>
        </p>

        <p class="open-text">Dynamics +Gain is a non profit designed with
            the sole purpose of removing the barriers that make it difficult
            to help.</p>

        <p class="open-text"></p>

        <style>
            #welcome-text{
                line-height: 1.1em !important;
                font-size:92px;
                width:60%;
                margin:0px;
            }
            #welcome-text span{
                font-size:62px;
                display: block;
                margin:0px;
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