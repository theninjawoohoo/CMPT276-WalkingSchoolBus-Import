package groupdenim.cmpt276.awalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class ListUsersYouMonitor extends AppCompatActivity {
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users_you_monitor);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.userinfo,list);
        ProxyBuilder.SimpleCallback<List<User>> getlist = userList -> getMonitorList(userList);
        ServerSingleton.getInstance().getMonitorUsers(this,getlist, CurrentUserSingleton.getInstance(this).getId());
    }

    private void getMonitorList(List<User> userList) {
        for (User user : userList) {
            list.add(user.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.userinfo,list);
        ListView lw = findViewById(R.id.listView_test);
        lw.setAdapter(adapter);
        Log.i("a", "getMonitorList: " + userList);
    }
}
