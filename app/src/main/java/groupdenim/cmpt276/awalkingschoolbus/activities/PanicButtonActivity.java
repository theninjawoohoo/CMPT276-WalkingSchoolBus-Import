package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;

public class PanicButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);
        setupPanicButton();
    }

    private void setupPanicButton() {
        Button panicButton = findViewById(R.id.panicButton);
        panicButton.setEnabled(true);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.emergencyText);
                Message message = new Message();
                message.setText(text.getText().toString());
                message.setEmergency(true);
                long id = CurrentUserSingleton.getInstance(PanicButtonActivity.this).getId();
                ProxyBuilder.SimpleCallback<Message> callback = messageSent -> sent(messageSent);
                ServerSingleton.getInstance().sendMessageToParents(PanicButtonActivity.this,callback,id,message);
//                finish();
                panicButton.setEnabled(false);
            }
        });
    }

    private void sent(Message messageSent) {
        finish();
    }
}
