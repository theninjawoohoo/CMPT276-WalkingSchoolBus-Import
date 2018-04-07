package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoAddFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoDeleteFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoJoinFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoLeadFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoLeaderLeaveFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoLeaveFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoRemoveFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupMemberInfoFragment;
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
        if (group.getLeader() != null) {
            ServerSingleton.getInstance().getUserById(this, callback, group.getLeader().getId());
        } else {
            getUsersInGroupFromServer();
        }
    }

    private void getGroupLeaderResponse(User user) {
        groupToDisplay.setLeader(user);
        groupLeader = user;

        getUsersInGroupFromServer();
    }

    private void getUsersInGroupFromServer() {
        if (groupToDisplay.getMemberUsers().size() > 0) {
            for (User user : groupToDisplay.getMemberUsers()) {
                ProxyBuilder.SimpleCallback<User> callback = returnedUser -> getUsersResponse(returnedUser);
                ServerSingleton.getInstance().getUserById(this, callback, user.getId());
            }
        } else {
            populateTextViews();
            populateList();
            createAppropriateButtons();
        }
    }

    private void populateTextViews() {
        populateFields(groupToDisplay.getGroupDescription(),
                R.id.TextView_GroupInfoActivity_GroupDescription);
        if (groupLeader != null) {
            populateFields(groupToDisplay.getLeader().getEmail(),
                    R.id.TextView_GroupInfoActivity_GroupLeader);
        } else {
            populateFields("No leader",
                    R.id.TextView_GroupInfoActivity_GroupLeader);
        }

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
        populateFields(destinationSpot,
                R.id.TextView_GroupInfoActivity_Destination);
        populateFields(meetingSpot,
                R.id.TextView_GroupInfoActivity_Meeting);
    }

    private void getUsersResponse(User user) {
        membersOfGroup.add(user);
        Log.i("CHECKING_GROUP_RESPONSE", "Got group response!");

        //Once needed data is retrieved from server, display the information
        if (membersOfGroup.size() == groupToDisplay.getMemberUsers().size() ||
                groupToDisplay.getMemberUsers().size() == 0) {
            //All members have been added to the membersOfGroup list, now containing their emails
            populateTextViews();
            populateList();
            createAppropriateButtons();
        }
    }

    public void updateUi() {
        Log.i("UPDATE_UI", "fjkrwnfkj");
        clearUi();
        updateGroupToDisplay();
        populateTextViews();
        populateList();
        createAppropriateButtons();
    }

    private void clearUi() {
        TextView text;
        text = findViewById(R.id.TextView_GroupInfoActivity_GroupDescription);
        text.setText(R.string.description_colon);
        text = findViewById(R.id.TextView_GroupInfoActivity_GroupLeader);
        text.setText(R.string.leader_colon);
        text = findViewById(R.id.TextView_GroupInfoActivity_Destination);
        text.setText(R.string.destination_colon);
        text = findViewById(R.id.TextView_GroupInfoActivity_Meeting);
        text.setText(R.string.meeting_colon);

        LinearLayout layout = findViewById(R.id.hlinearLayout_GroupInfoActivity_Buttons);
        layout.removeAllViews();
    }

    private void populateFields(String textToAdd, int resId) {
        TextView text = findViewById(resId);
        String currentText = text.getText().toString();
        text.setText(currentText + " " + textToAdd);
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
        registerOnClickCallBack();
    }

    private void registerOnClickCallBack() {
        ListView list = findViewById(R.id.list_members);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                CurrentUserSingleton currentUser =
                        CurrentUserSingleton.getInstance(GroupInfoActivity.this);
                boolean canView = false;
                // Check if they are a leader
                if (groupToDisplay.getLeader() != null &&
                        currentUser.getId().equals(groupToDisplay.getLeader().getId())) {
                    canView = true;
                }
                for (User user : membersOfGroup) {
                    // Check if they are a member of the group
                    if (currentUser.getId().equals(user.getId())) {
                        canView = true;
                    }
                    // Check if they monitor anyone in the group
                    for (User child : currentUser.getMonitorsUsers()) {
                        if (child.getId().equals(user.getId())) {
                            canView = true;
                        }
                    }
                }
                if (canView) {
                    // Get the id of the clicked user
                    long memberId = 0;
                    for (User member : membersOfGroup) {
                        if (member.getEmail() == textView.getText()) {
                            memberId = member.getId();
                            break;
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putLong("memberId", memberId);

                    FragmentManager manager = getSupportFragmentManager();
                    GroupMemberInfoFragment dialog = new GroupMemberInfoFragment();
                    dialog.setArguments(bundle);
                    dialog.show(manager, "MessageDialog");
                }  else {
                    Toast.makeText(GroupInfoActivity.this, "You cannot view this user's info.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        //Check if the current user is the leader
        if (groupLeader == null || !groupLeader.getId().equals(userSingleton.getId())) {
            createRequestLeadershipButton(layout);
        }

        if (groupLeader == null || !groupLeader.getId().equals(userSingleton.getId())) {
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
            createLeaderLeaveButton(layout);
            createDeleteButton(layout);
        }

        //Check if the user is monitoring anyone and create the appropriate buttons
        List<User> monitoringList = userSingleton.getMonitorsUsers();
        if (monitoringList.size() > 0) {
            //Get a list of the ids for comparing
            List<Long> monitorListId = new ArrayList<>();
            for (User user : monitoringList) {
                if (groupLeader == null || !user.getId().equals(groupLeader.getId())) {
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

    private void createLeaderLeaveButton(LinearLayout layout) {
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
                GroupInfoLeaderLeaveFragment dialog = new GroupInfoLeaderLeaveFragment();
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

    private void createRequestLeadershipButton(LinearLayout layout) {
        Button button = new Button(this);
        button.setText(R.string.lead);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUserSingleton currentUserSingleton =
                        CurrentUserSingleton.getInstance(GroupInfoActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("groupName", groupToDisplay.getGroupDescription());
                bundle.putLong("groupId", groupToDisplay.getId());
                bundle.putLong("userId", currentUserSingleton.getId());
                boolean hasParent = currentUserSingleton.getMonitoredByUsers().size() > 0;
                bundle.putBoolean("hasParent", hasParent);
                boolean hasLeader = groupLeader != null;
                bundle.putBoolean("hasLeader", hasLeader);

                FragmentManager manager = getSupportFragmentManager();
                GroupInfoLeadFragment dialog = new GroupInfoLeadFragment();
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
                if (groupLeader != null) {
                    bundle.putLong("leaderId", groupLeader.getId());
                } else {
                    bundle.putLong("leaderId", 0);
                }

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
                if (groupLeader != null) {
                    bundle.putLong("leaderId", groupLeader.getId());
                } else {
                    bundle.putLong("leaderId", 0);
                }

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
        String failedDestination = "Lat: " + latitude + ", Long: " + longitude;

        Log.d("GEO_ENCODER", "geoEncoder: latitude: " + latitude + " longitude: " + longitude);
        addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (addresses.size() == 0) {
            return failedDestination;
        }

        return addresses.get(0).getAddressLine(0);
    }

    public void setLeader(User user) {
        this.groupLeader = user;
        groupToDisplay.setLeader(user);
    }

    public Group getGroup() {
        return groupToDisplay;
    }
}
