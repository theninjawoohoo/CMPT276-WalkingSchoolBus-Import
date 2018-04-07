package groupdenim.cmpt276.awalkingschoolbus.fragments;

// Taken from https://stackoverflow.com/questions/7361135/how-to-change-color-and-font-on-listview

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRecord;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRequest;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionStatus;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class CustomRequestListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int id;
    private List<String> items;
    private List<PermissionRequest> requestsForMe;

    public CustomRequestListAdapter(Context context, int textViewResourceId ,
                                    List<String> list, List<PermissionRequest> requestsForMe) {
        super(context, textViewResourceId, list);
        this.context = context;
        id = textViewResourceId;
        items = list ;
        this.requestsForMe = requestsForMe;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = v ;
        if (view == null){
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(id, null);
        }

        TextView text = view.findViewById(R.id.textView_request);
        List<PermissionRecord> permissionRecords = requestsForMe.get(position).getAuthorizors();
        PermissionStatus currentUserStatus = PermissionStatus.PENDING;
        CurrentUserSingleton currentUserSingleton = CurrentUserSingleton.getInstance(getContext());
        for (PermissionRecord permissionRecord : permissionRecords) {
            for (User user : permissionRecord.getUsers()) {
                if (user.getId().equals(currentUserSingleton.getId())) {
                    currentUserStatus = permissionRecord.getStatus();
                }
            }
        }

        if (items.get(position) != null ) {
            switch (currentUserStatus) {
                case PENDING:
                    text.setBackgroundColor(Color.GREEN);
                    break;
                default:
                    text.setBackgroundColor(Color.GRAY);
            }
            text.setText(items.get(position));
        }

        return view;
    }

}