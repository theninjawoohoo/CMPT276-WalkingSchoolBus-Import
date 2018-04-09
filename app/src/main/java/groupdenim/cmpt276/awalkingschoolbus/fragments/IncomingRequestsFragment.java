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

public class IncomingRequestsFragment extends Fragment {
    List<PermissionRequest> requestsForMe = new ArrayList<>();
    List<String> requestTitles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming_requests, container, false);

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
        extractRequestsForMe(listOfRequests);
        generateRequestTitles();

        ListView list = getView().findViewById(R.id.incomingRequestList);
        ArrayAdapter adapter =
                new CustomRequestListAdapter(getContext(), R.layout.message_card, requestTitles, requestsForMe);
        list.setAdapter(adapter);
    }

    private void extractRequestsForMe(List<PermissionRequest> listOfRequests) {
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        for (PermissionRequest permissionRequest : listOfRequests) {
            if (!permissionRequest.getUserA().getId().equals(currentUserSingleton.getId())) {
                requestsForMe.add(permissionRequest);
            }
        }
    }

    public void updateList() {
        ListView list = getView().findViewById(R.id.incomingRequestList);
        ArrayAdapter adapter = new CustomRequestListAdapter(getContext(),
                R.layout.message_card, requestTitles, requestsForMe);
        list.setAdapter(adapter);
        generateRequestTitles();
    }

    private void generateRequestTitles() {
        requestTitles.clear();
        for (PermissionRequest permissionRequest : requestsForMe) {
            String email = getEmailOfRequester(permissionRequest);
            String title = "Request from " + email;
            requestTitles.add(title);
        }
    }

    private String getEmailOfRequester(PermissionRequest permissionRequest) {
        final int FIRST_INDEX = 1;
        final int LAST_INDEX = permissionRequest.getMessage().indexOf("'", FIRST_INDEX);
        return permissionRequest.getMessage().substring(FIRST_INDEX, LAST_INDEX);
    }

    private void setupLongClickListener(View view) {
        ListView list = view.findViewById(R.id.incomingRequestList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                PermissionRequest permissionRequest = requestsForMe.get(position);
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
                bundle.putBoolean("showIncomingRequest", true);

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

    public List<PermissionRequest> getRequestsForMe() {
        return requestsForMe;
    }
}
