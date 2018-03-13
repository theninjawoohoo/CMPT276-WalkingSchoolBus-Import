package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class AddSomeoneToMonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_someone_to_monitor);

        final Map<String,User> masterMap= ServerSingleton.getUserMap();
        final String currentUserEmail= ServerSingleton.getCurrentUserEmail();
        final User currentUser=masterMap.get(currentUserEmail);

        final EditText emailInput=findViewById(R.id.tetxtEmailInput);
        Button back=findViewById(R.id.btnBack);
        Button confirm=findViewById(R.id.btnConfirm);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkEmailValidity(emailInput,currentUser,masterMap))
                    return;

                //continue
                currentUser.getPeopleUserIsMonitoring().add(emailInput.getText().toString());



                //only implemented after checkEmailValidity works

                //add current user to the user being monitored by someone else
                User someoneBeingMonitoredByCurrentUser=masterMap.get(emailInput.getText().toString());
                someoneBeingMonitoredByCurrentUser.getPeopleMonitoringUser().add(currentUserEmail);


                Intent intent=new Intent(AddSomeoneToMonitorActivity.this,MonitorActivity.class);
                startActivity(intent);

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddSomeoneToMonitorActivity.this,MonitorActivity.class);
                startActivity(intent);
            }
        });
    }


    //we check if emailInput is valid or not
    public boolean checkEmailValidity(EditText emailInput,User currentUser,Map<String,User> masterMap)
    {
        //add code here to check if email exists from map/list or if already a part of user's list


        //checks if input email even exists
        User userExistenseCheck=masterMap.get(emailInput.getText().toString());
        if(userExistenseCheck==null)
        {
            Toast toast = Toast.makeText(this,"User email does not exist",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }


        //checks for duplicate within existing user's list
        List<String> list=currentUser.getPeopleUserIsMonitoring();
        for(int i=0;i<list.size();i++)
        {
            if(emailInput.getText().toString().equals(list.get(i)))
            {
                Toast toast = Toast.makeText(this,"User email already exists in your list",Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

        }


        return true;    //only for now
    }
}
