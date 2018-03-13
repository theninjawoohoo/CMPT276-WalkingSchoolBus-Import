package groupdenim.cmpt276.awalkingschoolbus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class GroupInfoLeaveFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String groupName = getArguments().getString("groupName");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_leave, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_GroupInfoLeaveFragment);
        String message = "Are you sure you want to leave the group " + groupName + "?";
        textView.setText(message);

        //Create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Remove user from group
                        //uncomment after
//                        UserSingleton userSingleton = UserSingleton.getInstance();
//                        final String userEmail = userSingleton.getCurrentUserEmail();
//                        GroupSingleton groupSingleton = GroupSingleton.getInstance();
//                        Group group = groupSingleton.getGroup(groupName);
//                        group.removeMember(userEmail);
//                        groupSingleton.setGroup(groupName, group);
//
//                        //Remove group from user's group list
//                        User user = userSingleton.getUserMap().get(userEmail);
//                        user.removeFromGroup(groupName);

                        //Update activity UI
                        ((GroupInfoActivity)getActivity()).updateUi();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //End
                        break;
                }
            }
        };

        //Build dialog
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}
