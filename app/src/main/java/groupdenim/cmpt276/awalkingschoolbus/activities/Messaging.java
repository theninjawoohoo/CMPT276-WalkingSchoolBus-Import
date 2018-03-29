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
import android.widget.Button;
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

    List<String> messageListString = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setupCallToServer();
        setupSendMessageToParentsButton();
        setupSendMessageToGroupsButton();
        setupListenerForMessageClick();
        setupUIRefresh();
    }

    private void setupSendMessageToGroupsButton() {
        Button send = findViewById(R.id.sendMessageToGroup);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Messaging.this,SendMessageToMyGroup.class);
                startActivity(intent);
            }
        });
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
                                messagesList.clear();
                                names.clear();
                                messageListString.clear();
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

                long messageId = messagesList.get(i).getId();
                long userId = CurrentUserSingleton.getInstance(Messaging.this).getId();
                editReadStatus(messageId,userId,messagesList.get(i));
                Intent intent = ViewMessage.makeIntent(Messaging.this,names.get(i), messagesList.get(i).getText());
                startActivity(intent);
            }
        });
    }

    private void editReadStatus(long messageId, long userId, Message message) {
        if (!getIsRead(message)) {
            ProxyBuilder.SimpleCallback<User> callback = user -> setStatus(user);
            ServerSingleton.getInstance().editReadStatusForMessage(Messaging.this,callback,messageId, userId);
        }
    }

    private void setStatus(User user) {
        populateCurrentUser(user.getEmail());
        messagesList.clear();
        names.clear();
        messageListString.clear();
        setupCallToServer();
    }

    private void setupCallToServer() {
        populateCurrentUser(CurrentUserSingleton.getInstance(this).getEmail());
        long id = CurrentUserSingleton.getInstance(this).getId();
        ProxyBuilder.SimpleCallback<List<Message>> callback = messages -> showMessages(messages);
        ServerSingleton.getInstance().getMessagesForUser(this, callback, id);
    }


    private void setupSendMessageToParentsButton() {
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
        messagesList = messages;
        getStringList(messages);
    }


    private void getStringList(List<Message> messages) {

        for (Message message : messages) {
            //boolean isRead = message.isRead();
            User user = message.getFromUser();
            boolean isRead = getIsRead(message);
            findUser(user,isRead);
        }
    }

    private boolean getIsRead(Message message) {
        populateCurrentUser(CurrentUserSingleton.getInstance(this).getEmail());
        List<Message> read = CurrentUserSingleton.getInstance(this).getReadMessages();
        for (Message messageToCheck : read) {
            if (messageToCheck.getId() == message.getId()) {
                return true;
            }
        }
        return false;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context,Messaging.class);
        return intent;
    }

    private void findUser(User user, boolean isRead) {
        ProxyBuilder.SimpleCallback<User> callback = userReturned -> getUser(userReturned,isRead);
        ServerSingleton.getInstance().getUserById(this,callback,user.getId());
    }

    private void getUser(User user, boolean isRead) {

        if (isRead) {
            String name = "From: " + user.getName();
            names.add(name);
            messageListString.add(name);
        } else if (!isRead){
            String name = "* From: " + user.getName();
            names.add(name);
            messageListString.add(name);
        }

        ListView messageListListView = findViewById(R.id.messageList);
        messageListListView.setAdapter(null);
        ArrayAdapter<String> messageListArrayAdapter = new ArrayAdapter<String>(this,R.layout.message_card, this.messageListString);
        messageListListView.setAdapter(messageListArrayAdapter);
    }

    private void populateCurrentUser(String email) {
        Context context = this.getApplicationContext();
        ProxyBuilder.SimpleCallback<User> callback = user -> setFields(user);
        ServerSingleton.getInstance().getUserByEmail(context,callback, email);
    }

    private void setFields(User user) {
        Log.i("a", "getuser: " + user);
        CurrentUserSingleton.setFields(user);
    }
}
