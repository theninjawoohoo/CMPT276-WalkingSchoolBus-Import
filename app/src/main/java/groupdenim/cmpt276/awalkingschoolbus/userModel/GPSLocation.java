package groupdenim.cmpt276.awalkingschoolbus.userModel;



public class GPSLocation {
    private double lat;
    private double lng;
    private String timestamp;

    public GPSLocation(double lat, double lng, String timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public GPSLocation() {

    }

    public double getLat() {

        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
