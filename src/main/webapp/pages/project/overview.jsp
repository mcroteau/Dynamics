<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Okay! Overview</title>
</head>
<body>

<c:if test="${not empty message}">
    <div class="notify">${message}</div>
</c:if>

<style>
    #projects-list{
        margin:30px auto 100px;
    }
    .project-wrapper{
        clear: both;
        display:block;
        padding:10px 10px;
        text-decoration: none;
        vertical-align: middle;
        margin:3px 0px;
        border-radius: 13px;
        background:#f7f9f9;
        border:solid 0px #C9DCDC
    }
    .project-wrapper:hover{
        background:#FFF8E9;
    }
    .project-wrapper.down{
        background:#CE6A6A;
    }
    .project-wrapper.down:hover{
        background: #e27e7e;
    }
    .project-wrapper.down .project-name,
    .project-wrapper.down .project-name span,
    .project-wrapper.down .project-time,
    .project-wrapper.down .project-stats{
        color: #ffbbbb;
    }
    .project-wrapper.down .operational{
        font-size:12px;
        color:#fff !important;
        font-family: roboto-black !important;
    }
    .project-name{
        float:left;
        width:50%;
        font-size:39px;
        font-family:roboto-bold !important;
    }
    .project-name span{
        display:block;
        font-size:12px;
    }
    .project-time{
        font-size:12px;
    }
    .project-stats{
        float:right;
    }
    .project-stats label{
        display:block;
        margin:0;
        padding:0;
        line-height: 1.0;
    }
</style>

<c:if test="${projects.size() > 0}">
    <div id="projects-list">
        <c:forEach var="project" items="${projects}">

            <c:set var="operational" value=""/>
            <c:if test="${project.status != '200' && project.status != 'getting data...'}">
                <c:set var="operational" value="down"/>
            </c:if>

            <a href="/o/project/${project.id}" class="project-wrapper ${operational}">
                <div class="project-name">
                        ${project.name}<c:if test="${project.status == '200'}">&check;</c:if>

                    <span>${project.uri}</span>

                    <c:if test="${operational == 'down'}">
                        <span class="operational">Down!</span>
                    </c:if>
                </div>
                <div class="project-stats">
                    <label>Status :
                            <c:if test="${project.status == '200'}">
                                <span class="ok">${project.status}</span> <span class="bold">Ok</span>
                            </c:if>
                            <c:if test="${project.status != '200'}">
                                <span class="ok not">${project.status}</span>
                            </c:if>
                    </label>
                    <label>Avg Resp :
                            <c:if test="${project.initial}">
                                -
                            </c:if>
                            <c:if test="${!project.initial}">
                                ${project.avgResp}
                            </c:if>
                    </label>
                    <div class="project-time">
                        ${project.prettyTime}&nbsp;${project.actualDate}
                    </div>
                </div>
                <br class="clear"/>
            </a>
        </c:forEach>
    </div>
</c:if>
<c:if test="${projects.size() == 0}">
    <p>No web sites added to your portfolio yet!&nbsp;
        <a href="/o/project/create" class="href-dotted">+ Website</a>
    </p>
</c:if>
</body>
</html>
