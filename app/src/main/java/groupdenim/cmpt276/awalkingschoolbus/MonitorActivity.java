package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        ListView listView=findViewById(R.id.listViewMonitor);
        //Buttons
        Button monitorSomeone=findViewById(R.id.btnMonitorSomeone);
        Button seeWhoIsMonitoringYou=findViewById(R.id.btnSeeWhoIsMonitoringYou);
        Button addSomeoneToMonitorYou=findViewById(R.id.btnSeeWhoIsMonitoringYou);

        List<String> students=new ArrayList<>();
        //this array is only temp, we need to acquire the list of students from singleton class which holds all stundets in it

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,students );
        listView.setAdapter(adapter);
    }

    //Intent to go back to main Activity
    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }



}
