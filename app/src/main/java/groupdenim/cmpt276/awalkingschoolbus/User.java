package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-03-06.
 */

class User {

    private long id=0;
    private String email="";   //most likely the primary key/identifier for each member
    private String memberName="";

    private List<Integer> groups=new ArrayList<>(); //what groups is the person a part of, marked by a number
    private List<String> peopleUserIsMonitoring=new ArrayList<>();  //we can add and removed people from here
    private List<String> peopleMonitoringUser=new ArrayList<>();


    public User(){
    }

    public String getEmail() {
        return email;
    }

    public String getMemberName() {
        return memberName;
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



    public void setEmail(String email) {
        this.email = email;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

}
