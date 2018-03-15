package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

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
                if(!checkEmailValidity(emailInput))
                    return;

                ProxyBuilder.SimpleCallback<User> callbackUser=user -> setFieldsUserToMonitorBySomeoneElse(user);
                ServerSingleton.getInstance().getUserByEmail(getApplicationContext(),callbackUser,emailInput.getText().toString());


                //only implemented after checkEmailValidity works

                Intent intent=new Intent(AddSomeoneToMonitorYouActivity.this,MonitorActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddSomeoneToMonitorYouActivity.this,MonitorActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setFieldsUserToMonitorBySomeoneElse(User user) {
        long id = user.getId();

        ProxyBuilder.SimpleCallback<List<User>> callback=userList->setUserList(userList);
        ServerSingleton.getInstance().monitoredByUsers(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId(),id);
    }

    public void setUserList(List<User> userList)
    {
        Intent intent = new Intent(AddSomeoneToMonitorYouActivity.this, MonitorActivity.class);
        startActivity(intent);
        finish();
        Log.i("CheckList",""+userList);
    }

    public boolean checkEmailValidity(EditText emailInput)
    {
        //add code here to check if email exists from map/list or if already a part of user's list


        /*
        //checks if input email even exists
        User userExistenseCheck=masterMap.get(emailInput.getText().toString());
        if(userExistenseCheck==null)
        {
            Toast toast = Toast.makeText(this,"User email does not exist",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        //checks for duplicate within existing user's list
        List<String> list=currentUser.getPeopleMonitoringUser();
        for(int i=0;i<list.size();i++)
        {
            if(emailInput.getText().toString().equals(list.get(i)))
            {
                Toast toast = Toast.makeText(this,"User email already exists in your list",Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }
        */


        return true;    //only for now
    }



}
