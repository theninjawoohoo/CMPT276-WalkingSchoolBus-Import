package groupdenim.cmpt276.awalkingschoolbus.activities;

//DO NOT COMMIT THIS FILE!
//This Maps test code was provided by:
//Tutorials used below
//https://www.youtube.com/watch?v=M0bYvXlhgSI

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;

public class MainMenuActivity extends AppCompatActivity {

    private static Context contexta;
    private String TOKEN;
    //Some const ints
    private User currentUser = new User();
    private static final String TAG = "MainActivity";
    private static final String LOGIN = "";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initializeMonitor();
        if(doesGoogleMapsWork()) {
            initializeMapButton();
        }
        initializeLogout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setEnabled(true);
    }


    private void initializeLogout() {
        Button logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }


    private void logout() {
        SharedPreferences sharedPrefs = getSharedPreferences(LOGIN, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email","");
        editor.putString("password","");
        editor.commit();
        finish();

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
                ProxyBuilder.SimpleCallback<List<Group>> callback = groups -> groupListResponse(groups);
                ServerSingleton.getInstance().getGroupList(MainMenuActivity.this, callback);
                btnMap.setEnabled(false);
            }
        });
    }

    private void groupListResponse(List<Group> groups) {
        MapSingleton mapSingleton = MapSingleton.getInstance();
        mapSingleton.convertGroupsToMeetingPlaces(groups);
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
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

}
