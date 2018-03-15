package groupdenim.cmpt276.awalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import java.util.Map;

import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;

public class CreateGroupActivity extends AppCompatActivity {
    private String groupDescription;
    private String address;
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private placeObject place;
    private String meetingPlace;

    private final int MIN_TEXT_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getFieldsFromMap();
        setupCancelButton();
        setupCreateButton();
    }

    private void getFieldsFromMap() {
        MapSingleton mapSingleton = MapSingleton.getInstance();
        //get address for display
        //get coordinates for meeting place
        //set coordinates for destination to 0,0

    }

    private void setupCancelButton(){
        Button button = findViewById(R.id.button_CreateGroupActivity_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    //sendInput();
                    finish();
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
        if (destination.length() < MIN_TEXT_LENGTH) {
            errorMessage = "Destination must be at least " + MIN_TEXT_LENGTH + " characters long.";
        } else {
            return true;
        }
        Toast.makeText(CreateGroupActivity.this, errorMessage,
                Toast.LENGTH_SHORT).show();
        return false;
    }

    /*
    private void sendInput() {
        //TODO: need to figure out what to do with coordinates:
        Coordinate tempCoord = new Coordinate(0, 0);
        Group group = new Group(destination, groupDescription, meetingPlace, tempCoord, tempCoord);
        GroupSingleton groupSingleton = GroupSingleton.getInstance();
        groupSingleton.addGroup(group);
    }
    */
}

