package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;
/**
 * Created by wwwfl on 2018-03-03.
 * Majority of maps code used from: https://www.youtube.com/watch?v=M0bYvXlhgSI
 * And several other of this youtuber's videos
 * The other part of my work such as adding locations and singletons and modifying fragements
 * will be entirely done by me.
 * I've added comments in attempt to understand what is going on with the magic code.
 * This is the model that manages the map activity.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Logging debug message...
        Toast.makeText(this, "Loading up Map!", Toast.LENGTH_SHORT).show();
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

    private static final String TAG = "MapActivty";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 1010;
    private static final float DEFAULT_ZOOM = 14f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168),
                                                                        new LatLng(71, 136));

    //Google map search bar widget + extra button
    private AutoCompleteTextView theSearchBox;
    private ImageView jumpToCurrentLocation;
    private ImageView displayInfoCurrentLocation;
    private ImageView displayNearbyLocation;
    private ImageView addNewMeetingSpot;

    //Google maps objects
    private boolean mLocationPermissionGranted = false;
    private GoogleMap Gmap;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private placeObject mPlace;
    private Marker mMarker;
    private int PLACE_PICKER_REQUEST = 1;
    private Marker tempMarker;

    //This is an object that helps the user obtain a location
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_updated);
        checkGPSStatus();
        getLocationPermissions();
        theSearchBox = (AutoCompleteTextView) findViewById(R.id.input_search);

        jumpToCurrentLocation = (ImageView) findViewById(R.id.ic_gps);
        displayInfoCurrentLocation = (ImageView) findViewById(R.id.ic_info);
        displayNearbyLocation = (ImageView) findViewById(R.id.ic_nearby);
        addNewMeetingSpot = (ImageView) findViewById(R.id.ic_addMeetingSpot);



    }

    private void initializeMap() {
        //Logging debug message...
        Log.d(TAG,"initializeMap(): Map is being initialized...");

        //Set the activity fragment to the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void initialize() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        theSearchBox.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        theSearchBox.setAdapter(mPlaceAutocompleteAdapter);

        theSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //Allows the textbox to search things by checking on user response
            //We base our actions off the IME (input method editor)
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                        //This method does geo-locating and searching
                        geoLocate();
                }
                return false;
            }
        });

        jumpToCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: clicked gps icon");
                getCurrentLocation();
            }
        });

        //This image will display the group view
        //In the future we will show group details instead of the location details.
        displayInfoCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: info has been displayed");
                try{
                    if(mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    }
                    else {
                        mMarker.showInfoWindow();
                    }
                }
                catch (NullPointerException e) {
                    Log.e(TAG, "onClick: nullPtrException found");
                }
            }
        });

        displayNearbyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "GooglePlayServicesNotAvailableException: " + e.getMessage());
                }
            }
        });

        //This widget creates a new meeting spot at this location.
        addNewMeetingSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapSingleton mapSingleton = MapSingleton.getInstance();
                mapSingleton.setTempObject(mPlace);
                Intent intent = new Intent(MapActivity.this, CreateGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button helpUser = (Button) findViewById(R.id.btn_helpUser);
        Button switchToGroupView = (Button) findViewById(R.id.btn_group_view_switch);

        //This opens our group list.
        switchToGroupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, GroupMeeting.class);
                startActivity(intent);
                finish();
            }
        });

        helpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment help = new HelpFragment();
                help.show(getFragmentManager(), "Help Box");
            }
        });
        //Then hide the keyboard
        hideSoftKeyboard();
    }

    //Code for nearby place picker
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                PendingResult<PlaceBuffer> resultOfLocation = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                resultOfLocation.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }


    //Precondition: the user enters input into the search box
    private void geoLocate() {
        Log.d(TAG, "Searching for location");

        String searchedLocation = theSearchBox.getText().toString();
        Toast.makeText(MapActivity.this, searchedLocation , Toast.LENGTH_SHORT).show();
        //Once the input is placed in the search box, we have to obtain a list of addresses
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> listOfAddresses = new ArrayList<>();
        try {
            listOfAddresses = geocoder.getFromLocationName(searchedLocation, 1);
        }
        catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        //Now Go To The location
        if(listOfAddresses.size() > 0) {
            Address address = listOfAddresses.get(0);
            Log.d(TAG, "geoLocate: I found the place. Heading over to" + address.toString());
            LatLng aLocation = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(aLocation, DEFAULT_ZOOM, address.getAddressLine(0));
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

            //Prompts the user, Hey do I have permission to use location on your phone.
            case LOCATION_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    //For each grant check if any of the permissions have been denied.
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
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

    //Gets the device location.
    private void getCurrentLocation() {
        //Log getting the location.
        Log.d(TAG,"getCurrentLocation() is called... ");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Ensure that no permissions are breached.
        try{
            if(mLocationPermissionGranted) {
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
                            Toast.makeText(MapActivity.this,"Where are you?", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
        catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: securityException has been called.");
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

    //Create a method to be able to move the camera around
    //This method accepts a place to move to.
    private void moveCamera(LatLng latlng, float zoom, placeObject thePlace) {
        Log.d(TAG, "Camera moved to latitude" + latlng.latitude
                + ", longitude " + latlng.longitude);
        Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        Gmap.setInfoWindowAdapter(new customWindow(MapActivity.this));

        if(thePlace != null) {
            try{
                String markerInfo = "Address: " + thePlace.getAddress() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latlng)
                        .title(thePlace.getName())
                        .snippet(markerInfo);

                if(tempMarker != null) {
                    tempMarker.remove();
                }
                mMarker = Gmap.addMarker(options);
                tempMarker = mMarker;
            }
            catch(NullPointerException e) {
                Log.e(TAG, "moveCamera: NullPointerException " + e.getMessage());
            }
        }
        else{
            Gmap.addMarker(new MarkerOptions().position(latlng));
        }

    }

    //Hides the keyboard upon searching/tapping.
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(theSearchBox.getWindowToken(), 0);
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

    //Search box tap
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> resultOfLocation = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeID);
            resultOfLocation.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    //Get's the place and stores it into a global placeObject.
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            //Release places in order to reduce memory leak

            if(!places.getStatus().isSuccess()) {
                Log.d(TAG, "OnResult: Place query failed. " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            //Set up a place object to initialize our global place to..
            try{
                mPlace = new placeObject();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());

                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());

                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());

                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };

}
