<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Okay! Monitor Project</title>

    <style type="text/css">
        #monitor-project-container{
            width:775px;
        }
        .button.disabled{
            opacity:0.4 !important;
        }
    </style>

</head>
<body>

<div id="monitor-project-container">


    <h1>Add Website</h1>

    <p>Okay! requires a <strong>200</strong>
        response before processing, ensure that the URL contains no redirects
        and starts with either <strong>http</strong> or <strong>https</strong>.</p>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>


    <form action="/o/project/save" method="post">

        <div class="form-group">
            <label for="name" class="nofloat">Name</label>
            <input type="text" name="name" id="name" value="" placeholder="Example Project" style="width:70%">
        </div>


        <div class="form-group">
            <label for="name">Url</label>
            <input type="text" name="uri" id="uri" value="" placeholder="http://sample.io" style="width:100%"><br/>
            <span style="display:block;margin-top:20px;font-size:14px;">Status :
                <span id="status" style="font-size:19px;"></span>
                <span id="processing" style="display:none">Processing...</span>
                <span id="success" style="display:none">Good to start monitoring</span>
            </span>
            <br class="clear"/>
        </div>

        <input type="submit" id="submit" name="submit" value="Start Monitoring Website" class="button disabled" disabled="disabled"/>
    </form>

</div>

<script type="text/javascript">
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
    });
</script>
</body>
</html>
