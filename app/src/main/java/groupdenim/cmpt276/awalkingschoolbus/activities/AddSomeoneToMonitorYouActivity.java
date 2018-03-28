package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class AddSomeoneToMonitorYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_someone_to_monitor_you);


        final EditText emailInput=findViewById(R.id.tetxtEmailInput);
        Button back=findViewById(R.id.btnBack);
        Button confirm=findViewById(R.id.btnConfirm);

        //checks if the user input is valid. If not valid breaks out of function, else updates server and switches activity
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProxyBuilder.SimpleCallback<User> callbackUser= user -> setFieldsUserToMonitorBySomeoneElse(user);
                ServerSingleton.getInstance().getUserByEmail(getApplicationContext(),callbackUser,emailInput.getText().toString());
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setFieldsUserToMonitorBySomeoneElse(User user) {
        long id = user.getId();

        ProxyBuilder.SimpleCallback<List<User>> callback=userList->setUserList(userList);
        ServerSingleton.getInstance().monitoredByUsers(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getId(),id);


        //adds user to be monitored by to singleton
        CurrentUserSingleton.getInstance(getApplicationContext()).getMonitoredByUsers().add(user);
    }

    public void setUserList(List<User> userList)
    {
        finish();
        Log.i("CheckList",""+userList);
    }


}
