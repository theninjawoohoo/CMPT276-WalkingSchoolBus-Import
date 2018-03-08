package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private List<String> members = new ArrayList<>();
    private String destination;
    private String meetingPlace;
    private Coordinate destinationCoordinate;
    private Coordinate meetingCoordinate;

    public Group() {}

    public Group(String destination, String groupName, String meetingPlace,
                 Coordinate destinationCoordinate, Coordinate meetingCoordinate) {
        this.destination = destination;
        this.groupName = groupName;
        this.meetingPlace = meetingPlace;
        this.destinationCoordinate = destinationCoordinate;
        this.meetingCoordinate = meetingCoordinate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public Coordinate getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public void setDestinationCoordinate(Coordinate destinationCoordinate) {
        this.destinationCoordinate = destinationCoordinate;
    }

    public Coordinate getMeetingCoordinate() {
        return meetingCoordinate;
    }

    public void setMeetingCoordinate(Coordinate meetingCoordinate) {
        this.meetingCoordinate = meetingCoordinate;
    }
}
