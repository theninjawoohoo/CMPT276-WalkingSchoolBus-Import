package groupdenim.cmpt276.awalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GroupInfoActivity extends AppCompatActivity {
    private String tempName = "GroupName";
    private String[] tempMembers = {"Name1", "Name2", "Name3", "Name4", "Name5", "Name6",
            "Name7", "Name8", "Name9", "Name10", "Name11", "Name12", "Name12", "Name14", "Name15"};
    private String tempDestination = "Destination School";
    private String tempMeeting = "Meeting Street";
    private final float TEXT_SIZE = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        populateFields(tempName, R.id.linearLayout_GroupInfoActivity_groupName);
        populateFields(tempDestination, R.id.linearLayout_GroupInfoActivity_Destination);
        populateFields(tempMeeting, R.id.linearLayout_GroupInfoActivity_Meeting);
        populateList();
    }

    private void populateFields(String name, int resId) {
        LinearLayout layout = findViewById(resId);
        TextView text = new TextView(this);
        text.setText(name);
        text.setTextSize(TEXT_SIZE);
        text.setGravity(Gravity.LEFT);
        layout.addView(text);
    }

    private void populateList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.group_member, tempMembers);
        ListView list = findViewById(R.id.list_members);
        list.setAdapter(adapter);
    }
}
