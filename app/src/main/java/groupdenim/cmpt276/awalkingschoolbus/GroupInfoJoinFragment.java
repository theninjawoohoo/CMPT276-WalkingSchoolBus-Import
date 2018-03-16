package groupdenim.cmpt276.awalkingschoolbus;

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

import static groupdenim.cmpt276.awalkingschoolbus.GroupInfoActivity.membersOfGroup2;

public class GroupInfoJoinFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String groupName = getArguments().getString("groupName");
        final long groupId = getArguments().getLong("groupId");
        final long userId = getArguments().getLong("userId");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_join, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoJoinFragment);
        String message = "Are you sure you want to join the group " + groupName + "?";
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

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList ->
                                getAddGroupResponse(returnedList, dialog);
                        ServerSingleton.getInstance().addNewMemberOfGroup(getActivity(), callback,
                                groupId, userId);
                    }
                });
            }
        });

/*
        //Create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Add user to group

                        //uncomment after
//                        UserSingleton userSingleton = UserSingleton.getInstance();
//                        final String userEmail = userSingleton.getCurrentUserEmail();
//                        GroupSingleton groupSingleton = GroupSingleton.getInstance();
//                        Group group = groupSingleton.getGroup(groupName);
//                        group.addMember(userEmail);
//                        groupSingleton.setGroup(groupName, group);
//
//                        //Add group to user's group list
//                        User user = userSingleton.getUserMap().get(userEmail);
//                        user.addToGroup(groupName);

                        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList -> getAddGroupResponse(returnedList, dialog);
                        ServerSingleton.getInstance().addNewMemberOfGroup(getActivity(), callback,
                                groupId, userId);


                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //End
                        break;
                }
            }
        };*/
        return dialog;
    }

    public void getAddGroupResponse(List<User> members, AlertDialog dialog){
        Log.i("ADD_RESPONSE", "Got response from add member");
        //Update activity UI
        GroupInfoActivity.isFragmentChange = true;
        ((GroupInfoActivity)getActivity()).updateUi();
        dialog.dismiss();
        membersOfGroup2 = members;
    }
}
