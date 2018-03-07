package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-03-06.
 */

class User {

    private String email="";   //most likely the primary key/identifier for each member
    private String memberName="";
    private String memberLastName="";
    private String GPSCoordinates="";   //not sure about this, will change later

    private List<Integer> groups=new ArrayList<>(); //what groups is the person a part of, marked by a number
    private List<String> peopleUserIsMonitoring=new ArrayList<>();  //we can add and removed people from here
    private List<String> peopleMonitoringUser=new ArrayList<>();

    private boolean isChild=true;   //false -> parent
    private boolean isBeingMonitored=false;
    private boolean isMonitoringOthers=false;

    public User(){
    }

    public String getEmail() {
        return email;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public List<String> getPeopleUserIsMonitoring() {
        return peopleUserIsMonitoring;
    }

    public List<String> getPeopleMonitoringUser() {
        return peopleMonitoringUser;
    }

    public boolean isChild() {
        return isChild;
    }

    public boolean isBeingMonitored() {
        return isBeingMonitored;
    }

    public boolean isMonitoringOthers() {
        return isMonitoringOthers;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public void setBeingMonitored(boolean beingMonitored) {
        isBeingMonitored = beingMonitored;
    }

    public void setMonitoringOthers(boolean monitoringOthers) {
        isMonitoringOthers = monitoringOthers;
    }
}
