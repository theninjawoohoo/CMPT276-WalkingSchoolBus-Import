package groupdenim.cmpt276.awalkingschoolbus.mapModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwwfl on 2018-03-09.
 */

public class MapSingleton {
    private static final MapSingleton instance = new MapSingleton();

    private placeObject tempObject;

    private MapSingleton(){}

    private List<placeObject> listOfMeetingSpots = new ArrayList<>();

    public placeObject getTempObject() {
        return tempObject;
    }

    public void setTempObject(placeObject tempObject) {
        this.tempObject = tempObject;
    }

    public List<placeObject> getList() {
        return listOfMeetingSpots;
    }

    public static MapSingleton getInstance(){
        return instance;
    }

}
