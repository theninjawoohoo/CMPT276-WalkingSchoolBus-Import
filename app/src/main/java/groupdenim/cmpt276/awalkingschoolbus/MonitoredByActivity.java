package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

public class MonitoredByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored_by);

        ListView listView=findViewById(R.id.listViewBeingMonitoredBy);

        Map<String,User> masterMap=UserSingleton.getUserMap();
        String currentUserEmail=UserSingleton.getCurrentUserEmail();
        User currentUser=masterMap.get(currentUserEmail);

        List<String> peopleMonitoringUser=currentUser.getPeopleMonitoringUser();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,peopleMonitoringUser );
        listView.setAdapter(adapter);


        Button goBack=findViewById(R.id.goBackBtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MonitoredByActivity.this,MonitorActivity.class);
                startActivity(intent);
            }
        });


    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MonitorActivity.class);
    }



}
