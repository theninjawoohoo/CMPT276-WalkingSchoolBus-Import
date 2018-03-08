package groupdenim.cmpt276.awalkingschoolbus;

        import java.util.ArrayList;
        import java.util.List;

class User {
    private String email;
    private String userName;

    private List<String> groups = new ArrayList<>(); //what groups is the person a part of (by name)
    private List<String> peopleUserIsMonitoring = new ArrayList<>();
    private List<String> peopleMonitoringUser = new ArrayList<>();

    public User(){
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
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
        this.userName = userName;
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
