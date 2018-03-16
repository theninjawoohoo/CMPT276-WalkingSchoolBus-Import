package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.mapModels.MapSingleton;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.placeObject;


/**
 * This class generates a arraylist of place objects you
 */


public class GroupMeetingActivity extends AppCompatActivity{
    private Button switchMap;
    private ListView listView;
    private List<Group> aListOfGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmeeting);
        switchMap = (Button) findViewById(R.id.btn_on_screen_two_map);

        initialize();
    }

    private void initialize() {
        serverRetrieve();

        switchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(GroupMeetingActivity.this, MapActivity.class);
                startActivity(mapIntent);
                finish();
            }
        });
        registerListClickCallback();
    }

    private void listViewResponse(List<Group> aListOfGroups) {
        this.aListOfGroups = aListOfGroups;
        populateListView();
    }


    private void serverRetrieve() {
        ProxyBuilder.SimpleCallback <List<Group>> callback = aBunchOfGroups -> listViewResponse(aBunchOfGroups);
        ServerSingleton.getInstance().getGroupList(this, callback);
    }

    private void populateListView() {
        List<String> aListOfDescriptions = new ArrayList<>();

        for(Group aGroup : aListOfGroups) {
            if(aGroup.getGroupDescription() != null){
                aListOfDescriptions.add(aGroup.getGroupDescription());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                R.layout.groupitem,
                                                                aListOfDescriptions);
        listView = (ListView) findViewById(R.id.list_view_group);
        listView.setAdapter(adapter);

    }

    private void registerListClickCallback() {
        listView = (ListView) findViewById(R.id.list_view_group);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = 0;
                TextView TextView = (TextView) view;
                String description = TextView.getText().toString();
                for(Group aGroup : aListOfGroups) {
                    if(aGroup.getGroupDescription().equals(description)) {
                        id = aGroup.getId();
                        break;
                    }
                }
                Intent intent = GroupInfoActivity.makeIntent(GroupMeetingActivity.this, id);
                startActivity(intent);
            }
        });
    }

}
