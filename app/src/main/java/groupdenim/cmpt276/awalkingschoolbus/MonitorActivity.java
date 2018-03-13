package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonitorActivity extends AppCompatActivity {

    Map<String,User> masterMap= ServerSingleton.getUserMap();
    String currentUserEmail= ServerSingleton.getCurrentUserEmail();
    User currentUser=masterMap.get(currentUserEmail);
    ArrayAdapter<String> adapter;
    List<String> studentsBeingMonitoredWithName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);



        ListView listView=findViewById(R.id.listViewMonitor);

        //Buttons
        Button monitorSomeone=findViewById(R.id.btnAddMonitorSomeone);
        Button seeWhoIsMonitoringYou=findViewById(R.id.btnSeeWhoIsMonitoringYou);
        Button addSomeoneToMonitorYou=findViewById(R.id.btnAddToMonitorYou);

        //list studentsBeingMonitored is temporary
        List<String> studentsBeingMonitored=currentUser.getPeopleUserIsMonitoring();

        //the list below is to concatenate name and email
        studentsBeingMonitoredWithName=new ArrayList<>();
        for(int i=0;i<studentsBeingMonitored.size();i++)
            studentsBeingMonitoredWithName.add(masterMap.get(studentsBeingMonitored.get(i)).getUserName()+"\t\t\t\t\t\t\t"+studentsBeingMonitored.get(i));

        adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        //Buttons to switch activities on clicking them
        switchActivity(monitorSomeone,seeWhoIsMonitoringYou,addSomeoneToMonitorYou);

    }

    //these two Over-ridden methods are to delete people
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_file,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo obj=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.delete:
                //deletes current user from other person's list
                User someoneBeingMonitoredByCurrentUser=masterMap.get(currentUser.getPeopleUserIsMonitoring().get(obj.position));
                someoneBeingMonitoredByCurrentUser.getPeopleMonitoringUser().remove(currentUserEmail);

                //delete from current user's list
                currentUser.getPeopleUserIsMonitoring().remove(obj.position);
                //delete from display list
                studentsBeingMonitoredWithName.remove(obj.position);

                adapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void switchActivity(Button monitorSomeone, Button seeWhoIsMonitoringYou, Button addSomeoneToMonitorYou)
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
