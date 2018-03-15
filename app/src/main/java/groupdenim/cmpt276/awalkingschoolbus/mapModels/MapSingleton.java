package groupdenim.cmpt276.awalkingschoolbus.mapModels;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.Group;

/**
 * Created by wwwfl on 2018-03-09.
 */

public class MapSingleton {
    private static MapSingleton instance;

    private MapSingleton(){}

    private List<placeObject> listOfMeetingSpots = new ArrayList<>();

    public void addAMeetingSpot(placeObject aPlace) {
        listOfMeetingSpots.add(aPlace);
    }

    public void deleteAMeetingSpot(placeObject aPlace) {
        listOfMeetingSpots.remove(aPlace);
    }

    public List<placeObject> getList() {
        return listOfMeetingSpots;
    }

    public void convertGroupsToMeetingPlaces(List<Group> groups) {
        listOfMeetingSpots.clear();
        for(Group aGroup : groups) {
            double latitude = aGroup.getRouteLatArray()[0];
            double longitude = aGroup.getRouteLngArray()[0];
            placeObject convertedObject = new placeObject(latitude, longitude);
            listOfMeetingSpots.add(convertedObject);
        }
    }

    public static MapSingleton getInstance(){
        if (instance == null) {
            instance = new MapSingleton();
        }
        return instance;
    }

}
