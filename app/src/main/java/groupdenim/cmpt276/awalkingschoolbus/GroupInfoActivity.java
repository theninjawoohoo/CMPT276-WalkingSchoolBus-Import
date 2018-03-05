package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GroupInfoActivity extends AppCompatActivity {
    private String tempName = "GroupName";
    private String[] tempMembers = {"Name1", "Name2", "Name3", "Name4", "Name5", "Name6",
            "Name7", "Name8", "Name9", "Name10", "Name11", "Name12", "Name12", "Name14", "Name15"};
    private String tempDestination = "Destination School";
    private String tempMeeting = "Meeting Street";
    private boolean tempIsInGroup = true;
    private boolean tempHasChildren = true;
    private final float TEXT_SIZE = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        populateFields(tempName, R.id.linearLayout_GroupInfoActivity_groupName);
        populateFields(tempDestination, R.id.linearLayout_GroupInfoActivity_Destination);
        populateFields(tempMeeting, R.id.linearLayout_GroupInfoActivity_Meeting);
        populateList();
        createAppropriateButtons();
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

    private void createAppropriateButtons() {
        LinearLayout layout = findViewById(R.id.hlinear_layout_group_buttons);
        if (tempIsInGroup) {
            Button button = new Button(this);
            button.setText(R.string.leave);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Diologue box asking "Are you sure you want to join *name of group*?"
                }
            });
            layout.addView(button);
        } else {
            Button button = new Button(this);
            button.setText(R.string.join);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Diologue box asking "Are you sure you want to leave *name of group*?"
                }
            });
            layout.addView(button);
        }

        if (tempHasChildren) {
            Button buttonAdd = new Button(this);
            buttonAdd.setText(R.string.add);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Diologue box asking which child to add to the group
                }
            });
            layout.addView(buttonAdd);

            Button buttonRemove = new Button(this);
            buttonRemove.setText(R.string.remove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Diologue box asking which child to remove from the group
                }
            });
            layout.addView(buttonRemove);
        }
    }
}
