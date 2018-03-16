package groupdenim.cmpt276.awalkingschoolbus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
//Test commit
public class Group {
    private long id;
    private String groupDescription;
    private List<User> memberUsers = new ArrayList<>();
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private User leader;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    private String href;
    public Group() {}

    public Group(String groupDescription, List<User> memberUsers, double[] routeLatArray,
                 double[] routeLngArray, User leader) {
        this.groupDescription = groupDescription;
        this.memberUsers = memberUsers;
        this.routeLatArray = routeLatArray;
        this.routeLngArray = routeLngArray;
        this.leader = leader;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public List<User> getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(List<User> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public void addMember(User user) {
        memberUsers.add(user);
    }

    public void removeMember(String userEmail) {
        memberUsers.remove(userEmail);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double[] getRouteLatArray() {
        return routeLatArray;
    }

    public void setRouteLatArray(double[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public double[] getRouteLngArray() {
        return routeLngArray;
    }

    public void setRouteLngArray(double[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }
}
