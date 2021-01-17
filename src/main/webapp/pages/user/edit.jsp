<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="dynamics.gain.common.Constants" %>

<div id="edit-user-form">

	<style type="text/css">
        @media screen and (max-width: 590px) {
            body{
                padding:20px !important;
            }
            #guest-content-container{
                width:100% !important;
            }
            input[type="text"],
            input[type="password"]{
                width:80% !important;
            }
            #guest-content-right{
                padding:0px;
            }
        }
	    #guest-content-container{
	        width:590px;
	    }
	    #amadeus-logo-container{
            display:none;
	    }
	    #guest-content-right{
	        float:none;
	        margin:0px auto !important;
	        width:100% !important;
	        text-align:left !important;
	    }
	    #guest-header{
	    }
		#profile-image{
		    text-align:center;
		}
		#profile-image img{
			height:201px;
			width:201px;
			border-radius: 201px;
			-moz-border-radius: 203px;
			-webkit-border-radius: 203px;
		}
        #edit-actions-container{
            margin:30px auto 30px auto;
        }
        #edit-actions-container a{
            display:inline-block;
            margin-top:30px;
        }
        #delete-user-container{
            margin:0px auto 10px auto;
        }
        #suspend-actions-container{
            margin:0px auto 200px auto;
        }
        label{
            font-size:19px;
            display:block;
            color:#2b2b34;
            margin:10px auto 0px auto;
        }
        input[type="text"],
        input[type="password"]{
            width:100% !important;
        }
        .button,
        .retro,
        input[type="submit"]{
            width:100%;
            display:block;
            -webkit-box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
            -moz-box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
            box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
        }
        .button:hover,
        .retro:hover,
        .yella:hover{
            -webkit-box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
            -moz-box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
            box-shadow: 0px 1px 7px 0px rgba(0,0,0,0) !important;
        }
        h2{
            margin-top:20px;
        }
        .notify{
            line-height:1.4em;
        }
	</style>


	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

	<div id="edit-user-container">

        <h1 class="regular">${user.username}</h1>

        <%if(Parakeet.hasRole(Constants.ROLE_ADMIN)){%>
            <form action="${pageContext.request.contextPath}/user/delete/${user.id}" method="post">
                <div id="delete-user-container" style="width:100%;">
                    <input type="submit" class="button remove" id="disable" value="Delete Account" style="width:100% !important;"/>
                </div>
            </form>
        <%}%>
    </div>
</div>
	
		
