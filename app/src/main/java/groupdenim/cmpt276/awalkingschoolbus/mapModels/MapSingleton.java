package groupdenim.cmpt276.awalkingschoolbus.mapModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwwfl on 2018-03-09.
 */

public class MapSingleton {
    List<LocationStruct> listOfMeetingSpots = new ArrayList<>();

    public void addAMeetingSpot(long latitude, long longitude) {
        LocationStruct newLoc = new LocationStruct(latitude, longitude);
        listOfMeetingSpots.add(newLoc);
    }

    public void deleteAMeetingSpot(long latitude, long longitude) {
        LocationStruct deleteThisMeeting = new LocationStruct(latitude, longitude);
        listOfMeetingSpots.remove(deleteThisMeeting);
    }



}
