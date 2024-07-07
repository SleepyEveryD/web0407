package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group {
    int groupId;
    String title ;
    String creator;
    int duration;
    int maxPeople;
    int minPeople;
    Date date_creation = java.sql.Date.valueOf(java.time.LocalDate.now());

    List<User> members= new ArrayList<>();

    public Group(String groupName, User creator, int minMembers, int maxMembers, int duration) {
        this.title = groupName;
        this.creator = creator.getName();
        this.minPeople = minMembers;
        this.maxPeople = maxMembers;
        this.duration = duration;
        this.members = new ArrayList<>();

    }

    public Group() {

    }

    public Group(int groupId, String title, int maxPeople, int minPeople, int durationDays, java.sql.Date dateCreation) {
        this.groupId = groupId;
        this.title = title;
        this.maxPeople = maxPeople;
        this.minPeople = minPeople;
        this.duration = durationDays;
        this.date_creation = dateCreation;
    }

    public String getCreator() {
        return creator;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(int minPeople) {
        this.minPeople = minPeople;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
