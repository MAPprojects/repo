package Domain;

import Repository.HasID;

import java.sql.Date;
import java.util.List;

public class Event implements HasID<Integer> {

    private Integer ID;
    private String Name;
    private Date EventDate;
    private Date Deadline;
    private String Details;
    private List<Integer> userIds;

    public Event(Integer id, String name, Date eventDate, Date deadline, String details, List<Integer> user_list) {
        Name = name;
        EventDate = eventDate;
        Deadline = deadline;
        Details = details;
        userIds = user_list;
        setId(id);
    }

    @Override
    public Integer getId() {
        return this.ID;
    }

    @Override
    public void setId(Integer id) {
        this.ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getEventDate() {
        return EventDate;
    }

    public void setEventDate(Date eventDate) {
        EventDate = eventDate;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        Deadline = deadline;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!Event.class.isAssignableFrom(obj.getClass()))
            return false;
        final Event other = (Event) obj;
        return this.getId().equals(other.getId());
    }
}
