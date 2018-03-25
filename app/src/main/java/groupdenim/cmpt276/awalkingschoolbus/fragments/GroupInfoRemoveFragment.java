package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.activities.GroupInfoActivity;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class GroupInfoRemoveFragment extends AppCompatDialogFragment {
    private List<User> children = new ArrayList<>();
    private List<String> selectedChildren = new ArrayList<>();
    private int numberOfSelectedChildren = 0;
    private int numberOfResponsesReceived = 0;

    private String groupName;
    private long leaderId;
    private long groupId;
    private View v;
    AlertDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        groupName = getArguments().getString("groupName");
        groupId = getArguments().getLong("groupId");
        leaderId = getArguments().getLong("leaderId");
        v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_remove, null);

        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        getListOfChildren();

        return dialog;
    }

    private void getListOfChildren() {
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList ->
                getChildrenResponse(returnedList);
        ServerSingleton.getInstance().getMonitorUsers(getContext(), callback,
                CurrentUserSingleton.getInstance(getContext()).getId());
    }

    private void getChildrenResponse(List<User> children) {
        this.children = children;
        dialog.setCanceledOnTouchOutside(true);
        populateList(v);
        registerOnClickCallBack(v);
        setupCancelButton(v);
        setupRemoveButton(v, groupName);
    }

    private void populateList(View v){
        //Display all children who are in this group
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        List<String> monitorsEmails = new ArrayList<>();
        for (User user : children) {
            boolean isInGroup = false;
            // Check if the user is in the group
            for (Group group : user.getMemberOfGroups()) {
                if (group.getId() == groupId) {
                    isInGroup = true;
                    break;
                }
            }
            if (isInGroup && !user.getId().equals(leaderId)) {
                monitorsEmails.add(user.getEmail());
            }
        }

        String[] children = monitorsEmails.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.person_user_is_monitoring, children);
        ListView list = v.findViewById(R.id.ListView_GroupInfoRemoveFragment_children);
        list.setAdapter(adapter);
    }

    private void registerOnClickCallBack(View v) {
        ListView list = v.findViewById(R.id.ListView_GroupInfoRemoveFragment_children);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                if (selectedChildren.contains(textView.getText().toString())) {
                    textView.setBackgroundColor(Color.WHITE);
                    selectedChildren.remove(textView.getText().toString());
                } else {
                    textView.setBackgroundColor(Color.GREEN);
                    selectedChildren.add(textView.getText().toString());
                }
            }
        });
    }

    private void setupCancelButton(View v) {
        Button button = v.findViewById(R.id.button_GroupInfoRemoveFragment_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setupRemoveButton(View v, final String groupName) {
        Button button = v.findViewById(R.id.button_GroupInfoRemoveFragment_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfSelectedChildren = selectedChildren.size();
                if (numberOfSelectedChildren > 0) {
                    disableAllButtons();
                    // Remove all selected users from the group
                    // Get id of each user
                    List<User> usersToRemove = new ArrayList<>();
                    for (String email : selectedChildren) {
                        for (User user : children) {
                            if (email.equals(user.getEmail())) {
                                usersToRemove.add(user);
                            }
                        }
                    }

                    //Add each user to the group on server
                    for (User user : usersToRemove) {
                        ProxyBuilder.SimpleCallback<Void> callback = returnedList ->
                                getRemoveUserResponse();
                        ServerSingleton.getInstance().removeMemberFromGroup(getActivity(), callback,
                                groupId, user.getId());
                    }
                } else {
                    String message = "Please select at lease one user";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void disableAllButtons() {
        Button buttonAdd = v.findViewById(R.id.button_GroupInfoRemoveFragment_add);
        Button buttonCancel = v.findViewById(R.id.button_GroupInfoRemoveFragment_cancel);
        buttonAdd.setEnabled(false);
        buttonCancel.setEnabled(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void getRemoveUserResponse() {
        numberOfResponsesReceived++;
        //Check if all responses have bee received
        if (numberOfResponsesReceived == numberOfSelectedChildren) {
            //Update activity UI to display new users
            List<User> members = ((GroupInfoActivity)getActivity()).getMembersOfGroup();
            List<User> membersToRemove = new ArrayList<>();
            for (User user : members) {
                for (String removedChild : selectedChildren) {
                    if (user.getEmail().equals(removedChild)) {
                        membersToRemove.add(user);
                        break;
                    }
                }
            }
            for (User user : membersToRemove) {
                members.remove(user);
            }
            ((GroupInfoActivity)getActivity()).setMembersOfGroup(members);
            ((GroupInfoActivity)getActivity()).updateUi();

            dismiss();
        }
    }
}
