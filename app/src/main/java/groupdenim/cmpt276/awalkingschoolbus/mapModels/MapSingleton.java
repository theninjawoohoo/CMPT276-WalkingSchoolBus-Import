package groupdenim.cmpt276.awalkingschoolbus.mapModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwwfl on 2018-03-09.
 */

public class MapSingleton {
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

}
