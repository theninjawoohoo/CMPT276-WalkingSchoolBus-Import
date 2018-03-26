package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class GroupMemberInfoFragment extends AppCompatDialogFragment {
    private View v;
    AlertDialog dialog;
    private long selectedUserId;
    private final int TEXT_SIZE = 24;
    private User selectedUser;
    private List<User> monitors;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_group_member_info, null);
        selectedUserId = getArguments().getLong("memberId");

        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        getUserInfoFromServer();

        return dialog;
    }

    private void getUserInfoFromServer() {
        ProxyBuilder.SimpleCallback<User> callback = returnedUser ->
                getUserInfoResponse(returnedUser);
        ServerSingleton.getInstance().getUserById(getActivity(), callback,
                selectedUserId);
    }

    private void getUserInfoResponse(User user) {
        selectedUser = user;
        getMonitorsFromServer();

    }

    private void getMonitorsFromServer() {
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList ->
                getMonitorResponse(returnedList);
        ServerSingleton.getInstance().getMonitoredUsers(getActivity(), callback,
                selectedUserId);
    }

    private void getMonitorResponse(List<User> monitors) {
        this.monitors = monitors;
        dialog.setCanceledOnTouchOutside(true);
        populateFields();
    }

    private void populateFields() {
        LinearLayout linearLayout =
                v.findViewById(R.id.LinearLayout_GroupMemberInfoFragment_DisplayInfo);

        populateMainInfo(linearLayout);
        populateMoreInfo(linearLayout);
        populateMonitorInfo(linearLayout);

        setupCancelButton(linearLayout);
    }

    private void populateMainInfo(LinearLayout linearLayout) {
        // Name
        String name = "Name: " + selectedUser.getName();
        TextView nameText = new TextView(getActivity());
        nameText.setText(name);
        nameText.setTextSize(TEXT_SIZE);
        linearLayout.addView(nameText);

        // Emergency contact info
        if (selectedUser.getEmergencyContactInfo() != null) {
            String emergency = "Emergency Contact: " + selectedUser.getEmergencyContactInfo();
            TextView emergencyText = new TextView(getActivity());
            emergencyText.setText(emergency);
            emergencyText.setTextSize(TEXT_SIZE);
            linearLayout.addView(emergencyText);
        }
    }

    private void populateMoreInfo(LinearLayout linearLayout) {
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        List<String> listDataHeader = new ArrayList<>();
        Map<String, List<String>> listDataChild = new HashMap<>();

        prepareMoreInfoData(listDataHeader, listDataChild);

        ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(),
                listDataHeader, (HashMap<String, List<String>>) listDataChild);

        expandableListView.setAdapter(adapter);

        linearLayout.addView(expandableListView);
    }

    private void prepareMoreInfoData(List<String> listDataHeader,
                                     Map<String, List<String>> listDataChild) {
        listDataHeader.add("More Info");

        List<String> moreUserInfo = new ArrayList<>();

        // Email
        String email = "Email: " + selectedUser.getEmail();
        moreUserInfo.add(email);

        // Birth year
        if (selectedUser.getBirthYear() != null) {
            String birthYear = "Birth Year: " + selectedUser.getBirthYear();
            moreUserInfo.add(birthYear);
        }

        // Birth month
        if (selectedUser.getBirthMonth() != null) {
            String birthMonth = "Birth Month: " + selectedUser.getBirthMonth();
            moreUserInfo.add(birthMonth);
        }

        // Address
        if (selectedUser.getAddress() != null) {
            String adress = "Address: " + selectedUser.getAddress();
            moreUserInfo.add(adress);
        }

        // Cell Phone
        if (selectedUser.getCellPhone() != null) {
            String cell = "Cell #: " + selectedUser.getCellPhone();
            moreUserInfo.add(cell);
        }

        // Home phone
        if (selectedUser.getHomePhone() != null) {
            String home = "Home #: " + selectedUser.getHomePhone();
            moreUserInfo.add(home);
        }

        // Grade
        if (selectedUser.getGrade() != null) {
            String grade = "Grade: " + selectedUser.getGrade();
            moreUserInfo.add(grade);
        }

        // Teacher name
        if (selectedUser.getTeacherName() != null) {
            String teacher = "Teacher: " + selectedUser.getTeacherName();
            moreUserInfo.add(teacher);
        }

        listDataChild.put(listDataHeader.get(0), moreUserInfo);
    }

    private void populateMonitorInfo(LinearLayout linearLayout) {
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        List<String> listDataHeader = new ArrayList<>();
        Map<String, List<String>> listDataChild = new HashMap<>();

        prepareMonitorData(listDataHeader, listDataChild, expandableListView);

        ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(),
                listDataHeader, (HashMap<String, List<String>>) listDataChild);

        expandableListView.setAdapter(adapter);

        linearLayout.addView(expandableListView);
    }

    private void prepareMonitorData(List<String> listDataHeader,
                                    Map<String, List<String>> listDataChild,
                                    ExpandableListView expandableListView) {
        // Monitors:
        listDataHeader.add("Monitors");

        List<String> monitorEmails = new ArrayList<>();
        for (User user : monitors) {
            monitorEmails.add(user.getEmail());
        }

        listDataChild.put(listDataHeader.get(0), monitorEmails);
        registerOnClickCallBack(expandableListView, monitorEmails);
    }

    private void registerOnClickCallBack(ExpandableListView expandableListView,
                                         List<String> monitorEmails) {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String monitorEmail = monitorEmails.get(childPosition);

                Bundle bundle = new Bundle();
                bundle.putString("monitorEmail", monitorEmail);

                FragmentManager manager = getChildFragmentManager();
                MonitorInfoFragment dialog = new MonitorInfoFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");

                return false;
            }
        });
    }

    private void setupCancelButton(LinearLayout linearLayout) {
        Button button = new Button(getActivity());
        button.setText("cancel");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        linearLayout.addView(button);
    }
}
