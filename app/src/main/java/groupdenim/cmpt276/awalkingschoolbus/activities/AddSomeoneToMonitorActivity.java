package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.*;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;

public class AddSomeoneToMonitorActivity extends AppCompatActivity {
    //Test for tag
    CurrentUserSingleton currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser=CurrentUserSingleton.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_someone_to_monitor);

        CurrentUserSingleton.updateUserSingleton(getApplicationContext());


        final EditText emailInput = findViewById(R.id.tetxtEmailInput);
        Button back = findViewById(R.id.btnBack);
        Button confirm = findViewById(R.id.btnConfirm);


        //checks if the user input is valid. If not valid breaks out of function, else updates server and switches activity
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProxyBuilder.SimpleCallback<User> callbackUser= user -> setFieldsUserToMonitor(user);
                ServerSingleton.getInstance().getUserByEmail(getApplicationContext(),callbackUser,emailInput.getText().toString());


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void setFieldsUserToMonitor(User user)
    {
        //userToMonitor.setId(user.getId());
        long id = user.getId();
        Log.i("a", "setFieldsUserToMonitor: " + id);

        ProxyBuilder.SimpleCallback<List<User>> callback=userList->setUserList(userList);
        ServerSingleton.getInstance().monitorUsers(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId(),id);


    }

    public void setUserList(List<User> userList)
    {

        Intent intent = new Intent(AddSomeoneToMonitorActivity.this, MonitorActivity.class);
        startActivity(intent);
        finish();
        Log.i("CheckList",""+userList);

    }





//    private void getUserFromServer(User user, User userA) {
//        userA = user;
//    }

}