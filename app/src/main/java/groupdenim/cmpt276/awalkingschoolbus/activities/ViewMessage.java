package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import groupdenim.cmpt276.awalkingschoolbus.R;

public class ViewMessage extends AppCompatActivity {

    private static TextView fromUserTextView;
    private static TextView messageTextView;
    private static final String INTENTUSER = "USER";
    private static final String INTENTMESSAGE = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        messageTextView = findViewById(R.id.messageText);
        fromUserTextView = findViewById(R.id.textViewFrom);
        collectData();
    }

    private void collectData() {
        Intent intent = getIntent();
        String arr[] = intent.getStringExtra(INTENTUSER).split(":");
        String userName = arr[1];
        String message = intent.getStringExtra(INTENTMESSAGE);
        fromUserTextView.setText(userName);
        messageTextView.setText(message);

    }

    public static Intent makeIntent(Context context, String userName, String text) {
        Intent intent = new Intent(context, ViewMessage.class);
        intent.putExtra(INTENTUSER, userName);
        intent.putExtra(INTENTMESSAGE, text);
        return intent;
    }
}
