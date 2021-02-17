package dynamics.gain.model;

import java.math.BigDecimal;

public class Stats {

    Long donorsCount;

    Long count;
    BigDecimal total;

    Long monthlyCount;
    BigDecimal monthlyTotal;

    public Long getDonorsCount() {
        return donorsCount;
    }

    public void setDonorsCount(Long donorsCount) {
        this.donorsCount = donorsCount;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(Long monthlyCount) {
        this.monthlyCount = monthlyCount;
    }

    public BigDecimal getMonthlyTotal() {
        return monthlyTotal;
    }

    public void setMonthlyTotal(BigDecimal monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }
}
