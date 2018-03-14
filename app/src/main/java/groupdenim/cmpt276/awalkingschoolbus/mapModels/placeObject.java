package groupdenim.cmpt276.awalkingschoolbus.mapModels;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * A Object to store place data because
 * we cannot save place data.
 */

public class placeObject {
    private String name;
    private String address;
    private String id;
    private LatLng latlng;

    public placeObject(String name, String address, String id, LatLng latlng) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latlng = latlng;
    }

    public placeObject(LatLng someLocation) {
        this.latlng = someLocation;
    }

    public placeObject() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }


    @Override
    public String toString() {
        return "placeObject{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latlng=" + latlng +
                '}';
    }
}
