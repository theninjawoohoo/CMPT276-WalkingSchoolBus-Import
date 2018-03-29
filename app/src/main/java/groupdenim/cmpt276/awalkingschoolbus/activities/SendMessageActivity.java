package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;

public class SendMessageActivity extends AppCompatActivity {

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
                long id = CurrentUserSingleton.getInstance(SendMessageActivity.this).getId();
                ProxyBuilder.SimpleCallback<Message> callback = messageSent -> sendMessage(messageSent);
                ServerSingleton.getInstance().sendMessageToParents(SendMessageActivity.this,callback,id,message);
                finish();
            }
        });
    }

    private void sendMessage(Message message) {
        Log.i("send message result", "sendMessage: " + message);
    }


}
