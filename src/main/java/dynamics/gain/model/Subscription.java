package dynamics.gain.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Subscription {

    Long id;
    String stripeId;
    BigDecimal amount;
    Location location;
    boolean cancelled;
    String donationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountZero(){
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }
}






