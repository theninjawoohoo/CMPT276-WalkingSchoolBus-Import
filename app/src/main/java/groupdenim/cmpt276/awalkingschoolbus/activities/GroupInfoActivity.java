package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoAddFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoDeleteFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoJoinFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoLeaveFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoRemoveFragment;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.R;

public class GroupInfoActivity extends AppCompatActivity {
    private long groupToDisplayId = 0;
    private Group groupToDisplay = new Group();
    private List<User> membersOfGroup = new ArrayList<>();
    private User groupLeader;
    private final float TEXT_SIZE = 24;
    private static final String PUT_EXTRA = "groupdenim.cmpt276.awalkingschoolbus - double";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        getGroupToDisplayIdFromIntent();
        getGroupFromServer();
    }

    private void getGroupToDisplayIdFromIntent() {
        Intent intent = getIntent();
        groupToDisplayId = intent.getLongExtra(PUT_EXTRA, 0);
    }

    private void getGroupFromServer() {
        ProxyBuilder.SimpleCallback<Group> callback = returnedGroup -> getGroupResponse(returnedGroup);
        ServerSingleton.getInstance().getGroupById(this, callback, groupToDisplayId);
    }

    private void getGroupResponse(Group group){
        groupToDisplay = group;

        ProxyBuilder.SimpleCallback<User> callback = returnedUser ->
                getGroupLeaderResponse(returnedUser);
        ServerSingleton.getInstance().getUserById(this, callback, group.getLeader().getId());
    }

    private void getGroupLeaderResponse(User user) {
        groupToDisplay.setLeader(user);
        groupLeader = user;

        getUsersInGroupFromServer();
    }

    //TODO: Not sure if this will work. I am trying to make each user make a call and wait for each one to come back before moving on
    private void getUsersInGroupFromServer() {
        if (groupToDisplay.getMemberUsers().size() > 0) {
            for (User user : groupToDisplay.getMemberUsers()) {
                ProxyBuilder.SimpleCallback<User> callback = returnedUser -> getUsersResponse(returnedUser);
                ServerSingleton.getInstance().getUserById(this, callback, user.getId());
            }
        } else {
            String meetingSpot = "Not Specified";
            String destinationSpot = "Not Specified";
            try {
                meetingSpot = geoEncoder(groupToDisplay.getRouteLatArray()[0],
                                                groupToDisplay.getRouteLngArray()[0]);
                destinationSpot = geoEncoder(groupToDisplay.getRouteLatArray()[1],
                                                groupToDisplay.getRouteLngArray()[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //All members have been added to the membersOfGroup list, now containing their emails
            populateFields(groupToDisplay.getGroupDescription(),
                    R.id.linearLayout_GroupInfoActivity_GroupDescription);
            populateFields(groupToDisplay.getLeader().getEmail(),
                R.id.linearLayout_GroupInfoActivity_GroupLeader);

            populateFields(meetingSpot,
                    R.id.linearLayout_GroupInfoActivity_Meeting); //TEMP
            populateFields(destinationSpot,
                    R.id.linearLayout_GroupInfoActivity_Destination);
            populateList();
            createAppropriateButtons();
        }
    }

    private void getUsersResponse(User user) {
        membersOfGroup.add(user);
        Log.i("CHECKING_GROUP_RESPONSE", "Got group response!");

        String meetingSpot = "Meeting Not Specified";
        String destinationSpot = "Destination Not Specified";
        try {
            meetingSpot = geoEncoder(groupToDisplay.getRouteLatArray()[0],
                    groupToDisplay.getRouteLngArray()[0]);
            destinationSpot = geoEncoder(groupToDisplay.getRouteLatArray()[1],
                    groupToDisplay.getRouteLngArray()[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Once needed data is retrieved from server, display the information
        if (membersOfGroup.size() == groupToDisplay.getMemberUsers().size() ||
            groupToDisplay.getMemberUsers().size() == 0) {
            //All members have been added to the membersOfGroup list, now containing their emails
            populateFields(groupToDisplay.getGroupDescription(),
                    R.id.linearLayout_GroupInfoActivity_GroupDescription);
            populateFields(groupToDisplay.getLeader().getEmail(),
                    R.id.linearLayout_GroupInfoActivity_GroupLeader);
            populateFields(meetingSpot,
                    R.id.linearLayout_GroupInfoActivity_Meeting); //TEMP
            populateFields(destinationSpot,
                    R.id.linearLayout_GroupInfoActivity_Destination);
            populateList();
            createAppropriateButtons();
        }
    }

    public void updateUi() {
        Log.i("UPDATE_UI", "fjkrwnfkj");
        clearUi();
        String meetingSpot = "Not Specified";
        String destinationSpot = "Not Specified";
        try {
            meetingSpot = geoEncoder(groupToDisplay.getRouteLatArray()[0],
                    groupToDisplay.getRouteLngArray()[0]);
            destinationSpot = geoEncoder(groupToDisplay.getRouteLatArray()[1],
                    groupToDisplay.getRouteLngArray()[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateGroupToDisplay();
        populateFields(groupToDisplay.getGroupDescription(),
                R.id.linearLayout_GroupInfoActivity_GroupDescription);
        populateFields(groupToDisplay.getLeader().getEmail(),
                R.id.linearLayout_GroupInfoActivity_GroupLeader);
        populateFields(meetingSpot,
                R.id.linearLayout_GroupInfoActivity_Meeting); //TEMP
        populateFields(destinationSpot,
                R.id.linearLayout_GroupInfoActivity_Destination);
        populateList();
        createAppropriateButtons();
    }

    private void clearUi() {
        final int FIELD_INDEX = 1;
        LinearLayout layout;
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_GroupDescription);
        layout.removeViewAt(FIELD_INDEX);
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_GroupLeader);
        layout.removeViewAt(FIELD_INDEX);
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_Destination);
        layout.removeViewAt(FIELD_INDEX);
        layout = findViewById(R.id.linearLayout_GroupInfoActivity_Meeting);
        layout.removeViewAt(FIELD_INDEX);

        layout = findViewById(R.id.hlinearLayout_GroupInfoActivity_Buttons);
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
        List<String> memberEmails = new ArrayList<>();
        for (User user : membersOfGroup) {
            memberEmails.add(user.getEmail());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.group_member, memberEmails);
        ListView list = findViewById(R.id.list_members);
        list.setAdapter(adapter);
    }

    private void updateGroupToDisplay() {
        groupToDisplay.setMemberUsers(membersOfGroup);
    }

    private void createAppropriateButtons() {
        List<Long> memberIds = new ArrayList<>();
        for (User user : groupToDisplay.getMemberUsers()) {
            memberIds.add(user.getId());
        }

        LinearLayout layout = findViewById(R.id.hlinearLayout_GroupInfoActivity_Buttons);
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance(GroupInfoActivity.this);

        if (!groupLeader.getId().equals(userSingleton.getId())) {
            //Check if the current user is in the group or not
            boolean isInGroup = false;
            for (Group group : userSingleton.getMemberOfGroups()) {
                if (group.getId() == groupToDisplay.getId()) {
                    isInGroup = true;
                    break;
                }
            }
            if (isInGroup) {
                createLeaveButton(layout);
            } else {
                createJoinButton(layout);
            }
        } else {
            createDeleteButton(layout);
        }

        //Check if the user is monitoring anyone and create the appropriate buttons
        List<User> monitoringList = userSingleton.getMonitorsUsers();
        if (monitoringList.size() > 0) {
            //Get a list of the ids for comparing
            List<Long> monitorListId = new ArrayList<>();
            for (User user : monitoringList) {
                if (!user.getId().equals(groupLeader.getId())) {
                    monitorListId.add(user.getId());
                }
            }

            //Check if the current user is monitoring a user who is not in the group
            boolean hasChildrenNotInGroup = false;
            for (Long monitoredId : monitorListId) {
                if (!memberIds.contains(monitoredId)) {
                    hasChildrenNotInGroup = true;
                }
            }
            if (hasChildrenNotInGroup) {
                createAddButton(layout);
            }

            //Check if the current user is monitoring a user who is in the group
            boolean hasChildrenInGroup = false;
            for (Long monitoredId : monitorListId) {
                if (memberIds.contains(monitoredId)) {
                    hasChildrenInGroup = true;
                }
            }
            if (hasChildrenInGroup) {
                createRemoveButton(layout);
            }
        }
    }

    private void createDeleteButton(LinearLayout layout) {
        Button button = new Button(this);
        button.setText(R.string.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoDeleteFragment dialog = new GroupInfoDeleteFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(button);
    }

    private void createLeaveButton(LinearLayout layout) {
        Button button = new Button(this);
        button.setText(R.string.leave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());
                bundle.putLong("userId",
                        CurrentUserSingleton.getInstance(GroupInfoActivity.this).getId());

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
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());
                bundle.putLong("userId",
                        CurrentUserSingleton.getInstance(GroupInfoActivity.this).getId());

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
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());
                bundle.putLong("leaderId", groupLeader.getId());

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
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());
                bundle.putLong("leaderId", groupLeader.getId());

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoRemoveFragment dialog = new GroupInfoRemoveFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
        layout.addView(buttonRemove);
    }

    public void setMembersOfGroup(List<User> membersOfGroup) {
        this.membersOfGroup = membersOfGroup;
    }

    public List<User> getMembersOfGroup() {
        return this.membersOfGroup;
    }

    public Group getGroupToDisplay() {
        return this.groupToDisplay;
    }

    public static Intent makeIntent(Context context, long id) {
        Intent intent = new Intent(context, GroupInfoActivity.class);
        intent.putExtra(PUT_EXTRA, id);

        return intent;
    }

    private String geoEncoder(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if(addresses.size() == 0) {
            return "Not Specified/Coordinates Don't exist.";
        }

        return addresses.get(0).getAddressLine(0);
    }

}
