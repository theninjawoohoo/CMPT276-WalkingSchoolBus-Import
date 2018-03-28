package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class MonitorInfoFragment extends AppCompatDialogFragment {
    private View v;
    AlertDialog dialog;
    private String monitorEmail;
    private final int TEXT_SIZE = 24;
    private User selectedUser;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_monitor_info, null);
        monitorEmail = getArguments().getString("monitorEmail");

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
        ServerSingleton.getInstance().getUserByEmail(getActivity(), callback,
                monitorEmail);
    }

    private void getUserInfoResponse(User user) {
        selectedUser = user;
        dialog.setCanceledOnTouchOutside(true);
        populateFields();
        setupCancelButton();
    }


    private void populateFields() {
        LinearLayout linearLayout =
                v.findViewById(R.id.LinearLayout_MonitorInfoFragment_DisplayInfo);

        // Name
        String name = "Name: " + selectedUser.getEmail();
        TextView nameText = new TextView(getActivity());
        nameText.setText(name);
        nameText.setTextSize(TEXT_SIZE);
        linearLayout.addView(nameText);

        // Email
        String email = "Email: " + selectedUser.getEmail();
        TextView emailText = new TextView(getActivity());
        emailText.setText(email);
        emailText.setTextSize(TEXT_SIZE);
        linearLayout.addView(emailText);

        // Birth year
        if (selectedUser.getBirthYear() != null && !selectedUser.getBirthYear().isEmpty()) {
            String birthYear = "Birth Year: " + selectedUser.getBirthYear();
            TextView yearText = new TextView(getActivity());
            yearText.setText(birthYear);
            yearText.setTextSize(TEXT_SIZE);
            linearLayout.addView(yearText);
        }

        // Birth month
        if (selectedUser.getBirthMonth() != null && !selectedUser.getBirthMonth().isEmpty()) {
            String birthMonth = "Birth Month: " + selectedUser.getBirthMonth();
            TextView monthText = new TextView(getActivity());
            monthText.setText(birthMonth);
            monthText.setTextSize(TEXT_SIZE);
            linearLayout.addView(monthText);
        }

        // Address
        if (selectedUser.getAddress() != null && !selectedUser.getAddress().isEmpty()) {
            String address = "Address: " + selectedUser.getAddress();
            TextView addressText = new TextView(getActivity());
            addressText.setText(address);
            addressText.setTextSize(TEXT_SIZE);
            linearLayout.addView(addressText);
        }

        // Cell Phone
        if (selectedUser.getCellPhone() != null && !selectedUser.getCellPhone().isEmpty()) {
            String cell = "Cell #: " + selectedUser.getCellPhone();
            TextView cellText = new TextView(getActivity());
            cellText.setText(cell);
            cellText.setTextSize(TEXT_SIZE);
            linearLayout.addView(cellText);
        }

        // Home phone
        if (selectedUser.getHomePhone() != null && !selectedUser.getHomePhone().isEmpty()) {
            String home = "Home #: " + selectedUser.getHomePhone();
            TextView homeText = new TextView(getActivity());
            homeText.setText(home);
            homeText.setTextSize(TEXT_SIZE);
            linearLayout.addView(homeText);
        }

        // Grade
        if (selectedUser.getGrade() != null && !selectedUser.getGrade().isEmpty()) {
            String grade = "Grade: " + selectedUser.getGrade();
            TextView gradeText = new TextView(getActivity());
            gradeText.setText(grade);
            gradeText.setTextSize(TEXT_SIZE);
            linearLayout.addView(gradeText);
        }

        // Teacher name
        if (selectedUser.getTeacherName() != null && !selectedUser.getTeacherName().isEmpty()) {
            String teacher = "Teacher: " + selectedUser.getTeacherName();
            TextView teacherText = new TextView(getActivity());
            teacherText.setText(teacher);
            teacherText.setTextSize(TEXT_SIZE);
            linearLayout.addView(teacherText);
        }
    }


    private void setupCancelButton() {
        LinearLayout linearLayout =
                v.findViewById(R.id.LinearLayout_MonitorInfoFragment_DisplayInfo);
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
