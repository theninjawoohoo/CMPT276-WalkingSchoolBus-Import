package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

public class IncomingRequestsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming_requests, container, false);

        populateListView();
        //setupLongClickListener();
        return view;
    }

    private void populateListView() {
        long id = CurrentUserSingleton.getInstance(getContext()).getId();
        ProxyBuilder.SimpleCallback<List<PermissionRequest>> callback = listOfRequests -> getList(listOfRequests);
        ServerSingleton.getInstance().getPendingPermissions(getContext(),callback,id);

    }

    private void getList(List<PermissionRequest> listOfRequests) {
        ListView list = getView().findViewById(R.id.incomingRequestList);
        List<String> requestStringList = getStringListFrom(listOfRequests);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),R.layout.message_card,requestStringList);
        list.setAdapter(adapter);

    }

    private List<String> getStringListFrom(List<PermissionRequest> listOfRequests) {
        List<String> array = new ArrayList<>();
        for (PermissionRequest request : listOfRequests) {
            array.add(request.getMessage());
        }
        return array;
    }

    private void setupLongClickListener() {
        ListView list = getView().findViewById(R.id.incomingRequestList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


}
