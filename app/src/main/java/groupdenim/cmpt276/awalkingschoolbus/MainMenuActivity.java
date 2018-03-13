package groupdenim.cmpt276.awalkingschoolbus;

//DO NOT COMMIT THIS FILE!
//This Maps test code was provided by:
//Tutorials used below
//https://www.youtube.com/watch?v=M0bYvXlhgSI

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainMenuActivity extends AppCompatActivity {

    //Some const ints
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Group group = new Group("School", "TestGroup", "MyHouse",
            new Coordinate(0, 0), new Coordinate(0, 0));

    private User user = new User("tempUserName", "tempEmail");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initializeMonitor();
        if(doesGoogleMapsWork()) {
            initializeMapButton();
        }
        initializeCreateButton();
        initializeInfoButton();

        UserSingleton userSingleton = UserSingleton.getInstance();
        user.addPersonToMonitor("ChildEmail1");
        user.addPersonToMonitor("ChildEmail2");
        user.addPersonToMonitor("ChildEmail3");
        user.addPersonToMonitor("ChildEmail4");
        user.addPersonToMonitor("ChildEmail5");
        user.addPersonToMonitor("ChildEmail6");
        userSingleton.addUser(user);
        GroupSingleton groupSingleton = GroupSingleton.getInstance();
        groupSingleton.addGroup(group);
    }

    public boolean doesGoogleMapsWork() {
        Log.d(TAG, "doesGoogleMapsWork(): checking the api key and current version");

        int apiAvaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainMenuActivity.this);
        if(apiAvaliable == ConnectionResult.SUCCESS) {
            //The user should be able to make google maps requests
            Log.d(TAG, "doesGoogleMapsWork(): Google Play Map services is functional.");
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(apiAvaliable)) {
            //An error has occured but it's most likely an outdated api.
            Log.d(TAG, "doesGoogleMapsWork():An error has occured but it can be fixed");
            Dialog dialogBox = GoogleApiAvailability.getInstance().getErrorDialog(MainMenuActivity.this,
                                                                                apiAvaliable,
                                                                                ERROR_DIALOG_REQUEST);
            dialogBox.show();
        }

        else {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void initializeMapButton() {
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeMonitor() {
        Button btnMonitor = (Button) findViewById(R.id.btn_monitor);
        btnMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MonitorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeInfoButton() {
        Button button = findViewById(R.id.temp_button_info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, GroupInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeCreateButton() {
        Button button = findViewById(R.id.temp_button_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }
}
