package groupdenim.cmpt276.awalkingschoolbus.userModel;

import java.util.List;

/**
 * Created by Farhan on 2018-04-04.
 */

public class PermissionRequest {
    private long id;
    private String action;
    private PermissionStatus status;
    private User userA;
    private User userB;
    private Group groupG;
    private User requestingUser;
    private List<PermissionRecord> authorizors;
    private String message;
    private String href;

    public PermissionRequest() {}

    public PermissionRequest(long id, String action, PermissionStatus status, User userA, User userB, Group groupG, User requestingUser, List<PermissionRecord> authorizors, String message, String href) {
        this.id = id;
        this.action = action;
        this.status = status;
        this.userA = userA;
        this.userB = userB;
        this.groupG = groupG;
        this.requestingUser = requestingUser;
        this.authorizors = authorizors;
        this.message = message;
        this.href = href;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(PermissionStatus status) {
        this.status = status;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public Group getGroupG() {
        return groupG;
    }

    public void setGroupG(Group groupG) {
        this.groupG = groupG;
    }

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public List<PermissionRecord> getAuthorizors() {
        return authorizors;
    }

    public void setAuthorizors(List<PermissionRecord> authorizors) {
        this.authorizors = authorizors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        String toReturn = "{ \n id: " + id + ","
                + "\naction: " + action + ","
                + "\nstatus: " + status + ","
                + "\nuserA: " + userA + ","
                + "\nuserB: " + userB + ","
                + "\nstatus: " + status + ","
                + "\ngroupG: " + groupG + ","
                + "\nrequestingUser: " + requestingUser + ","
                + "\nmessage: " + message + ","
                + "\nhref: " + href
                + "\n}";
        return toReturn;
    }
}
