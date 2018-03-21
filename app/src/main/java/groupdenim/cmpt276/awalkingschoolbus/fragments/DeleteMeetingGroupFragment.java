package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.mapModels.GroupMeetingDeletionSingleton;

/**
 * This fragment prompts the user if they would like to delete this meeting group.
 */

public class DeleteMeetingGroupFragment extends AppCompatDialogFragment {
    TextView groupDescription;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_group, null);

        builder.setView(view).setTitle("Deleting a Group")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
        GroupMeetingDeletionSingleton deletion = GroupMeetingDeletionSingleton.getInstance();

        groupDescription = view.findViewById(R.id.group_desc);

        String description = deletion.getDescription();

        groupDescription.setText(description);

        return builder.create();
    }


}
