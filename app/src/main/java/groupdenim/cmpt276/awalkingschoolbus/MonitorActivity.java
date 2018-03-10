package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        Map<String,User> masterMap=UserSingleton.getUserMap();
        String currentUserEmail=UserSingleton.getCurrentUserEmail();
        User currentUser=masterMap.get(currentUserEmail);

        ListView listView=findViewById(R.id.listViewMonitor);

        //Buttons
        Button monitorSomeone=findViewById(R.id.btnAddMonitorSomeone);
        Button seeWhoIsMonitoringYou=findViewById(R.id.btnSeeWhoIsMonitoringYou);
        Button addSomeoneToMonitorYou=findViewById(R.id.btnAddToMonitorYou);

        //list studentsBeingMonitored is temporary
        List<String> studentsBeingMonitored=currentUser.getPeopleUserIsMonitoring();
        //the list below is to concatenate name and email
        List<String> studentsBeingMonitoredWithName=new ArrayList<>();
        for(int i=0;i<studentsBeingMonitored.size();i++)
            studentsBeingMonitoredWithName.add(masterMap.get(studentsBeingMonitored.get(i)).getUserName()+"\t\t\t\t\t\t\t"+studentsBeingMonitored.get(i));

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);


        //Buttons to switch activities on clicking them
        switchActivity(monitorSomeone,seeWhoIsMonitoringYou,addSomeoneToMonitorYou);

    }

    public void switchActivity(Button monitorSomeone,Button seeWhoIsMonitoringYou,Button addSomeoneToMonitorYou)
    {
        seeWhoIsMonitoringYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MonitorActivity.this,MonitoredByActivity.class);
                startActivity(intent);
            }
        });

        monitorSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MonitorActivity.this,AddSomeoneToMonitorActivity.class);
                startActivity(intent);
            }
        });

        addSomeoneToMonitorYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MonitorActivity.this,AddSomeoneToMonitorYouActivity.class);
                startActivity(intent);
            }
        });

    }


    //Intent to go back to main Activity
    public static Intent makeIntent(Context context){
        return new Intent(context, MainMenuActivity.class);
    }


}
