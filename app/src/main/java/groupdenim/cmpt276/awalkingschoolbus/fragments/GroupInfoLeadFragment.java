package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import groupdenim.cmpt276.awalkingschoolbus.activities.GroupInfoActivity;

public class GroupInfoLeadFragment extends AppCompatDialogFragment {
    private String groupName;
    private long groupId;
    private long userId;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        groupName = getArguments().getString("groupName");
        groupId = getArguments().getLong("groupId");
        userId = getArguments().getLong("userId");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_join, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoJoinFragment);
        String message = "Are you sure you want to lead the group " + groupName + "?";
        textView.setText(message);

        //Build dialog
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button buttonYes = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button buttonCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonYes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ProxyBuilder.SimpleCallback<Group> callback = returnedGroup ->
                                getGroupByIdResponse(dialog, returnedGroup);
                        ServerSingleton.getInstance().getGroupById(getActivity(), callback,
                                groupId);
                        buttonYes.setEnabled(false);
                        buttonCancel.setEnabled(false);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                });
            }
        });

        return dialog;
    }

    public void getGroupByIdResponse(AlertDialog dialog, Group group) {
        ProxyBuilder.SimpleCallback<User> callback = returnedUser ->
                getUserByIdResponse(dialog, group, returnedUser);
        ServerSingleton.getInstance().getUserById(getActivity(), callback,
                userId);
    }

    public void getUserByIdResponse(AlertDialog dialog, Group group, User user) {
        group.setLeader(user);
        ProxyBuilder.SimpleCallback<Group> callback = returnedList ->
                getAddLeaderResponse(dialog, group, user);
        ServerSingleton.getInstance().updateGroupById(getActivity(), callback,
                groupId, group);
    }

    public void getAddLeaderResponse(AlertDialog dialog, Group group, User user){
        Log.i("ADD_RESPONSE", "Got response from add member");
        //Update user singleton to join group
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance(getActivity());
        List<Group> leadsGroups = userSingleton.getLeadsGroups();
        leadsGroups.add(group);
        userSingleton.setLeadsGroups(leadsGroups);
        //Update the activity's member list
        ((GroupInfoActivity)getActivity()).setLeader(user);
        //Update UI to show new data
        ((GroupInfoActivity)getActivity()).updateUi();
        dialog.dismiss();
    }
}
