package dynamics.gain.model;

import java.util.ArrayList;
import java.util.List;

public class Location {

    long id;
    long townId;

    String name;
    String location;
    String geo;

    String stripePublishable;
    String stripeSecret;

    DailyCount count;
    List<User> accounts = new ArrayList<>();
    List<DailyCount> counts = new ArrayList<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTownId() {
        return townId;
    }

    public void setTownId(long townId) {
        this.townId = townId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getStripePublishable() {
        return stripePublishable;
    }

    public void setStripePublishable(String stripePublishable) {
        this.stripePublishable = stripePublishable;
    }

    public String getStripeSecret() {
        return stripeSecret;
    }

    public void setStripeSecret(String stripeSecret) {
        this.stripeSecret = stripeSecret;
    }

    public DailyCount getCount() {
        return count;
    }

    public void setCount(DailyCount count) {
        this.count = count;
    }

    public List<User> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<User> accounts) {
        this.accounts = accounts;
    }

    public List<DailyCount> getCounts() {
        return counts;
    }

    public void setCounts(List<DailyCount> counts) {
        this.counts = counts;
    }
}
