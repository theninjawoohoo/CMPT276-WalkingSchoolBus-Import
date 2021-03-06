package groupdenim.cmpt276.awalkingschoolbus.userModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private long id;
    private String name;
    private String email;
    private String password;
    private String birthYear;
    private String birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;
    private int currentPoints;
    private int totalPointsEarned;
    private String customJson;
    private GPSLocation lastGpsLocation = new GPSLocation();
    private List<Group> leadsGroups = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();
    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();
    private String href;
    private List<Message> unreadMessages;
    private List<Message> readMessages;

    public User() {

    }

    public User(Long id, String name, String email, String password, List<User> monitorsUsers, List<User> monitoredByUsers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.monitoredByUsers = monitoredByUsers;
        this.monitorsUsers = monitorsUsers;
    }

    public User(long id, String href) {
        this.id = id;
        this.href = href;
    }

    public User(String name, String email) {
        this.name=name;
        this.email=email;
    }

    public void deepCopyUserFields(User user)
    {
        id=user.getId();
        name=user.getName();
        email=user.getEmail();
        password=user.getPassword();
        birthYear=user.getBirthYear();
        birthMonth=user.getBirthMonth();
        address=user.getAddress();
        cellPhone=user.getCellPhone();
        homePhone=user.getHomePhone();
        grade=user.getGrade();
        teacherName=user.getTeacherName();
        emergencyContactInfo=user.getEmergencyContactInfo();
        lastGpsLocation=user.getLastGpsLocation();
        href=user.getHref();
        memberOfGroups=user.getMemberOfGroups();
        monitoredByUsers=user.getMonitoredByUsers();
        monitorsUsers=user.getMonitorsUsers();
        leadsGroups=user.getLeadsGroups();
        unreadMessages=user.getUnreadMessages();
        readMessages=user.getReadMessages();

        //added current points and total points earned
        currentPoints=user.getCurrentPoints();
        totalPointsEarned=user.getTotalPointsEarned();
        customJson = user.getCustomJson();

    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(int totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public List<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(List<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public List<Message> getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(List<Message> readMessages) {
        this.readMessages = readMessages;
    }

    public List<Group> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<Group> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    //private List<Void> walkingGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
    }
    public void setMemberOfGroups(List<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
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

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmergencyContactInfo() {
        return emergencyContactInfo;
    }

    public void setEmergencyContactInfo(String emergencyContactInfo) {
        this.emergencyContactInfo = emergencyContactInfo;
    }

    public GPSLocation getLastGpsLocation() {
        return lastGpsLocation;
    }

    public void setLastGpsLocation(GPSLocation lastGpsLocation) {
        this.lastGpsLocation = lastGpsLocation;
    }

//    public void setLastGpsLocationLatitude(double lat) {
//        this.lastGpsLocation.setLat(lat);
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
                + "\nmonitorsusers: " + monitorsUsers + ","
                + "\nmonitoredByUsers: " + monitoredByUsers + ","
                + "\nemail: " + email + ","
                + "\nid: " + id
                + "\n}";
        return json;

    }


    public String getCustomJson() {
        return customJson;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }
}
