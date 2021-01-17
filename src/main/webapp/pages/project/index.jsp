<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Okay! ${project.name}</title>
</head>
<body>

<style>
    #project-page-wrapper{
        text-align: center;
        padding:10px 20px 30px;
        background: #f7f9f9;
        border:solid 0px #C9DCDC;
        border-radius: 20px;
    }
    #project-page-actions{
        margin-top:30px;
        text-align: right;
    }
    .project-stats{
        width:100%;
        margin:20px 0px 0px;
    }
    .project-stats th{
        text-align: right;
        font-family: roboto-light !important;
    }
    .project-stats td{
        text-align: left;
        font-family: roboto !important;
    }
    .project-stats th,
    .project-stats td{
        width:calc(46%);
        padding:13px 5px;
        font-size:23px;
        vertical-align: middle;
    }
    #project-phones-list{
        text-align: center;
    }
    #project-phones-list h3{
        color:#1e4f4f;
        color:#000;
        margin:30px 0px 10px;
    }
    #project-phones-list form{
        font-size:25px;
        font-family: roboto-slab !important;
    }
    label{
        color: #3e464a;
        font-size: 19px;
        vertical-align: bottom;
        font-family: roboto-light !important;
    }
    label span{
        font-size:23px;
        vertical-align: bottom;
        font-family: roboto-bold !important;
    }
</style>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <div id="project-page-wrapper">

        <h1>${project.name}
            <c:if test="${project.status == '200'}">&check;</c:if>
        </h1>
        <p class="regular">${project.uri}
            <a href="/o/project/edit/${project.id}" class="href-dotted">Edit</a></p>


        <c:if test="${!project.initial}">
            <table class="project-stats">
                <tr>
                    <th>Avg Resp:</th>
                    <td>${project.avgResp}</td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>
                        <span class="ok">${project.status}</span>
                        <c:if test="${project.status == '200'}"> <span class="bold">Ok</span></c:if>
                    </td>
                </tr>
                <tr>
                    <th>Percent Uptime:</th>
                    <td>${project.percentUptime}%</td>
                </tr>
                <tr>
                    <th>Last Monitored:</th>
                    <td>${project.prettyTime}&nbsp;${project.actualDate}</td>
                </tr>
            </table>
        </c:if>

        <c:if test="${project.initial}">
            <table class="project-stats">
                <tr>
                    <th>Avg Resp:</th>
                    <td><span class="ok not" style="font-size: 12px;">gathering data...</span></td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td></td>
                </tr>
                <tr>
                    <th>Percent Uptime:</th>
                    <td></td>
                </tr>
                <tr>
                    <th>Last Monitored:</th>
                    <td></td>
                </tr>
            </table>
        </c:if>

        <div id="project-phones-list">
            <h3>Contact Phones</h3>
            <ul>
                <c:forEach var="projectPhone" items="${project.projectPhones}">
                    <li>
                        <form action="/o/project/phone/delete/${projectPhone.id}" method="post">
                            ${projectPhone.phone}
                            <input type="submit" value="&nbsp;-&nbsp;" class="button remoe tiny" onclick="return confirm('Are you sure you want to delete Phone?');" />
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div id="project-page-actions">
            <a href="/o/project/phone/add/${project.id}" class="href-dotted">+ Add Phone</a>
        </div>
    </div>



<script>
    $(document).ready(function(){

        var g = {},
            data = [],
            processing = false,
            pings = 0;

        var $pingDate = $("#ping-date"),
            $projectStatus = $("#project-status"),
            $projectResponseTime = $("#project-response"),
            $projectUptime = $("#project-uptime"),
            $downStatus = $("#down-status"),
            $projectName = $("#project-name")

        // var pingInterval = setInterval(ping, 1000);


        function ping(){
            if(!processing){
                processing = true
                $.ajax({
                    method:'post',
                    url : "/o/project/validate?uri=${project.uri}",
                    success : success,
                    error: error
                })
            }
        }


        function success(resp, n){
            var data = JSON.parse(resp);

            $projectStatus.html(data.status + (data.status == "200" ? " Ok" : ""))
            $projectResponseTime.html(data.responseTime)


            if(data.statusCode != 200){
                $downStatus.show()
                $projectName.addClass("down")
            }

            if(data.StatusCode == 200){
                $downStatus.hide()
                $projectName.removeClass("down")
            }

            processing = false
        }

        function error(){
            console.log("error...")
            processing = false
        }

        function clean() {
            clearInterval(pingInterval);
        }

    });
</script>

</body>
</html>
