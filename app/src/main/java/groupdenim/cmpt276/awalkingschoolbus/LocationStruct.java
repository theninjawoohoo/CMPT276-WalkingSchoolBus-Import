package groupdenim.cmpt276.awalkingschoolbus;

/**
 * Created by wwwfl on 2018-03-09.
 * A Basic java object to store the longititude and latitude of a location.
 */

public class LocationStruct {
    public long longitude;
    public long latitude;

    LocationStruct(long latitude, long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
