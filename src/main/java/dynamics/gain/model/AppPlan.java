package dynamics.gain.model;

public class AppPlan {

    int id;
    String stripeId;
    int okayProductId;

    int amount;
    String nickname;
    String description;
    String frequency;
    String currency;

    int projectLimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public int getOkayProductId() {
        return okayProductId;
    }

    public void setOkayProductId(int okayProductId) {
        this.okayProductId = okayProductId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getProjectLimit() {
        return projectLimit;
    }

    public void setProjectLimit(int projectLimit) {
        this.projectLimit = projectLimit;
    }
}
