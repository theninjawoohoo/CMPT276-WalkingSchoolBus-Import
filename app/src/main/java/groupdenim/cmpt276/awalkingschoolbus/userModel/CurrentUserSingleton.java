package groupdenim.cmpt276.awalkingschoolbus.userModel;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;

/**
 * Created by niragmehta on 2018-03-13.
 */

public class CurrentUserSingleton {

    private static Context context;

    private Long id;
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
    private GPSLocation lastGpsLocation;
    private List<Message> unreadMessages;
    private List<Message> readMessages;
    private List<Group> leadsGroups = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();
    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();

    private static final CurrentUserSingleton instance = new CurrentUserSingleton();

    public static CurrentUserSingleton getInstance(Context context) {

        if(instance ==null)
        {
            updateUserSingleton(context);

        }

        return instance;
    }

    public static void updateUserSingleton(Context context)
    {
        ProxyBuilder.SimpleCallback<User> callback = userObj -> setFields(userObj);
        //306 should be nini
        ServerSingleton.getInstance().getUserById(context,callback, instance.id);

    }

    public static void setFields(User user)
    {
        instance.setEmail(user.getEmail());
        instance.setName(user.getName());
        instance.setId(user.getId());
        instance.setPassword(user.getPassword());
        instance.setLeadsGroups(user.getLeadsGroups());
        instance.setMemberOfGroups(user.getMemberOfGroups());
        instance.setMonitorsUsers(user.getMonitorsUsers());
        instance.setMonitoredByUsers(user.getMonitoredByUsers());
        instance.setBirthYear(user.getBirthYear());
        instance.setBirthMonth(user.getBirthMonth());
        instance.setAddress(user.getAddress());
        instance.setCellPhone(user.getCellPhone());
        instance.setHomePhone(user.getHomePhone());
        instance.setGrade(user.getGrade());
        instance.setTeacherName(user.getTeacherName());
        instance.setEmergencyContactInfo(user.getEmergencyContactInfo());
        instance.setLastGpsLocation(user.getLastGpsLocation());
        instance.setUnreadMessages(user.getUnreadMessages());
        instance.setReadMessages(user.getReadMessages());
    }

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

    public void setMemberOfGroups(List<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public void setLeadsGroups(List<Group> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public void setId(Long id) {
        instance.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private CurrentUserSingleton() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Group> getLeadsGroups() {
        return leadsGroups;
    }

    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
    }

    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }


}
