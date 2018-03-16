package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;

public class CreateGroupActivity extends AppCompatActivity {
    private String groupDescription;
    private String address;
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private placeObject place;

    private final int MIN_TEXT_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getFieldsFromMap();
        setupCancelButton();
        setupCreateButton();
        setupAddressText();

    }

    private void getFieldsFromMap() {
        MapSingleton mapSingleton = MapSingleton.getInstance();
        place = mapSingleton.getTempObject();
        address = place.getAddress();
        LatLng latLng = place.getLatlng();
        routeLatArray[0] = latLng.latitude;
        routeLngArray[0] = latLng.longitude;
        routeLatArray[1] = 0; //TEMPORARY
        routeLngArray[1] = 0; //TEMPORARY
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

            }
        });
    }

    private void getInput() {
        EditText editGroupName = findViewById(R.id.editText_CreateGroupActivity_groupDescription);
        groupDescription = editGroupName.getText().toString();
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
}

