package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;

public class SendMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        setupSendButton();
    }

    private void setupSendButton() {
        Button send = findViewById(R.id.button_send_sendMessage);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.message_ET);
                Message message = new Message();
                message.setText(text.getText().toString());
                message.setEmergency(false);
                long id = CurrentUserSingleton.getInstance(SendMessage.this).getId();
                ProxyBuilder.SimpleCallback<Message> callback = messageSent -> sendMessage(messageSent);
                ServerSingleton.getInstance().sendMessageById(SendMessage.this,callback,id,message);
            }
        });
    }

    private void sendMessage(Message message) {
        Log.i("send message result", "sendMessage: " + message);
    }

//    private void setupSpinner() {
//        List<Group> groupList = CurrentUserSingleton.getInstance(this).getMemberOfGroups();
//        Log.i("abc", "setupSpinner: " + groupList);
//        Spinner groups = findViewById(R.id.Spinner_groupList_SendMessage);
//        ArrayList<String> groupListString = new ArrayList<>();
//        for (Group group : groupList) {
//            groupListString.add(group.getId() + " ");
//            Log.i("a", "setupSpinner: " + group);
//        }

}
