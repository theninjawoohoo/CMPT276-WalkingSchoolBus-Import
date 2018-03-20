package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class MonitorActivity extends AppCompatActivity {

    public static List<String> studentsBeingMonitoredWithName=new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        updateListWhichDisplaysUsers();

        TextView txtDisplay=findViewById(R.id.txtDisplayEmail);
        txtDisplay.setText("Your name, email and id is "+CurrentUserSingleton.getInstance(getApplicationContext()).getName()+
                ", "+CurrentUserSingleton.getInstance(getApplicationContext()).getEmail()+
                " & "+CurrentUserSingleton.getInstance(getApplicationContext()).getId());

        listView=findViewById(R.id.listViewMonitor);

        //Buttons
        Button monitorSomeone=findViewById(R.id.btnAddMonitorSomeone);
        Button seeWhoIsMonitoringYou=findViewById(R.id.btnSeeWhoIsMonitoringYou);
        Button addSomeoneToMonitorYou=findViewById(R.id.btnAddToMonitorYou);
        Button backBtn=findViewById(R.id.btnBack);

        adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        //Buttons to switch activities on clicking them
        switchActivity(monitorSomeone,seeWhoIsMonitoringYou,addSomeoneToMonitorYou,backBtn);

    }


    public void updateListWhichDisplaysUsers()
    {
        //CurrentUserSingleton.updateUserSingleton(getApplicationContext());
        studentsBeingMonitoredWithName=new ArrayList<>();
        ProxyBuilder.SimpleCallback<List<User>> callback= userList -> getMonitorList(userList);
        ServerSingleton.getInstance().getMonitorUsers(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId());

    }

    public void getMonitorList(List<User> userList)
    {
        for(User user : userList)
            studentsBeingMonitoredWithName.add(user.getName()+"  "+user.getEmail());

        adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);
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

                int index=obj.position;
                ProxyBuilder.SimpleCallback<List<User>> callback=userList -> temp(userList,index);
                ServerSingleton.getInstance().getMonitorUsers(getApplicationContext(),
                        callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId());



                break;
        }

        return super.onContextItemSelected(item);
    }

    public void temp(List<User> userList,int index)
    {
        Long id=userList.get(index).getId();

        ProxyBuilder.SimpleCallback<Void> callback=tempo->setUserList(tempo,index);
        ServerSingleton.getInstance().stopMonitoringUser(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId(),id);

    }

    private void setUserList(Void tempo,int index) {
        studentsBeingMonitoredWithName.remove(index);
        adapter.notifyDataSetChanged();
    }


    public void switchActivity(Button monitorSomeone, Button seeWhoIsMonitoringYou, Button addSomeoneToMonitorYou,Button backBtn)
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

        //go back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



}
