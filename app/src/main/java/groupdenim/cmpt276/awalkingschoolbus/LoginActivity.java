package groupdenim.cmpt276.awalkingschoolbus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {




    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView_et;
    private View progressView;
    private View loginFormView;
    private WebService proxy;
    private String TOKEN;
    private static final String LOGIN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView_et = (EditText) findViewById(R.id.password);
        passwordView_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        proxy = ProxyBuilder.getProxy(getString(R.string.api_key), null);
        setupRegisterButton();
        setupLoginButton();
        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        checkIfLoggedIn();
    }

    private void checkIfLoggedIn() {
        SharedPreferences sharedPrefs = getSharedPreferences(LOGIN, 0);
        String email = sharedPrefs.getString("email", "");
        String password = sharedPrefs.getString("password", "");

        if (email != "" && password!="") {
            showProgress(true);
            sendLoginRequest(email,password);
        }
    }


    private void setupLoginButton() {
        Button login = (Button) findViewById(R.id.Button_login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void setupRegisterButton() {
        Button register = (Button) findViewById(R.id.Button_register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {


        // Reset errors.
        emailView.setError(null);
        passwordView_et.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView_et.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView_et.setError(getString(R.string.error_invalid_password));
            focusView = passwordView_et;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            sendLoginRequest(email,password);
        }
    }

    private void sendLoginRequest(String email, String password) {
        User userServer = new User();
        userServer.setEmail(email);
        userServer.setPassword(password);
        Call<Void> caller = proxy.getLogin(userServer);
        ProxyBuilder.setOnTokenReceiveCallback(token -> response(token));
        ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> response(returnedNothing, email, password));
        //resetscreen();
        //Intent intent =new Intent(this,LoginActivity.class);
        //startActivity(intent);
    }

    private void resetscreen() {
        showProgress(false);
        return;
    }

    private void response(Void nothing, String email, String password) {
        Log.i("HEADERRESPONSE", "response: " );
        showProgress(false);
        //start new activity
        populateCurrentUser(email);
        saveLoginInfo(email,password);
    }

    private void saveLoginInfo(String email, String password) {
        SharedPreferences sharedPrefs = getSharedPreferences(LOGIN, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.commit();
    }

    private void populateCurrentUser(String email) {
        Context context = this.getApplicationContext();
        ProxyBuilder.SimpleCallback<User> callback = user -> getuser(user);
        ServerSingleton.getInstance().getUserByEmail(context,callback, email);
    }
    private void getuser(User user) {
        Log.i("a", "getuser: " + user);
        CurrentUserSingleton.setFields(user);
        Intent mainMenu = new Intent(LoginActivity.this, MainMenuActivity.class);
        startActivity(mainMenu);
        finish();
    }

    private void response(String token) {
        TOKEN = token;
        ServerSingleton.getInstance().setToken(token);
        Log.i("DIDWEGETTOKEN", "response: " + token);
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

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }




}

