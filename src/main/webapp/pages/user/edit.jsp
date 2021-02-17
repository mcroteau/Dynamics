<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="dynamics.gain.common.Constants" %>

    <style>
        #message{
            display:none;
        }
    </style>

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

    <h1>Your Profile</h1>
    <p>Below are your contribution details.</p>

    <c:if test="${subscription != null}">
        <div id="subscription-details">
            <h3 class="bold">Subscription Details</h3>

            <p>$${subscriptionAmount} billed monthly</p>

<%--            <input type="submit" class="button small beauty" value="Cancel" id="cancel"/>--%>
        </div>
        <p id="message" class="notify">Processing... please wait.</p>
    </c:if>

    <c:if test="${chargeAmount != null}">
        <h3>One-Time Donation</h3>
        <h1>$${chargeAmount}</h1>
    </c:if>

    <c:if test="${chargeAmount == null && subscription == null}">
        <p>No current donations.</p>
        <p><a href="/z/donate" class="href-dotted">Donate</a></p>
    </c:if>

    <p>Thank you for being a contributor.<br/>
        Contact us any time if you have questions.</p>

    <a href="/z/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>

<script>
    $(document).ready(function(){
        var processing = false

        var $cancelBtn = $('#cancel'),
            $messageDiv = $('#message'),
            $subscriptionDiv = $('#subscription-details');

        $cancelBtn.click(function(evnt){
            evnt.preventDefault();
            if(!processing){
                $messageDiv.show();
                $.ajax({
                    url : "/z/donate/cancel/${user.stripeSubscriptionId}",
                    method: "delete",
                    success: function(raw){
                        var resp = JSON.parse(raw)
                        console.log(resp)
                        if(resp == "success") {
                            $messageDiv.html("Successfully processed cancellation.")
                            $subscriptionDiv.hide()
                        }
                        if(resp != "success") {
                            $messageDiv.html("Something went wrong, looks like we are going to have to process manually.")
                        }
                    },
                    error : function(){
                        console.log("something went wrong.")
                        $messageDiv.html("Something went wrong, looks like we are going to have to process manually.")
                    }
                })
            }
        })
    })
</script>
	
		
