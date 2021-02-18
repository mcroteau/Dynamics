package dynamics.gain.model;

import java.util.List;

public class Town {

    long id;
    String name;
    String townUri;
    Integer count;

    List<Location> locations;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTownUri() {
        return townUri;
    }

    public void setTownUri(String townUri) {
        this.townUri = townUri;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
