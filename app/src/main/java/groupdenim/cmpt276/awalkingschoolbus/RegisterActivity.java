package groupdenim.cmpt276.awalkingschoolbus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    //set up fields
    Button register;
    private EditText username;
    private EditText password;
    private EditText email;
    private View mProgressView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check to see if service completed as expected
            boolean result = intent.getBooleanExtra("result",false);

            //if service was successful
            if (result) {
                String name = intent.getStringExtra("name");
                Toast.makeText(RegisterActivity.this,
                        "Success, User: " + name + " created!",
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                Log.i("e", "onReceive: help" );
            }

            //stop service
            Intent register = new Intent(RegisterActivity.this,RegisterService.class);
            stopService(register);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //you know what this does.
        register = findViewById(R.id.button_register);
        username = findViewById(R.id.EditText_user);
        password = findViewById(R.id.EditText_pass);
        email = findViewById(R.id.EditText_email);
        mProgressView = findViewById(R.id.login_progress2);
        setBroadCastReceiver();
        setButton();

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            register.setVisibility(show ? View.GONE : View.VISIBLE);
            register.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    username.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            password.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setBroadCastReceiver() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(RegisterService.SERVICE));
    }

    private void setButton() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("a", " created!");
                //get text from screen
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String mail = email.getText().toString();


                Intent register = new Intent(RegisterActivity.this,RegisterService.class);
                register.putExtra("name",name);
                register.putExtra("email", mail);
                register.putExtra("password",pass);
                Log.i("a", "onClick: made it this far");
                startService(register);
                showProgress(true);


            }
        });
    }

}
