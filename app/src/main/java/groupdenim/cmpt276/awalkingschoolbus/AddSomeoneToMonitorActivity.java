package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.http.HTTP;

import static groupdenim.cmpt276.awalkingschoolbus.CurrentUserSingleton.setFields;

public class AddSomeoneToMonitorActivity extends AppCompatActivity {

    CurrentUserSingleton currentUser;
    User userToMonitor=new User();

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
                if (!checkEmailValidity(emailInput))
                    return;

                //add the new user to current User's List of people to monitor
                //currentUser.getPeopleUserIsMonitoring().add(emailInput.getText().toString());

                ProxyBuilder.SimpleCallback<User> callbackUser=user -> setFieldsUserToMonitor(user);
                ServerSingleton.getInstance().getUserByEmail(getApplicationContext(),callbackUser,emailInput.getText().toString());


                ProxyBuilder.SimpleCallback<List<User>> callback=userList->setUserList(userList);
                ServerSingleton.getInstance().monitorUsers(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId(),userToMonitor.getId());



                Intent intent = new Intent(AddSomeoneToMonitorActivity.this, MonitorActivity.class);
                startActivity(intent);

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSomeoneToMonitorActivity.this, MonitorActivity.class);
                startActivity(intent);
            }
        });
    }


    public void setFieldsUserToMonitor(User user)
    {
        userToMonitor.setId(user.getId());
    }

    public void setUserList(List<User> userList)
    {

    }

    //we check if emailInput is valid or not
    public boolean checkEmailValidity(EditText emailInput) {
        //add code here to check if email exists from map/list or if already a part of user's list

        //for now just assume true


        ProxyBuilder.SimpleCallback<User> callback=user -> setFields(user);
        ServerSingleton.getInstance().getUserByEmail(getApplicationContext(),callback,emailInput.getText().toString());


        //if user doesnt exist
        if (userToMonitor == null) {
            Toast toast = Toast.makeText(this, "User email does not exist", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        //if user doesnt exist
        int check = 0;
        for (int i = 0; i < currentUser.getMonitorsUsers().size(); i++) {
            if (emailInput.getText().toString().equals(currentUser.getMonitorsUsers().get(i).getEmail())) {
                ++check;
                continue;
            }
        }
        if (check == 0) {
            Toast toast = Toast.makeText(this, "User email already exists in your list", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;    //only for now
    }



//    private void getUserFromServer(User user, User userA) {
//        userA = user;
//    }

}