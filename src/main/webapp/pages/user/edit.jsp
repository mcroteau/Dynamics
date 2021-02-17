<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="dynamics.gain.common.Constants" %>

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

    <h1>Your Profile</h1>
    <p>Below are your contribution details.</p>

    <c:if test="${subscriptions.size() > 0}">
        <div id="subscription-details">
            <h3>Subscriptions</h3>

            <c:forEach var="subscription" items="${subscriptions}">
                <p><strong>$${subscription.amount}</strong> monthly to
                        <c:if test="${subscription.location != null}">
                            ${subscription.location.name}
                        </c:if>
                        <c:if test="${subscription.location == null}">
                            Dynamics <strong>+Gain</strong>
                        </c:if>
                </p>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${charges.size() > 0}">
        <h3 style="margin-top:30px;">One-Time Donations</h3>
        <c:forEach var="charge" items="${charges}">
            <p><strong>$${charge.amount}</strong> donated to
                <c:if test="${charge.location != null}">
                    ${charge.location.name}
                </c:if>
                <c:if test="${charge.location == null}">
                    Dynamics <strong>+Gain</strong>
                </c:if>
            </p>
        </c:forEach>
    </c:if>

    <c:if test="${charges.size() == 0 && subscriptions.size() == 0}">
        <p>No current donations.</p>
        <p><a href="/z/donate" class="href-dotted">Donate</a></p>
    </c:if>


    <c:if test="${charges.size() > 0 || subscriptions.size() > 0}">
        <p>Thank you for being a contributor.<br/>
        Contact us any time if you have questions.</p>
    </c:if>


    <a href="/z/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>
	
		
