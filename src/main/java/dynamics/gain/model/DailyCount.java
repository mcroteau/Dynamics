package dynamics.gain.model;

public class DailyCount {

    long id;
    long locationId;
    long accountId;
    int count;

    String notes;
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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(long dateEntered) {
        this.dateEntered = dateEntered;
    }

}
