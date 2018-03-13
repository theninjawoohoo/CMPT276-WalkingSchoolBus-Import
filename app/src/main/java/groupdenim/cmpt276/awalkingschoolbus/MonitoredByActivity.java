//package groupdenim.cmpt276.awalkingschoolbus;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.ContextMenu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class MonitoredByActivity extends AppCompatActivity {
//
//    Map<String,User> masterMap= ServerSingleton.getUserMap();
//    String currentUserEmail= ServerSingleton.getCurrentUserEmail();
//    User currentUser=masterMap.get(currentUserEmail);
//    List<String> peopleMonitoringUserWithName;  //list to be displayed
//    ArrayAdapter<String> adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_monitored_by);
//
//        ListView listView=findViewById(R.id.listViewBeingMonitoredBy);
//
//        //list peopleMonitoringUser is temporary
//        List<String> peopleMonitoringUser= currentUserServer.getPeopleMonitoringUser();
//        //the list below is to concatenate name and email
//        peopleMonitoringUserWithName=new ArrayList<>();
//        for(int i=0;i<peopleMonitoringUser.size();i++)
//            peopleMonitoringUserWithName.add(masterMap.get(peopleMonitoringUser.get(i)).getUserName()+"\t\t\t\t\t\t\t"+peopleMonitoringUser.get(i));
//
//
//        adapter=new ArrayAdapter<String>(this,
//                R.layout.student_in_list,peopleMonitoringUserWithName );
//        listView.setAdapter(adapter);
//        registerForContextMenu(listView);
//
//        Button goBack=findViewById(R.id.goBackBtn);
//        goBackButton(goBack);
//
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater=getMenuInflater();
//        menuInflater.inflate(R.menu.context_menu_file,menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo obj=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId())
//        {
//            case R.id.delete:
//                User someoneMonitoringCurrentUserServer =masterMap.get(currentUserServer.getPeopleMonitoringUser().get(obj.position));
//                someoneMonitoringCurrentUserServer.getPeopleUserIsMonitoring().remove(currentUserEmail);
//
//                currentUserServer.getPeopleMonitoringUser().remove(obj.position);
//                peopleMonitoringUserWithName.remove(obj.position);
//
//                adapter.notifyDataSetChanged();
//                break;
//        }
//
//        return super.onContextItemSelected(item);
//    }
//
//    public void goBackButton(Button goBack)
//    {
//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MonitoredByActivity.this,MonitorActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    public static Intent makeIntent(Context context){
//        return new Intent(context, MonitorActivity.class);
//    }
//
//
//
//}
