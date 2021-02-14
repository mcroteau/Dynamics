package dynamics.gain.model;

import java.util.ArrayList;
import java.util.List;

public class Location {

    long id;
    long userId;
    long townId;
    Town town;
    String name;
    String needs;
    int count;

    String locationUri;
    String description;

    String stripePublishable;
    String stripeSecret;

    List<User> accounts = new ArrayList<>();
    List<DailyCount> counts = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTownId() {
        return townId;
    }

    public void setTownId(long townId) {
        this.townId = townId;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocationUri() {
        return locationUri;
    }

    public void setLocationUri(String locationUri) {
        this.locationUri = locationUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
