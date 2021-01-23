package dynamics.gain.model;

import dynamics.gain.common.Utils;

import java.math.BigDecimal;

public class Donation {

    BigDecimal amount;
    String email;

    String creditCard;
    Integer expMonth;
    Integer expYear;
    String cvc;
    boolean recurring;

    String donorId;
    String subscriptionId;

    String status;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid(){
        if(!Utils.isValidMailbox(this.getEmail())){
            this.setStatus("Email is invalid, please try again!");
            return false;
        }
        if(this.getCreditCard().equals("")){
            this.setStatus("Credit card is empty, please try again");
            return false;
        }
        if(this.getExpMonth() == null){
            this.setStatus("Expiration month is empty, please try again");
            return false;
        }
        if(this.getExpYear() == null){
            this.setStatus("Expiration year is empty, please try again");
            return false;
        }
        if(this.getCvc().equals("")){
            this.setStatus("Cvc is empty! please try again.");
            return false;
        }
        return true;
    }
}
