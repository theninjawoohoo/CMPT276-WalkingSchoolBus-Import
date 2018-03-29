package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;

public class SendMessageToMyGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_to_my_group);
        setupSpinner();
        setupSendButton();
    }


    private void setupSendButton() {
        Button send = findViewById(R.id.button_send_sendMessageToMyGroup);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.message_ET);
                Message message = new Message();
                message.setText(text.getText().toString());
                message.setEmergency(false);
                Spinner groupIdSpinner = findViewById(R.id.leadsGroup);
                String groupdIdString = groupIdSpinner.getSelectedItem().toString();
                long groupId = Long.parseLong(groupdIdString);

                ProxyBuilder.SimpleCallback<Message> callback = messageSent -> sendMessage(messageSent);
                ServerSingleton.getInstance().sendMessageToGroup(SendMessageToMyGroup.this,callback,groupId,message);

                finish();
            }
        });
    }

    private void sendMessage(Message message) {
        Log.i("send message result", "sendMessage: " + message);
    }

    private void setupSpinner() {
        List<Group> groupList = CurrentUserSingleton.getInstance(this).getLeadsGroups();
        test(groupList);

        Spinner groups = findViewById(R.id.leadsGroup);
        ArrayList<String> groupListString = new ArrayList<>();
        for (Group group : groupList) {
            groupListString.add(group.getId() + "");
            Log.i("a", "setupSpinner: " + group);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, groupListString);
        groups.setAdapter(adapter);
    }

    private void test(List<Group> groupsList) {
        for (Group group : groupsList) {
            Log.i("AAA", "test: " + group.getId() + " name: " + group.getGroupDescription());
        }
    }
}
