package groupdenim.cmpt276.awalkingschoolbus;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {
    private String tempName = "TestGroup";
    //private Group group = GroupSingleton.getInstance().getGroup(tempName); //Name should be passed in
    private String[] tempMembers = {"Name1", "Name2", "Name3", "Name4", "Name5", "Name6",
            "Name7", "Name8", "Name9",};
    private String tempDestination = "School";
    private String tempMeeting = "MyHouse";
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

    public void updateUi() {
        clearUi();
        populateFields(tempName, R.id.linearLayout_GroupInfoActivity_groupName);
        populateFields(tempDestination, R.id.linearLayout_GroupInfoActivity_Destination);
        populateFields(tempMeeting, R.id.linearLayout_GroupInfoActivity_Meeting);
        populateList();
        createAppropriateButtons();
    }

    private void clearUi() {
        final int FIELD_INDEX = 1;
        LinearLayout layout;
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_groupName);
        layout.removeViewAt(FIELD_INDEX);
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_Destination);
        layout.removeViewAt(FIELD_INDEX);
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_Meeting);
        layout.removeViewAt(FIELD_INDEX);

        layout = findViewById(R.id.hlinear_layout_group_buttons);
        layout.removeAllViews();
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
//
//        UserSingleton userSingleton = UserSingleton.getInstance();
//        final String userEmail = userSingleton.getCurrentUserEmail();
//        User user = userSingleton.getUserMap().get(userEmail);
//
//        final int USER_GROUPS_SIZE = user.getGroups().size();
//        boolean isInGroup = (USER_GROUPS_SIZE > 0);
//        if (isInGroup) {
//            createLeaveButton(layout);
//        } else {
//            createJoinButton(layout);
//        }
//
//        List<String> monitoringList = user.getPeopleUserIsMonitoring();
//        final int USER_CHILDREN_SIZE = monitoringList.size();
//        boolean hasChildren = (USER_CHILDREN_SIZE > 0);
//        if (hasChildren) {
//            createAddButton(layout);
//        }
//
//        boolean hasChildrenInGroup = false;
//        for (String child : monitoringList) {
//            for (String member : group.getMembers()) {
//                if (child.equals(member)) {
//                    hasChildrenInGroup = true;
//                    break;
//                }
//            }
//        }
        //if (hasChildrenInGroup) {
            createRemoveButton(layout);
        //}
    }

    private void createLeaveButton(LinearLayout layout) {
        Button button = new Button(this);
        button.setText(R.string.leave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", tempName);

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoLeaveFragment dialog = new GroupInfoLeaveFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(button);
    }

    private void createJoinButton(LinearLayout layout) {
        Button button = new Button(this);
        button.setText(R.string.join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", tempName);

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoJoinFragment dialog = new GroupInfoJoinFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(button);
    }

    private void createAddButton(LinearLayout layout) {
        Button buttonAdd = new Button(this);
        buttonAdd.setText(R.string.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", tempName);

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoAddFragment dialog = new GroupInfoAddFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(buttonAdd);
    }

    private void createRemoveButton(LinearLayout layout) {
        Button buttonRemove = new Button(this);
        buttonRemove.setText(R.string.remove);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", tempName);

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoRemoveFragment dialog = new GroupInfoRemoveFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(buttonRemove);
    }
}
