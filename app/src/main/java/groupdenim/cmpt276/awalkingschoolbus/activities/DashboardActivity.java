package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.GPSLocation;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;


/**
 * Created by wwwfl on 2018-03-27.
 */

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Logging debug message...
        Log.d(TAG, "onMapReady(): Google Map is ready to go.");

        //Create the map
        Gmap = googleMap;

        if (mLocationPermissionGranted) {
            getCurrentLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Gmap.setMyLocationEnabled(true);
            Gmap.getUiSettings().setMyLocationButtonEnabled(false);

            initialize();
        }
    }


    //Changing/Dynamic Variables
    private GoogleMap Gmap;
    private boolean mLocationPermissionGranted = false;

    //Constant variables
    private final String TAG = "TAG";
    private static final int LOCATION_REQUEST_CODE = 1010;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 14f;

    //Array list of markers for children.
    List<Marker> listOfChildrenMarkers = new ArrayList<>();

    //Location manager
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        checkGPSStatus();
        getLocationPermissions();
        try {
            populateMapWithChildren();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void initialize() {

    }


    private void getCurrentLocation() {
        //Log getting the location.
        Log.d(TAG,"getCurrentLocation() is called... ");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Ensure that no permissions are breached.
        try{
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found LocationStruct!");
                            Location currentLocation = (Location) task.getResult();
                            LatLng latlngCoords = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(latlngCoords, DEFAULT_ZOOM, "My Location");
                        }
                        else {
                            Log.d(TAG, "onComplete: current location cannot be found");
                            Toast.makeText(DashboardActivity.this,"Where are you?", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: securityException has been called.");
        }
    }

    //Requests the user for permissions
    private void getLocationPermissions() {
        //Logging debug message...
        Log.d(TAG,"getLocationPermissions(): Obtaining permissions...");

        //Create a list of Permissions array
        String[] listOfPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        //Then we check if the device allows the usage of location
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initializeMap();
            }
            else {
                ActivityCompat.requestPermissions(this, listOfPermissions, LOCATION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, listOfPermissions, LOCATION_REQUEST_CODE);
        }
    }

    //Create a method to be able to move the camera around
    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "Camera moved to latitude" + latlng.latitude
                + ", longitude " + latlng.longitude);
        Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void initializeMap() {
        //Logging debug message...
        Log.d(TAG,"initializeMap(): Map is being initialized...");

        //Set the activity fragment to the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DashboardActivity.this);
    }

    //Prevents the app from crashing if GPS is turned off.
    //Code found somewhere in Youtube comments
    private void checkGPSStatus() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    //Prevents the app from crashing if GPS is turned off.
    //Code found somewhere in Youtube comments
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Populate the map with your children
    private void populateMapWithChildren() throws ParseException {
        long id = CurrentUserSingleton.getInstance(DashboardActivity.this).getId();
        List<User> childrenList = new ArrayList<>();

        ProxyBuilder.SimpleCallback<List<User>> callback = list -> getChildren(list, childrenList) ;
        ServerSingleton.getInstance().getMonitorUsers(this, callback, id);


    }

    private void getChildren(List<User> list, List<User> childrenList) {
        for(User child : list) {
            getChild(child.getEmail(), childrenList);
        }

    }

    private void getChild(String email, List<User> childrenList) {
        ProxyBuilder.SimpleCallback<User> callback = user -> gotUser(user, childrenList);
        ServerSingleton.getInstance().getUserByEmail(this,callback,email);

    }

    private void gotUser(User user, List<User> childrenList) {
        GPSLocation childLocation = user.getLastGpsLocation();
        if(childLocation.getLat() != 0 && childLocation.getLng() != 0
                && childLocation.getTimestamp() != null) {
            Log.i("FUJ", "gotUser: " + childLocation.getTimestamp());
            childrenList.add(user);
        }
        else {
            Toast.makeText(DashboardActivity.this,"Child: " + user
                            + " has not started walking" + "\n"
                            + " Lat: " + childLocation.getLat() + "\n"
                            + " Long: " + childLocation.getLng() + "\n"
                            + " TimeStamp: " + childLocation.getTimestamp() + "\n"
                    , Toast.LENGTH_LONG).show();
            Log.i("FUJK", "gotUser: " + user.getLastGpsLocation().getLat());
        }
//        try {
//            markerAdd(user);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }


    private void markerAdd(User child) throws ParseException {
        //Get latitude longitude coordinates
        GPSLocation childLocation = child.getLastGpsLocation();
        LatLng latlng = new LatLng(childLocation.getLat(), childLocation.getLng());

        //Calculate the difference in the time
        String timestamp = childLocation.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh-mm-ss a");
        Date date = sdf.parse(timestamp);

        long differenceInSeconds = (System.currentTimeMillis() - date.getTime()) * 100;

        //Get childDetails from server
        String childDetails = "Email: " + child.getEmail() + "\n" +
                "Time: " + childLocation.getTimestamp() + "\n" +
                "Difference In Time: " + differenceInSeconds + " seconds" + "\n";

        //Add a new marker
        MarkerOptions newMarker = new MarkerOptions()
                .position(latlng)
                .title("Location of child")
                .snippet(childDetails)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        //Place it on the map
        Gmap.addMarker(newMarker);

    }
}
