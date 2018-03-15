package groupdenim.cmpt276.awalkingschoolbus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private long id;
    private String groupDescription;
    private List<String> members = new ArrayList<>();
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private User leader;

    public Group() {}

    public Group(String groupDescription, List<String> members, double[] routeLatArray,
                 double[] routeLngArray, User leader) {
        this.groupDescription = groupDescription;
        this.members = members;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(String userEmail) {
        members.add(userEmail);
    }

    public void removeMember(String userEmail) {
        members.remove(userEmail);
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
