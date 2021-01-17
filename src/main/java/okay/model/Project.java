package okay.model;

import java.util.List;

public class Project {

    long id;
    long userId;
    String name;
    String uri;
    String status;
    int statusCode;
    double avgResp;
    boolean initial;
    boolean disabled;
    String prettyTime;
    String actualDate;
    double percentUptime;
    List<ProjectPhone> projectPhones;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getAvgResp() {
        return avgResp;
    }

    public void setAvgResp(double avgResp) {
        this.avgResp = avgResp;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPrettyTime() {
        return prettyTime;
    }

    public void setPrettyTime(String prettyTime) {
        this.prettyTime = prettyTime;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public double getPercentUptime() {
        return percentUptime;
    }

    public void setPercentUptime(double percentUptime) {
        this.percentUptime = percentUptime;
    }

    public List<ProjectPhone> getProjectPhones() {
        return projectPhones;
    }

    public void setProjectPhones(List<ProjectPhone> projectPhones) {
        this.projectPhones = projectPhones;
    }
}
