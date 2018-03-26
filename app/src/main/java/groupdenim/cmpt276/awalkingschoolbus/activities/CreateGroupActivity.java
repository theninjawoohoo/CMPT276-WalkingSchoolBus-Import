package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.fragments.PlaceAutocompleteAdapter;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;

public class CreateGroupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String groupDescription;
    private String address;
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private final String TAG = "TAG";
    private placeObject place;
    private GoogleApiClient mGoogleApiClient;

    //Search box that looks for a google place.
    private AutoCompleteTextView theSearchBox;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168),
            new LatLng(71, 136));

    private final int MIN_TEXT_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getFieldsFromMap();
        setupCancelButton();
        setupCreateButton();
        setupAddressText();
        setupSearchButton();

    }

    private void getFieldsFromMap() {
        MapSingleton mapSingleton = MapSingleton.getInstance();
        place = mapSingleton.getTempObject();
        address = place.getAddress();
        LatLng latLng = place.getLatlng();
        routeLatArray[0] = latLng.latitude;
        routeLngArray[0] = latLng.longitude;
    }

    private void setupCancelButton(){
        Button button = findViewById(R.id.button_CreateGroupActivity_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupCreateButton() {
        Button button = findViewById(R.id.button_CreateGroupActivity_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: make it validate input then store input in new group
                getInput();
                if (isInputValid()) {
                    button.setEnabled(false);
                    sendInput();
                }

                if(!(theSearchBox.getText().toString().isEmpty())) {
                    try {
                        geoLocate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void getInput() {
        EditText editGroupName = findViewById(R.id.editText_CreateGroupActivity_groupDescription);
        editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(theSearchBox.getText().toString().isEmpty())) {
                    try {
                        geoLocate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        groupDescription = editGroupName.getText().toString();
    }

    private void setupSearchButton() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        theSearchBox = (AutoCompleteTextView) findViewById(R.id.input_search);

        //AutoComplete Box.
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        //Sets an autocomplete adapter for the search box.
        theSearchBox.setAdapter(mPlaceAutocompleteAdapter);

        theSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //Allows the textbox to search things by checking on user response
            //We base our actions off the IME (input method editor)
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_BACK) {
                    //Sets the destination.
                    try {
                        geoLocate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }


        });

        hideSoftKeyboard();
    }

    //Precondition: the user enters input into the search box
    private void geoLocate() throws Exception {
        Log.d(TAG, "Searching for location");

        String searchedLocation = theSearchBox.getText().toString();
        Toast.makeText(CreateGroupActivity.this, searchedLocation , Toast.LENGTH_LONG).show();

        //Once the input is placed in the search box, we have to obtain a list of addresses
        Geocoder geocoder = new Geocoder(CreateGroupActivity.this);
        List<Address> listOfAddresses = new ArrayList<>();
        try {
            listOfAddresses = geocoder.getFromLocationName(searchedLocation, 1);
        }
        catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(listOfAddresses.size() == 0) {
            throw new Exception("geoLocate: Something Wrong has happened.");
        }

        //Now Go To The location
        //And set the destination to send to the server
        if (listOfAddresses.size() > 0) {
            Address address = listOfAddresses.get(0);
            routeLatArray[1] = address.getLatitude();
            routeLngArray[1] = address.getLongitude();
        }
    }

    private boolean isInputValid() {
        String errorMessage;
        if (groupDescription.length() < MIN_TEXT_LENGTH) {
            errorMessage = "Description must be at least " + MIN_TEXT_LENGTH + " characters long.";
        } else {
            return true;
        }
        Toast.makeText(CreateGroupActivity.this, errorMessage,
                Toast.LENGTH_SHORT).show();
        return false;
    }

    private void sendInput() {
        //CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(this);
//        User currentUser = new User();
//        currentUser.setId(CurrentUserSingleton.getInstance(CreateGroupActivity.this).getId());
//        //Group group = new Group(groupDescription, new ArrayList<String>(), routeLatArray, routeLngArray, currentUser);
//        Group group = new Group();
//        group.setLeader(currentUser);
//        group.setGroupDescription(groupDescription);
//  //      group.setRouteLatArray(routeLatArray);
////        group.setRouteLngArray(routeLngArray);
//
//        //Add the group and wait for a response
//        ProxyBuilder.SimpleCallback<Group> callback = groups -> createGroupResponse(groups);
//        ServerSingleton.getInstance().createNewGroup(CreateGroupActivity.this, callback, group);

        Group group = new Group();
        group.setGroupDescription(groupDescription);
        group.setId(3);
        group.setRouteLatArray(routeLatArray);
        group.setRouteLngArray(routeLngArray);
        User user = new User();
        user.setId(CurrentUserSingleton.getInstance(CreateGroupActivity.this).getId());
        group.setLeader(user);
        Log.i("a", "sendInput: " + group);
        ProxyBuilder.SimpleCallback<Group> callback = groupa -> createGroupResponse(groupa);
        ServerSingleton.getInstance().createNewGroup(this,callback,group);
    }

    private void createGroupResponse(Group group) {
        // Update the MapSingleton's group list with the new group
        Log.i("a", "createGroupResponse: " + group);
        ProxyBuilder.SimpleCallback<List<Group>> callback = groups -> getGroupsResponse(groups);
        ServerSingleton.getInstance().getGroupList(CreateGroupActivity.this, callback);
    }

    private void getGroupsResponse(List<Group> groups) {
        //Update the mapSingleton group list and go back to map activity
        MapSingleton mapSingleton = MapSingleton.getInstance();
        mapSingleton.convertGroupsToMeetingPlaces(groups);
        Intent intent = new Intent(CreateGroupActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupAddressText() {
        TextView text = findViewById(R.id.textView_CreateGroupActivity_meetingAddress);
        text.setText(address);
    }

    //Hides the keyboard upon searching/tapping.
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(theSearchBox.getWindowToken(), 0);
    }
}

