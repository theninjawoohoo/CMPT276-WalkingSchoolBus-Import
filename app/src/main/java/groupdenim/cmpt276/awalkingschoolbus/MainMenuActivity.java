package groupdenim.cmpt276.awalkingschoolbus;

//DO NOT COMMIT THIS FILE!
//This Maps test code was provided by:
//Tutorials used below
//https://www.youtube.com/watch?v=M0bYvXlhgSI

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private Context contexta;
    //Some const ints
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private User user = new User("tempUserName", "tempEmail");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //testUserList();
        //getuserbyId();
        //getMonitorUser();
        //monitorUser();
        //monitoredUser();
        //getMonitored();
        //stopMonitoring();

        initializeMonitor();
        if(doesGoogleMapsWork()) {
            initializeMapButton();
        }
        initializeCreateButton();
        initializeInfoButton();

    }

//    private void stopMonitoring() {
//        contexta = this.getApplicationContext();
//        String email = "317";
//        long id = 473;
//        long aa = 306;
//        ProxyBuilder.SimpleCallback<Void> callback = userList -> stopMonit(userList);
//        //306 should be nini
//        ServerSingleton.getInstance().stopMonitoringUser(contexta,callback,CurrentUserSingleton.getInstance(contexta).getId(),aa);
//    }
//
//    private void stopMonit(Void nothing) {
//        getuserbyId();
//    }


//    private void monitoredUser() {
//        contexta = this.getApplicationContext();
//        String email = "317";
//        long id = 473;
//        long aa = 318;
//        ProxyBuilder.SimpleCallback<List<User>> callback = userList -> monitored(userList);
//        //306 should be nini
//        ServerSingleton.getInstance().monitoredByUsers(contexta,callback,CurrentUserSingleton.getInstance(contexta).getId(),aa);
//    }

//    private void monitored(List<User> userList) {
//        //Log.i(TAG, "monitored: " + userList);
//        Log.i(TAG, "monitored: " + CurrentUserSingleton.getInstance(this).getMonitoredByUsers());
//    }
//    private void getMonitored() {
//        //Log.i(TAG, "monitored: " + userList);
//        Log.i(TAG, "monitored: " + CurrentUserSingleton.getInstance(this).getMonitoredByUsers());
//    }

//    private void monitorUser() {
//        contexta = this.getApplicationContext();
//        String email = "317";
//        long id = 473;
//        long aa = 318;
//        ProxyBuilder.SimpleCallback<List<User>> callback = userList -> monitor(userList);
//        //306 should be nini
//        ServerSingleton.getInstance().monitorUsers(contexta,callback,CurrentUserSingleton.getInstance(contexta).getId(),aa);
//    }
//
//    private void monitor(List<User> userList) {
//        Log.i("aagairwgjoirwjg", "monitor: " + userList);
//    }

//    private void getMonitorUser() {
//        contexta = this.getApplicationContext();
//        String email = "yout@yout.ca";
//        long id = 318;
//        ProxyBuilder.SimpleCallback<List<User>> callback = userList -> getMonitor(userList);
//        //306 should be nini
//        ServerSingleton.getInstance().getMonitorUsers(contexta,callback, id);
//        Log.i("ag", "testUserList: asaogjaiorwjg");
//    }
//
//    private void getMonitor(List<User> userList) {
//        Log.i("MONITOR", "getMonitor: " + userList);
//    }
//
//    private void getuserbyId() {
//        contexta = this.getApplicationContext();
//        String email = "yout@yout.ca";
//        ProxyBuilder.SimpleCallback<User> callback = userList -> getuserId(userList);
//        //306 should be nini
//        ServerSingleton.getInstance().getUserByEmail(contexta,callback, CurrentUserSingleton.getInstance(this).getEmail());
//        Log.i("ag", "testUserList: asaogjaiorwjg");
//    }
//
//    private void getuserId(User usera) {
//        Log.i(TAG, "getuserId: " + usera.toString());
//
//    }

//    private void testUserList() {
//        contexta = this.getApplicationContext();
//        ProxyBuilder.SimpleCallback<List<User>> callback = userList -> getUserList(userList);
//        ServerSingleton.getInstance().getUserListFromServer(contexta,callback);
//        Log.i("ag", "testUserList: asaogjaiorwjg");
//
//    }
//
//    private void getUserList(List<User> userList) {
//        Log.i("aa", "getUserList: " + userList);
//
//    }

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
                //uncomment after
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
