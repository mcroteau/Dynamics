package dynamics.gain.model;

public class DailyCount {

    long id;
    long locationId;
    long userId;
    int count;
    long dateEntered;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(long dateEntered) {
        this.dateEntered = dateEntered;
    }

}
