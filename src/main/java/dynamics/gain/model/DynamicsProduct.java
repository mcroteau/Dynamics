package dynamics.gain.model;

public class DynamicsProduct {

    Long id;
    String stripeId;
    String nickname;
    String stripeType = "service";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getStripeType() {
        return stripeType;
    }

    public void setStripeType(String stripeType) {
        this.stripeType = stripeType;
    }
}