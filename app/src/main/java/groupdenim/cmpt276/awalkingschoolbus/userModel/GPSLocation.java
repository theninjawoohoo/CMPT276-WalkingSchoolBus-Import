package groupdenim.cmpt276.awalkingschoolbus.userModel;

/**
 * Created by Farhan on 2018-03-21.
 */

public class GPSLocation {
    private long lat;
    private long lng;
    private String timestamp;

    public GPSLocation(long lat, long lng, String timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public GPSLocation() {

    }

    public long getLat() {

        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
