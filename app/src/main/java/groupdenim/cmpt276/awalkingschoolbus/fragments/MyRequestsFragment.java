package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRecord;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRequest;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class MyRequestsFragment extends Fragment {
    List<PermissionRequest> myRequests = new ArrayList<>();
    List<String> requestsToDisplay = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);

        populateListView();
        setupLongClickListener(view);
        return view;
    }

    private void populateListView() {
        long id = CurrentUserSingleton.getInstance(getContext()).getId();
        ProxyBuilder.SimpleCallback<List<PermissionRequest>> callback = listOfRequests -> getList(listOfRequests);
        ServerSingleton.getInstance().getAllPermission(getContext(),callback,id);

    }

    private void getList(List<PermissionRequest> listOfRequests) {
        extractMyRequests(listOfRequests);
        generateRequestTitles();

        ListView list = getView().findViewById(R.id.myRequests);
        ArrayAdapter adapter =
                new ArrayAdapter(getContext(), R.layout.message_card, requestsToDisplay);
        list.setAdapter(adapter);
    }

    private void extractMyRequests(List<PermissionRequest> listOfRequests) {
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        long userId = currentUserSingleton.getId();

        for (PermissionRequest permissionRequest : listOfRequests) {
            if (permissionRequest.getRequestingUser().getId().equals(userId)) {
                myRequests.add(permissionRequest);
            }
        }
    }

    private void generateRequestTitles() {
        for (PermissionRequest permissionRequest : myRequests) {
            String title = "Request sent to for leadership of group " + getGroupName(permissionRequest);
            requestsToDisplay.add(title);
        }
    }

    private List<String> getStringListFrom(List<PermissionRequest> listOfRequests) {
        List<String> array = new ArrayList<>();
        for (PermissionRequest request : listOfRequests) {
            array.add(request.getMessage());
        }
        return array;
    }

    private void setupLongClickListener(View view) {
        ListView list = view.findViewById(R.id.myRequests);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                PermissionRequest permissionRequest = myRequests.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("requestMessage", permissionRequest.getMessage());
                bundle.putLong("requestId", permissionRequest.getId());
                String currentUserRequestStatus = getCurrentUserRequestStatus(permissionRequest);
                bundle.putString("currentUserRequestStatus", currentUserRequestStatus);
                String currentRequestStatus = permissionRequest.getStatus().toString();
                bundle.putString("currentRequestStatus", currentRequestStatus);
                List<Long> responseIds = getResponseIds(permissionRequest);
                long[] responseIdsArray = getResponseIdsArray(responseIds);
                List<String> responseStatuses = getResponseStatuses(permissionRequest);
                bundle.putLongArray("responseIds", responseIdsArray);
                bundle.putStringArrayList("responseStatuses", (ArrayList<String>) responseStatuses);
                bundle.putBoolean("showIncomingRequest", false);

                FragmentManager manager = getChildFragmentManager();
                RequestInfoFragment dialog = new RequestInfoFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "MessageDialog");
            }
        });
    }

    private String getCurrentUserRequestStatus(PermissionRequest permissionRequest) {
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        String currentUserRequestStatus = "PENDING";
        for (PermissionRecord permissionRecord : permissionRequest.getAuthorizors()) {
            for (User user : permissionRecord.getUsers()) {
                if (user.getId().equals(currentUserSingleton.getId())) {
                    currentUserRequestStatus = permissionRecord.getStatus().toString();
                    break;
                }
            }
        }
        return currentUserRequestStatus;
    }

    private long[] getResponseIdsArray(List<Long> responseIds) {
        long[] responseIdsArray = new long[responseIds.size()];
        for (int i = 0; i < responseIds.size(); i++) {
            responseIdsArray[i] = responseIds.get(i);
        }
        return responseIdsArray;
    }

    private List<Long> getResponseIds(PermissionRequest permissionRequest) {
        List<Long> responseIds = new ArrayList<>();
        for (PermissionRecord permissionRecord : permissionRequest.getAuthorizors()) {
            for (User user : permissionRecord.getUsers()) {
                if (!user.getId().equals(permissionRequest.getUserA().getId())) {
                    responseIds.add(user.getId());
                }
            }
        }
        return responseIds;
    }

    public List<String> getResponseStatuses(PermissionRequest permissionRequest) {
        List<String> responseStatuses = new ArrayList<>();
        for (PermissionRecord permissionRecord : permissionRequest.getAuthorizors()) {
            for (User user : permissionRecord.getUsers()) {
                if (!user.getId().equals(permissionRequest.getUserA().getId())) {
                    responseStatuses.add(permissionRecord.getStatus().toString());
                }
            }
        }
        return responseStatuses;
    }

    public String getGroupName(PermissionRequest permissionRequest) {
        String message = permissionRequest.getMessage();
        String sequenceToFind = "to begin leading the group named '";
        final int START_INDEX = message.indexOf(sequenceToFind) + sequenceToFind.length();
        final int END_INDEX = message.length() - 1;
        return message.substring(START_INDEX, END_INDEX);
    }
}
