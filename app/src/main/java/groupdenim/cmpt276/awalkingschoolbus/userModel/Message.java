package groupdenim.cmpt276.awalkingschoolbus.userModel;

/**
 * Created by Farhan on 2018-03-21.
 */

public class Message {
    private long id;
    private String timestamp;
    private String text;
    private User fromUser;
    private Group toGroup;
    private boolean emergency;
    private String href;

    public Message(long id, String timestamp, String text, User fromUser, Group toGroup, boolean emergency, String href) {

        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.fromUser = fromUser;
        this.toGroup = toGroup;
        this.emergency = emergency;
        this.href = href;
    }

    public Message() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Group getToGroup() {
        return toGroup;
    }

    public void setToGroup(Group toGroup) {
        this.toGroup = toGroup;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Id:" + id + ", "
                + "TimeStamp: " + timestamp + ", "
                + "Text: " + text + ", "
                + "User: " + fromUser.toString() + ", "
                + "Group: " + toGroup + ", "
                + "Emergency: " + emergency + ", "
                + "href: " + href;
    }
}
