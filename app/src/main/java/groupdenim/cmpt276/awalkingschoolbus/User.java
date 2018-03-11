package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    private List<User> leadsGroups = new ArrayList<>();

    private List<User> memberOfGroups = new ArrayList<>();

    private List<User> monitoredByUsers = new ArrayList<>();

    private List<User> monitorsUsers = new ArrayList<>();

    private String href;
    public User() {

    }

    public List<User> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<User> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    //private List<Void> walkingGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    public List<User> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<User> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }
    public User(Long id, String name, String email, String password, List<User> monitorsUsers, List<User> monitoredByUsers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.monitoredByUsers = monitoredByUsers;
        this.monitorsUsers = monitorsUsers;
    }

    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

//    public List<Void> getWalkingGroups() {
//        return walkingGroups;
//    }
//
//    public void setWalkingGroups(List<Void> walkingGroups) {
//        this.walkingGroups = walkingGroups;
//    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
