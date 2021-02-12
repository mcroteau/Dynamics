<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="dynamics.gain.common.Constants" %>

<div id="edit-user-form">

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

	<div id="edit-user-container">

        <h1>Profile</h1>
        <p>Thank you for being a contributor. Below are your contribution details.</p>
        <c:if test="${subscription != null}">
            <p class="bold">Subscription Details</p>
            <table>
                <tr>
                    <th style="text-align: right">Amount</th>
                    <td style="text-align: left">$${subscriptionAmount}</td>
                </tr>
                <tr>
                    <th style="text-align: right">Frequency</th>
                    <td style="text-align: left">Month</td>
                </tr>
            </table>
            <form action="/z/donate/cancel" modelAttribute="user">
                <input type="hidden" name="id" value="${user.id}"/>
                <input type="hidden" name="stripeSubscriptionId" value="${user.stripeSubscriptionId}"/>
                <input type="submit" class="button small beauty" value="Cancel"/>
            </form>
        </c:if>
    </div>
</div>
	
		
