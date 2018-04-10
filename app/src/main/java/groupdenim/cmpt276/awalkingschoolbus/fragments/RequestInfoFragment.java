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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRecord;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRequest;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionStatus;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class RequestInfoFragment extends AppCompatDialogFragment {
    private String requestMessage;
    private long requestId;
    private String currentRequestStatus;
    private String currentUserRequestStatus;
    private long[] responseIds;
    private List<String> responseStatuses;
    private boolean showIncomingRequest;
    private AlertDialog dialog;
    private Button approveButton;
    private Button denyButton;
    private List<User> usersToDisplay = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        requestMessage = getArguments().getString("requestMessage");
        requestId = getArguments().getLong("requestId");
        currentRequestStatus = getArguments().getString("currentRequestStatus");
        currentUserRequestStatus = getArguments().getString("currentUserRequestStatus");
        responseIds = getArguments().getLongArray("responseIds");
        responseStatuses = getArguments().getStringArrayList("responseStatuses");
        showIncomingRequest = getArguments().getBoolean("showIncomingRequest");

        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_request_info, null);

        //Build dialog
        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();

        getUsersToDisplay(v);

        return dialog;
    }

    private void getUsersToDisplay(View v) {
        for (long userId : responseIds) {
            ProxyBuilder.SimpleCallback<User> callback = returnedUser ->
                    getUsersToDisplayResponse(returnedUser, v);
            ServerSingleton.getInstance().getUserById(getActivity(), callback,
                    userId);
        }
    }

    private void getUsersToDisplayResponse(User user, View v) {
        usersToDisplay.add(user);
        if (usersToDisplay.size() == responseIds.length) {
            displayMessage(v);
            if (currentUserRequestStatus.equals("PENDING") && currentRequestStatus.equals("PENDING") && showIncomingRequest) {
                initializeApproveButton(v);
                initializeDenyButton(v);
            }
        }
    }

    private void displayMessage(View v) {
        TextView textView = v.findViewById(R.id.TextView_RequestInfoFragment);
        StringBuilder message = new StringBuilder();

        message.append(requestMessage + "\n\n Status: " + currentRequestStatus + "\n\n");
        for (int i = 0; i < usersToDisplay.size(); i++) {
            message.append(responseStatuses.get(i) + " from "
                    + usersToDisplay.get(i).getEmail() + "\n");
        }

        textView.setText(message);
    }

    private void initializeApproveButton(View view) {
        approveButton = new Button(getContext());
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableInputs();
                ProxyBuilder.SimpleCallback<PermissionRequest> callback = returnedRequest ->
                        approveRequestResponse();
                ServerSingleton.getInstance().approveOrDeny(getActivity(), callback,
                        requestId, PermissionStatus.APPROVED);
            }
        });
        approveButton.setText(R.string.approve);
        LinearLayout linearLayout = view.findViewById(R.id.LinearLayout_RequestInfoFragment);
        linearLayout.addView(approveButton);
    }

    private void approveRequestResponse() {
        updateLocalList(PermissionStatus.APPROVED);
        Toast.makeText(getContext(), "APPROVED", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void initializeDenyButton(View view) {
        denyButton = new Button(getContext());
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableInputs();
                ProxyBuilder.SimpleCallback<PermissionRequest> callback = returnedRequest ->
                        denyRequestResponse();
                ServerSingleton.getInstance().approveOrDeny(getActivity(), callback,
                        requestId, PermissionStatus.DENIED);
            }
        });
        denyButton.setText(R.string.deny);
        LinearLayout linearLayout = view.findViewById(R.id.LinearLayout_RequestInfoFragment);
        linearLayout.addView(denyButton);
    }

    private void denyRequestResponse() {
        updateLocalList(PermissionStatus.DENIED);
        Toast.makeText(getContext(), "DENIED", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void updateLocalList(PermissionStatus status) {
        List<PermissionRequest> currentRequests =
                ((IncomingRequestsFragment) getParentFragment()).getRequestsForMe();
        // Set current user's request status
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        for (PermissionRequest permissionRequest : currentRequests) {
            if (permissionRequest.getId() == requestId) {
                for (PermissionRecord permissionRecord : permissionRequest.getAuthorizors()) {
                    for (User user : permissionRecord.getUsers()) {
                        if (user.getId().equals(currentUserSingleton.getId())) {
                            permissionRecord.setStatus(status);
                        }
                    }
                }
            }
        }

        // Set overall request status
        boolean statusChanged = true;
        PermissionRequest changedRequest = new PermissionRequest();
        for (PermissionRequest permissionRequest : currentRequests) {
            if (permissionRequest.getId() == requestId) {
                changedRequest = permissionRequest;
                if (status.toString().equals("DENIED")) {
                    break;
                }
                for (PermissionRecord permissionRecord : permissionRequest.getAuthorizors()) {
                    if (!permissionRecord.getStatus().toString().equals(status.toString())) {
                        statusChanged = false;
                    }
                }
            }
        }

        if (statusChanged) {
            changedRequest.setStatus(status);
        }
        ((IncomingRequestsFragment) getParentFragment()).updateList();
    }


    private void disableInputs() {
        dialog.setCanceledOnTouchOutside(false);
        approveButton.setEnabled(false);
        denyButton.setEnabled(false);
    }
}
