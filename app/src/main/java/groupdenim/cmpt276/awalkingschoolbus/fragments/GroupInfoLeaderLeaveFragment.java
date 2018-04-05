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

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.activities.GroupInfoActivity;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class GroupInfoLeaderLeaveFragment extends AppCompatDialogFragment {
    private long groupId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        groupId = getArguments().getLong("groupId");

        final String groupName = getArguments().getString("groupName");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_leave, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoLeaveFragment);
        String message = "Are you sure you want to leave the group " + groupName
                + " as the leader?";
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
        group.setLeader(null);
        ProxyBuilder.SimpleCallback<Group> callback = returnedList ->
                getRemoveGroupResponse(dialog);
        ServerSingleton.getInstance().updateGroupById(getActivity(), callback,
                groupId, group);
    }

    public void getRemoveGroupResponse(AlertDialog dialog){
        Log.i("REMOVE_RESPONSE", "Got response from remove leader");
        //Update user singleton to leave group
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance(getActivity());
        List<Group> groupList = userSingleton.getLeadsGroups();
        final long groupId = getArguments().getLong("groupId");

        Group groupToRemove = new Group();
        for (Group group : groupList) {
            if (group.getId() == groupId) {
                groupToRemove = group;
                break;
            }
        }
        groupList.remove(groupToRemove);
        userSingleton.setLeadsGroups(groupList);

        ((GroupInfoActivity)getActivity()).setLeader(null);

        //Update UI to show new data
        ((GroupInfoActivity)getActivity()).updateUi();
        dialog.dismiss();
    }
}
