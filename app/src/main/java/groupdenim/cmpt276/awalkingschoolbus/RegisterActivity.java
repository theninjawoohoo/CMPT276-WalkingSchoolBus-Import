package groupdenim.cmpt276.awalkingschoolbus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {

    //set up fields
    Button register;
    private EditText username_et;
    private EditText password_et;
    private EditText email_et;
    private View mProgressView;
    private WebService proxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //you know what this does.
        register = findViewById(R.id.button_register);
        username_et = findViewById(R.id.EditText_user);
        password_et = findViewById(R.id.EditText_pass);
        email_et = findViewById(R.id.EditText_email);
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
                String name = username_et.getText().toString();
                String pass = password_et.getText().toString();
                String mail = email_et.getText().toString();

                User userServer = new User();
                userServer.setName(name);
                userServer.setPassword(pass);
                userServer.setEmail(mail);

                attemptRegister(userServer,mail,pass,name);

            }
        });
    }

    private void attemptRegister(User userServer, String email, String pass, String name) {
        Boolean cancel = false;

        // Reset errors.
        email_et.setError(null);
        password_et.setError(null);


        // Check for a valid password_et, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            password_et.setError(getString(R.string.error_invalid_password));
            cancel=true;
            showProgress(false);
            return;
        }

        // Check for a valid email_et address.
        if (TextUtils.isEmpty(email)) {
            email_et.setError(getString(R.string.error_field_required));
            showProgress(false);
            cancel = true;
            return;
        } else if (!isEmailValid(email)) {
            email_et.setError(getString(R.string.error_invalid_email));
            showProgress(false);
            cancel = true;
            return;
        }
        else if (!isNameValid(name)) {
            username_et.setError(getString(R.string.error_invalid_email));
            showProgress(false);
            cancel = true;
            return;
        }


        if (!cancel) {
            Call<User> caller = proxy.registerUser(userServer);
            ProxyBuilder.callProxy(RegisterActivity.this, caller, returnedUser -> response(returnedUser));
        }
    }

    private boolean isEmailValid(String email) {
        if (email.length()>6 && email.contains("@")) {
            return true;
        }
       return false;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    private boolean isNameValid(String password) {
        return password.length() >= 4;
    }



    private void response(User userServer) {
        Log.w("TAG", "Server replied with userServer: " + userServer.toString());
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
                    username_et.setVisibility(show ? View.GONE : View.VISIBLE);
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
            password_et.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
