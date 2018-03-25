package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;


/**
 * This class generates a arraylist of place objects you
 */


public class GroupMeetingActivity extends AppCompatActivity{
    private Button switchMap;
    private ListView listView;
    private List<Group> aListOfGroups;
    private EditText theSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmeeting);
        switchMap = (Button) findViewById(R.id.btn_on_screen_two_map);
        theSearchBox = (EditText) findViewById(R.id.input_search_group);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        hideSoftKeyboard();
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

        for (Group aGroup : aListOfGroups) {
            if (aGroup.getGroupDescription() != null) {
                aListOfDescriptions.add(aGroup.getGroupDescription());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                R.layout.groupitem,
                                                                aListOfDescriptions);
        listView = (ListView) findViewById(R.id.list_view_group);
        listView.setAdapter(adapter);

        theSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged (Editable editable) {

            }
        });

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

    //Hides the keyboard upon searching/tapping.
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(theSearchBox.getWindowToken(), 0);
    }

}
