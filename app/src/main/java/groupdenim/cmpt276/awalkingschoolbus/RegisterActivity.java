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

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {

    //set up fields
    Button register;
    private EditText username;
    private EditText password;
    private EditText email;
    private View mProgressView;
    private WebService proxy;


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


        proxy = ProxyBuilder.getProxy(getString(R.string.api_key), null);
        setButton();

    }

    private void setButton() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                //get text from screen
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String mail = email.getText().toString();

                User user = new User();
                user.setName(name);
                user.setPassword(pass);
                user.setEmail(mail);

                Call<User> caller = proxy.registerUser(user);
                ProxyBuilder.callProxy(RegisterActivity.this, caller, returnedUser -> response(returnedUser));


            }
        });
    }

    private void response(User user) {
        Log.w("TAG", "Server replied with user: " + user.toString());
        showProgress(false);
        finish();
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

}
