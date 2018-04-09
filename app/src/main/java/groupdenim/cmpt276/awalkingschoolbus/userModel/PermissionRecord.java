package groupdenim.cmpt276.awalkingschoolbus.userModel;

import java.util.List;

/**
 * Created by Farhan on 2018-04-04.
 */

public class PermissionRecord {
    private List<User> users;
    private PermissionStatus status;
    private User whoApprovedOrDenied;

    public PermissionRecord() {
    }

    public PermissionRecord(List<User> users, PermissionStatus status, User whoApprovedOrDenied) {
        this.users = users;
        this.status = status;
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(PermissionStatus status) {
        this.status = status;
    }

    public User getWhoApprovedOrDenied() {
        return whoApprovedOrDenied;
    }

    public void setWhoApprovedOrDenied(User whoApprovedOrDenied) {
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }
}
