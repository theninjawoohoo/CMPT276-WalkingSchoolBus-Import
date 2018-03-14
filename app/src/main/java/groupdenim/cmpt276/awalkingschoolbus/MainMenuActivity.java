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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainMenuActivity extends AppCompatActivity {

    private Context contexta;
    private String TOKEN;
    //Some const ints
    private User currentUser = new User();
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(MainMenuActivity.this);
        //getUser();
        //getGroupList();
        //currentUser.setId((long)318);
        //testUserList();
        //getuserbyId();
        //getMonitorUser();
        //monitorUser();
        //initializeMonitor();
        //monitorUser();
        //monitoredUser();
        //getMonitored();
        //stopMonitoring();
        createGroup();
        initializeMonitor();
        if(doesGoogleMapsWork()) {
            initializeMapButton();
        }
        initializeCreateButton();
        initializeInfoButton();
        initializeLogout();

    }

    private void initializeLogout() {
        Button logout = findViewById(R.id.btnLogout);
        
    }

    private void createGroup() {
        Group group = new Group();
        group.setGroupDescription("my group");
        group.setId(3);
        User user = new User();
        user.setId(CurrentUserSingleton.getInstance(this).getId());
        group.setLeader(user);
        ProxyBuilder.SimpleCallback<Group> callback = groupa -> create(groupa);
//        //306 should be nini
        ServerSingleton.getInstance().createNewGroup(contexta,callback,group);
    }

    private void create(Group group) {
        Log.i("createdGroup", "create: "+ group);
        listGroups();
    }

    private void listGroups() {
        ProxyBuilder.SimpleCallback<List<Group>> callback = groupa -> listGrp(groupa);
//        //306 should be nini
        ServerSingleton.getInstance().getGroupList(this,callback);
    }

    private void listGrp(List<Group> groupa) {
        Log.i(TAG, "listGrp: " + groupa);
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

//    private void getUser() {
//        ProxyBuilder.SimpleCallback<User> callback = returnedUser -> response(returnedUser);
//        ServerSingleton serverSingleton = ServerSingleton.getInstance();
//        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(this);
//        serverSingleton.getUserById(this, callback, currentUserSingleton.getId());
//    }

//    private void createNewGroup() {
//        double[] routeLatArray = new double[2];
//        double[] routeLngArray = new double[2];
//        Group group = new Group("testGroup", null, routeLatArray,
//               routeLngArray, currentUser);
//
//        Log.i("LEADER_ID", "Leader id: " + currentUser.getId());
//
//        ProxyBuilder.SimpleCallback<Group> callback = returnedGroup -> response(returnedGroup);
//        ServerSingleton serverSingleton = ServerSingleton.getInstance();
//        serverSingleton.createNewGroup(MainMenuActivity.this, callback, group);
//    }

    /*private void getGroupList() {
        ProxyBuilder.SimpleCallback<List<Group>> callback = returnedList -> response(returnedList);
        ServerSingleton serverSingleton = ServerSingleton.getInstance();
        serverSingleton.getUserById(MainMenuActivity.this, callback);
    }*/


//    private void response(Group group) {
//        Log.i("GROUPRESPONSE", "response: " + group.toString());
//    }
//
//    private void response(List<Group> groups) {
//        Log.i("GROUP_LIST_RESPONSE", "response: " + groups.toString());
//    }

//    private void response(User user) {
//        Log.i("USERRESPONSE", "response: " + user.toString());
//        currentUser = user;
//        createNewGroup();
//    }

    private void response(String token) {
        TOKEN = token;
        ServerSingleton.getInstance().setToken(token);
        Log.i("DIDWEGETTOKEN", "response: " + token);
    }

}
