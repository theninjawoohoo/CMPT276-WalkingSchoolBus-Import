package groupdenim.cmpt276.awalkingschoolbus;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Headers;


/**
 * Created by Farhan on 2018-03-06.
 */

public class LoginService extends IntentService {
    public static final String SERVICE = "Register Service";
    public final String USER = "UserReturned";
    boolean result = false;

    public LoginService() {
        super("RegisterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent messageToReturn = new Intent(SERVICE);
        //result = false;

        //Initialize webservice
        WebService webService = WebService.retrofit.create(WebService.class);

        //get data from intent
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");


//        //create user to pass into call
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);


        //make call
        Call<Void> callRegister = webService.getLogin(user);

        //get response
        callRegister.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("bn", "onResponse: response from server");
                if (response.isSuccessful()) {
                    okhttp3.Headers header =  response.headers();
                    String auth = header.get("Authorization");

                    Log.i("gotHeader", "onResponse: " + header);
                    Log.i("H", "onResponse: " + auth);
                    result = true;
                    Log.i("bbb", "onResponse: isSuccessful has run");
                } else {
                    Log.i("aaa", "onResponse: else if not isSuccessful");
                    try {

                        Log.i("f", "onResponse: nope ");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginService.this,error.getString("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("ERROR:", "onFailure: " + t);
                Toast.makeText(LoginService.this,t.getMessage(),Toast.LENGTH_SHORT);
                return;
            }
        });

        //return data
        messageToReturn.putExtra("result",result);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageToReturn);

    }
}
