package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class Messaging extends AppCompatActivity {

    List<String> messageList;
    List<String> names = new ArrayList<>();
    List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setupCallToServer();
        setupSendMessageButton();
        setupListenerForMessageClick();
        setupUIRefresh();
    }

    private void setupUIRefresh() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                Log.i("v", "run: a");
                                setupCallToServer();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void setupListenerForMessageClick() {
        ListView messageListListView = findViewById(R.id.messageList);
        messageListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ban", "onItemClick: " + view.getId() + " " + i);
                Log.i("ban", "onItemClick: message says " + Messaging.this.messageList.get(i));
                Intent intent = ViewMessage.makeIntent(Messaging.this,names.get(i),messages.get(i).getText());
                startActivity(intent);
            }
        });
    }

    private void setupCallToServer() {
        long id = CurrentUserSingleton.getInstance(this).getId();
        ProxyBuilder.SimpleCallback<List<Message>> callback = messages -> showMessages(messages);
        ServerSingleton.getInstance().getMessagesForUser(this, callback, id);

        setupReadVsUnread();
    }

    private void setupReadVsUnread() {
        long id = CurrentUserSingleton.getInstance(this).getId();
        ProxyBuilder.SimpleCallback<List<Message>> readCallback = messages -> setStatusRead(messages);
        ServerSingleton.getInstance().getReadMessagesForUser(this, readCallback, id);

        ProxyBuilder.SimpleCallback<List<Message>> unreadCallback = messages -> setStatusUnread(messages);
        ServerSingleton.getInstance().getReadMessagesForUser(this, unreadCallback, id);

    }

    private void setStatusUnread(List<Message> messages) {
        for (Message message : messages) {
            message.setRead(false);
            messages.add(message);
        }
    }

    private void setStatusRead(List<Message> messages) {
        for (Message message : messages) {
            message.setRead(true);
            messages.add(message);
        }
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
        Log.i("bav", "showMes: got unread messages");
        ListView messageList = findViewById(R.id.messageList);
        this.messageList = getStringList(messages);
//        ArrayAdapter<String> messageListArrayAdapter = new ArrayAdapter<String>(this,R.layout.message_card, messageList);
//        messageList.setAdapter(messageListArrayAdapter);
    }


    private List<String> getStringList(List<Message> messages) {
        Log.i("message Test", "getStringList: " + messages);
        List<String> messageListString = new ArrayList<>();
        for (Message message : messages) {
            boolean isRead = message.isRead();
            messages.add(message);
            User user = message.getFromUser();
            findUser(user,messageListString,isRead);
            this.messages.add(message);
            //messageListString.add(message.getFromUser().getId() + " " + message.getText() );
        }
        for (String s : names) {
            Log.i("tagthis", "getStringList: ajajajajaj");
            messageListString.add(s);
        }
        return messageListString;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context,Messaging.class);
        return intent;
    }

    private void findUser(User user, List<String> messageListString, boolean isRead) {
        ProxyBuilder.SimpleCallback<User> callback = userReturned -> getUser(userReturned, messageListString, isRead);
        ServerSingleton.getInstance().getUserById(this,callback,user.getId());
    }

    private void getUser(User user, List<String> messageListString, boolean isRead) {
        if (isRead) {
            String name = "From: " + user.getName();
            names.add(name);
            this.messageList.add(name);
        } else {
            String name = "* From: " + user.getName();
            names.add(name);
            this.messageList.add(name);
        }
        ListView messageListListView = findViewById(R.id.messageList);
        ArrayAdapter<String> messageListArrayAdapter = new ArrayAdapter<String>(this,R.layout.message_card, this.messageList);
        messageListListView.setAdapter(messageListArrayAdapter);
    }
}
