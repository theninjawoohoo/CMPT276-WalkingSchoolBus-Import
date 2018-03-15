package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

public class UserServer {
    private Long id;
    private String name;
    private String email;
    private String password;

    private List<UserServer> leadsGroups = new ArrayList<>();

    private List<UserServer> memberOfGroups = new ArrayList<>();

    private List<UserServer> monitoredByUsers = new ArrayList<>();

    private List<UserServer> monitorsUsers = new ArrayList<>();

    private String href;
    public UserServer() {
    }

    public List<UserServer> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<UserServer> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    //private List<Void> walkingGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    public List<UserServer> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<UserServer> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }
    public UserServer(Long id, String name, String email, String password, List<UserServer> monitorsUsers, List<UserServer> monitoredByUsers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.monitoredByUsers = monitoredByUsers;
        this.monitorsUsers = monitorsUsers;
    }

    public List<UserServer> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(List<UserServer> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public List<UserServer> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<UserServer> monitorsUsers) {
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

    @Override
    public String toString() {
        String json = "{ \n name: " + name + ","
                + "\nemail: " + email + ","
                + "\nid: " + id
                + "\n}";
        return json;
    }
}



