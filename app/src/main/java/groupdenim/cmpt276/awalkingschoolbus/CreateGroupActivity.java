package groupdenim.cmpt276.awalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

public class CreateGroupActivity extends AppCompatActivity {
    private String destination;
    private String groupDescription;
    private Place place;
    private String meetingPlace;
    /*
    private long id;
    private String groupDescription;
    private List<String> members = new ArrayList<>();
    private double[] routeLatArray = new double[2];
    private double[] routeLngArray = new double[2];
    private User leader;
    * */
    private final int MIN_TEXT_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        setupCancelButton();
        setupCreateButton();
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
        EditText editDestination =  findViewById(R.id.editText_CreateGroupActivity_destination);
        EditText editGroupName =  findViewById(R.id.editText_CreateGroupActivity_groupName);
        EditText editMeeting =  findViewById(R.id.editText_CreateGroupActivity_meeting);
        destination = editDestination.getText().toString();
        groupDescription = editGroupName.getText().toString();
        meetingPlace = editMeeting.getText().toString();
    }

    private boolean isInputValid() {
        String errorMessage;
        if (destination.length() < MIN_TEXT_LENGTH) {
            errorMessage = "Destination must be at least " + MIN_TEXT_LENGTH + " characters long.";
        } else if (groupDescription.length() < MIN_TEXT_LENGTH) {
            errorMessage = "Group name must be at least " + MIN_TEXT_LENGTH + " characters long.";
        } else if (meetingPlace.length() < MIN_TEXT_LENGTH) {
            errorMessage = "Meeting place must be at least " + MIN_TEXT_LENGTH + " characters long.";
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

