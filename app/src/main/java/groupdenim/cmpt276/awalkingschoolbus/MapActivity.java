package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by wwwfl on 2018-03-03.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "MapActivty";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 1010;


    private boolean mLocationPermissionGranted = false;
    private GoogleMap Gmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermissions();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Logging debug message...
        Toast.makeText(this, "Map is ready to go!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady(): Google Map is ready to go.");

        //Create the map
        Gmap = googleMap;
    }

    private void initializeMap() {
        //Logging debug message...
        Log.d(TAG,"initializeMap(): Map is being initialized...");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

    }

    private void getLocationPermissions() {
        //Logging debug message...
        Log.d(TAG,"getLocationPermissions(): Obtaining permissions...");
        //Create a list of Permissions array
        String[] listOfPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                                      Manifest.permission.ACCESS_FINE_LOCATION};

        //Then we check if the device allows the usage of location
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult() is called... ");
        mLocationPermissionGranted = false;

        //This method shall check if the permissions are granted on the mobile device
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if(grantResults.length > 0) {

                    for(int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Log.d(TAG, "permission denied...");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    //Some permission is granted
                    Log.d(TAG, "permission granted...");
                    mLocationPermissionGranted = true;
                    initializeMap();
                }
            }
        }
    }



}
