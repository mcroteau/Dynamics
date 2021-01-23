package dynamics.gain.model;


public class User {

	long id;
	String uuid;
	String username;
	String password;
	long dateCreated;

	Long planId;
	String stripeUserId;
	String stripeChargeId;
	String stripeSubscriptionId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStripeUserId() {
		return stripeUserId;
	}

	public void setStripeUserId(String stripeUserId) {
		this.stripeUserId = stripeUserId;
	}

	public String getStripeChargeId() {
		return stripeChargeId;
	}

	public void setStripeChargeId(String stripeChargeId) {
		this.stripeChargeId = stripeChargeId;
	}

	public String getStripeSubscriptionId() {
		return stripeSubscriptionId;
	}

	public void setStripeSubscriptionId(String stripeSubscriptionId) {
		this.stripeSubscriptionId = stripeSubscriptionId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

}

