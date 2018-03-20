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

import groupdenim.cmpt276.awalkingschoolbus.activities.GroupInfoActivity;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class GroupInfoLeaveFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String groupName = getArguments().getString("groupName");
        final long groupId = getArguments().getLong("groupId");
        final long userId = getArguments().getLong("userId");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_leave, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoLeaveFragment);
        String message = "Are you sure you want to leave the group " + groupName + "?";
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
                        ProxyBuilder.SimpleCallback<Void> callback = returnedList ->
                                getRemoveGroupResponse(dialog);
                        ServerSingleton.getInstance().removeMemberFromGroup(getActivity(), callback,
                                groupId, userId);
                        buttonYes.setEnabled(false);
                        buttonCancel.setEnabled(false);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                });
            }
        });

        return dialog;
    }

    public void getRemoveGroupResponse(AlertDialog dialog){
        Log.i("REMOVE_RESPONSE", "Got response from remove member");
        //Update user singleton to leave group
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance(getActivity());
        List<Group> groupList = userSingleton.getMemberOfGroups();
        final long groupId = getArguments().getLong("groupId");
        for (Group group : groupList) {
            if (group.getId() == groupId) {
                groupList.remove(group);
            }
        }
        userSingleton.setMemberOfGroups(groupList);
        //Update the activity's member list
        List<User> members = ((GroupInfoActivity)getActivity()).getMembersOfGroup();
        for (User user : members) {
            if (user.getId().equals(userSingleton.getId())) {
                members.remove(user);
            }
        }
        ((GroupInfoActivity)getActivity()).setMembersOfGroup(members);
        //Update UI to show new data
        ((GroupInfoActivity)getActivity()).updateUi();
        dialog.dismiss();
    }
}
