package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

public class AddSomeoneToMonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_someone_to_monitor);

        final Map<String,User> masterMap=UserSingleton.getUserMap();
        final String currentUserEmail=UserSingleton.getCurrentUserEmail();
        final User currentUser=masterMap.get(currentUserEmail);

        final EditText emailInput=findViewById(R.id.tetxtEmailInput);
        Button back=findViewById(R.id.btnBack);
        Button confirm=findViewById(R.id.btnConfirm);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkEmailValidity(emailInput))
                    return;

                //continue
                currentUser.getPeopleUserIsMonitoring().add(emailInput.getText().toString());


                /*
                //only implemented after checkEmailValidity works

                //add current user to the user being monitored by someone else
                User someoneBeingMonitoredByCurrentUser=masterMap.get(emailInput);
                someoneBeingMonitoredByCurrentUser.getPeopleMonitoringUser().add(currentUserEmail);
                */

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
    public boolean checkEmailValidity(EditText emailInput)
    {
        //add code here to check if email exists from map/list or if already a part of user's list



        return true;    //only for now
    }
}
