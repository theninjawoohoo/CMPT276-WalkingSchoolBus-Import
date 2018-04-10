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

public class GroupInfoDeleteFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String groupName = getArguments().getString("groupName");
        final long groupId = getArguments().getLong("groupId");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_delete, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoDeleteFragment);
        String message = "Are you sure you want to delete the group " + groupName + "?";
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
                                getDeleteGroupResponse();
                        ServerSingleton.getInstance().deleteGroup(getActivity(), callback,
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

    public void getDeleteGroupResponse(){
        Log.i("DELETE_RESPONSE", "Got response from delete group");
        //Update user singleton to no longer lead this group
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance(getActivity());
        List<Group> groupList = userSingleton.getLeadsGroups();
        final long groupId = getArguments().getLong("groupId");
        Group groupToDelete = new Group();
        for (Group group : groupList) {
            if (group.getId() == groupId) {
                groupToDelete = group;
            }
        }
        groupList.remove(groupToDelete);
        userSingleton.setLeadsGroups(groupList);

        getActivity().finish();

    }
}
