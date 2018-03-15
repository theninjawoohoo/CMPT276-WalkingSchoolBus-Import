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

public class MonitoredByActivity extends AppCompatActivity {


    CurrentUserSingleton currentUser;
    public static List<String> studentsBeingMonitoredWithName;  //list to be displayed
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser=CurrentUserSingleton.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored_by);

        updateListWhichDisplaysUsers();

        listView=findViewById(R.id.listViewBeingMonitoredBy);

        //list peopleMonitoringUser is temporary



        adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        Button goBack=findViewById(R.id.goBackBtn);
        goBackButton(goBack);

    }

    public void updateListWhichDisplaysUsers()
    {
        studentsBeingMonitoredWithName=new ArrayList<>();
        ProxyBuilder.SimpleCallback<List<User>> callback=userList -> getMonitorList(userList);
        ServerSingleton.getInstance().getMonitoredUsers(getApplicationContext(),callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId());
    }

    private void getMonitorList(List<User> userList)
    {
        for(User user : userList)
            studentsBeingMonitoredWithName.add(user.getName()+"  "+user.getEmail());

        adapter=new ArrayAdapter<String>(this,
                R.layout.student_in_list,studentsBeingMonitoredWithName );
        listView.setAdapter(adapter);
    }

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

                int index=obj.position;
                ProxyBuilder.SimpleCallback<List<User>> callback=userList -> temp(userList,index);
                ServerSingleton.getInstance().getMonitorUsers(getApplicationContext(),
                        callback,CurrentUserSingleton.getInstance(getApplicationContext()).getId());

                adapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void temp(List<User> userList, int index) {
        Long id=userList.get(index).getId();

        ProxyBuilder.SimpleCallback<Void> callback=tempo->setUserList(tempo,index);
        ServerSingleton.getInstance().stopBeingMonitored(getApplicationContext(),callback,id,CurrentUserSingleton.getInstance(getApplicationContext()).getId());
    }

    private void setUserList(Void tempo, int index) {
        studentsBeingMonitoredWithName.remove(index);
        adapter.notifyDataSetChanged();
    }


    public void goBackButton(Button goBack)
    {
        goBack.setOnClickListener(view -> {
            Intent intent=new Intent(MonitoredByActivity.this,MonitorActivity.class);
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MonitorActivity.class);
    }



}
