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

        <p id="welcome-text">We make sure your site is up
            and running 24/7 so you can focus
            on what's important...
        </p>

        <form action="/o/signmeup" id="signup" method="post" modelAttribute="signMeUp">
            <p id="home-signup">
                Okay, please monitor my<br/>project @
                <input type="text" name="uri" id="uri" placeholder="offtoworkwego.io"><span id="success" style="display:none">&check;</span> using
                <br/>email
                <input type="text" name="username" placeholder="snowhite@mail.com">
                and <br/>password
                <input type="text" name="password" placeholder="password">!
                <input type="submit" href="/o/signup" class="button yellow signup-homepage" value="Start Monitoring!">
            </p>
        </form>


        <p class="open-text">Okay! runs 24/7 lightly touching your project
            to ensure 200 responses and will alert you via
            SMS Text Message you ever there isn't.</p>

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

    <script>


        $(document).ready(function(){
            var $uri = $("#uri"),
                $status = $("#status"),
                $processing = $("#processing"),
                $success = $("#success"),
                $submit = $("#submit"),
                processing = false

            $uri.keyup(function(){
                validateUri()
            })

            function validateUri(){
                processing = true
                $processing.show()
                $.ajax({
                    method: "post",
                    url : "/o/project/validate?uri=" + $uri.val(),
                    success : success,
                    error : error
                })
            }

            function success(resp, xhr){
                var data = JSON.parse(resp);
                $status.html(data.status)
                if(data.status == 200){
                    console.log($submit)
                    $success.show()
                    $submit.attr("disabled", false).removeClass("disabled")
                }
                if(data.status != 200){
                    $success.hide()
                    $submit.attr("disabled", true).addClass("disabled")
                }

                $processing.hide()
                processing = false
            }

            function error(error, xhr){
                //console.log(error, xhr)
                $status.html(error.status)
                $processing.hide()
                processing = false
                $submit.attr("disabled", true)
            }

            $("body").on("submit", "#signup", function() {
                $(this).submit(function() {
                    return false;
                });
                return true;
            });
        });

    </script>
</body>