package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;

public class Messaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setupCallToServer();
        setupSendMessageButton();
    }

    private void setupCallToServer() {
        long id = CurrentUserSingleton.getInstance(this).getId();
        ProxyBuilder.SimpleCallback<List<Message>> callback = messages -> showMessages(messages);
        ServerSingleton.getInstance().getUnreadMessagesForUser(this, callback, id);
    }

    private void setupSendMessageButton() {
        FloatingActionButton sendMessage = findViewById(R.id.Button_sendMessage_Messaging);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Messaging.this,SendMessage.class);
                startActivity(intent);
            }
        });
    }

    private void showMessages(List<Message> messages) {
        ListView messageList = findViewById(R.id.messageList);
        List<String> messageListString = getStringList(messages);
        ArrayAdapter<String> messageListArrayAdapter = new ArrayAdapter<String>(this,R.layout.message_card, messageListString);
        messageList.setAdapter(messageListArrayAdapter);
    }

    private List<String> getStringList(List<Message> messages) {
        Log.i("message Test", "getStringList: " + messages);
        List<String> messageListString = new ArrayList<>();
        for (Message message : messages) {
            messageListString.add(message.getFromUser().getId() + " " + message.getText() );
        }
        return messageListString;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context,Messaging.class);
        return intent;
    }
}
