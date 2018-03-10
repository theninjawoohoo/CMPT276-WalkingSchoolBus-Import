package groupdenim.cmpt276.awalkingschoolbus;

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

public class GroupInfoRemoveFragment extends AppCompatDialogFragment {
    private List<String> selectedChildren = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String groupName = getArguments().getString("groupName");
        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_info_remove, null);

        populateList(v);
        registerOnClickCallBack(v);
        setupCancelButton(v);
        setupRemoveButton(v, groupName);

        //Build dialog
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void populateList(View v){
        //Display all children who are in this group
        UserSingleton userSingleton = UserSingleton.getInstance();
        String userEmail = userSingleton.getCurrentUserEmail();
        User user = userSingleton.getUser(userEmail);
        //TODO: make it so only the children who are in the group are displayed
        String[] children = user.getPeopleUserIsMonitoring().toArray(new String[0]);
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
                if (selectedChildren.size() > 0) {
                    //Remove all selected users from the group
                    //Note: this will only work if the users are real users in the UserSingleton userMap
                    UserSingleton userSingleton = UserSingleton.getInstance();
                    GroupSingleton groupSingleton = GroupSingleton.getInstance();
                    Group group = groupSingleton.getGroup(groupName);

                    for (String userEmail : selectedChildren) {
                        User currentUser = userSingleton.getUser(userEmail);
                        currentUser.removeFromGroup(groupName);
                        group.removeMember(userEmail);
                    }

                    //Update activity UI to display new users
                    ((GroupInfoActivity)getActivity()).updateUi();

                    dismiss();
                } else {
                    String message = "Please select at lease one user";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
