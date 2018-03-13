package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

class User {
    private String email;
    private String name;


    private List<String> groups = new ArrayList<>(); //what groups is the person a part of (by name)


    private List<String> peopleUserIsMonitoring = new ArrayList<>();    //uses email
    private List<String> peopleMonitoringUser = new ArrayList<>();      //uses email

    public User(String userName, String email){
        this.name = userName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return name;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getPeopleUserIsMonitoring() {
        return peopleUserIsMonitoring;
    }

    public List<String> getPeopleMonitoringUser() {
        return peopleMonitoringUser;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public void removeFromGroup(String groupName) {
        for ( int i = 0;  i < groups.size(); i++){
            String currentGroupName = groups.get(i);
            if (currentGroupName.equals(groupName)){
                groups.remove(i);
                break;
            }
        }
    }

    public void addToGroup(String groupName) {
        groups.add(groupName);
    }
}
