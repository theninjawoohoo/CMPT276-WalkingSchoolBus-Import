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
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;

/**
 * Created by wwwfl on 2018-03-25.
 */

public class WalkingMapActivity extends AppCompatActivity implements OnMapReadyCallback,
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
    private boolean isTracking = true;
    private LatLng myLatLng;

    //Constant variables
    private final String TAG = "TAG";
    private static final int LOCATION_REQUEST_CODE = 1010;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 14f;
    private static final int WAIT_TIME = 30000;


    //Location manager
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager theLocManager;

    //Markers
    private Marker tempMarker;
    private Marker mMarker;

    //Some textView and buttons
    private TextView latitudeText;
    private TextView longitudeText;
    private TextView trackingStatus;
    private Button enableTracking;
    private Button disableTracking;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_map);
        checkGPSStatus();
        getLocationPermissions();

        //Setup the managers and buttons
        theLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        latitudeText = (TextView) findViewById(R.id.latitude);
        longitudeText = (TextView) findViewById(R.id.longitude);

        updateTracker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        theLocManager.removeUpdates((LocationListener) this);
        Log.i(TAG, "onPause, done");
    }

    private void initialize() {
//        trackingStatus = (TextView) findViewById(R.id.trackMode);
//        trackingStatus.setText(R.string.trueStatement);
//        enableTracking = (Button) findViewById(R.id.btn_resume_tracking);
//        disableTracking = (Button) findViewById(R.id.btn_stop_tracking);
//
//        enableTracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                trackingStatus.setText(R.string.trueStatement);
//                if(isTracking) {
//                    return;
//                }
//                updateTracker();
//                isTracking = true;
//
//            }
//        });
//
//        disableTracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                trackingStatus.setText(R.string.falseStatement);
//                if(!isTracking) {
//                    return;
//                }
//                isTracking = false;
//            }
//        });
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
                            Toast.makeText(WalkingMapActivity.this,"Where are you?", Toast.LENGTH_SHORT).show();
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

        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(title);

        if(tempMarker != null) {
            tempMarker.remove();
        }
        mMarker = Gmap.addMarker(options);
        tempMarker = mMarker;

    }

    private void initializeMap() {
        //Logging debug message...
        Log.d(TAG,"initializeMap(): Map is being initialized...");

        //Set the activity fragment to the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(WalkingMapActivity.this);
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

    private void updateTracker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Ensure that the network provider is enabled.
        //Courtesy of this video: https://www.youtube.com/watch?v=qS1E-Vrk60E
        if(theLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            theLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    WAIT_TIME, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //Get latitude and longitude
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            latitudeText.setText("" + latitude);
                            longitudeText.setText("" + longitude);

                            LatLng yourLatLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                String currentLocality = addressList.get(0).getLocality();
                                Gmap.clear();
                                moveCamera(yourLatLng, DEFAULT_ZOOM, currentLocality);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {
                            isTracking = true;
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                            isTracking = false;
                        }
                    });
        }

        //Use the GPS provider instead of the network provider
        else if (theLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            theLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    WAIT_TIME, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //Get latitude and longitude
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            latitudeText.setText("" + latitude);
                            longitudeText.setText("" + longitude);

                            LatLng yourLatLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                String currentLocality = addressList.get(0).getLocality();
                                Gmap.clear();
                                moveCamera(yourLatLng, DEFAULT_ZOOM, currentLocality);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Code used from...
                            //https://github.com/AllInOneYT/Project/blob/master/Android/MyApplication/app/src/main/java/myapp/myapplication/MainActivity.java
                            long currentDate = System.currentTimeMillis();
                            //Extract the date
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh-mm-ss a");
                            String dateString = sdf.format(currentDate);




                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
        }
    }

    //THis class will have to get the singleton and send the user's current location to the server
    private void sendCurrentLocationTOServer() {

    }

}