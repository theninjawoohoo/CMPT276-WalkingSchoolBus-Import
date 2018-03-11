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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Farhan on 2018-03-06.
 */

public class RegisterService extends IntentService {
    public static final String SERVICE = "Register Service";
    public final String USER = "UserReturned";
//    boolean result = false;

    public RegisterService() {
        super("RegisterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent messageToReturn = new Intent(SERVICE);
        boolean result = false;

        //Initialize webservice
        WebService webService = WebService.retrofit.create(WebService.class);

        //get data from intent
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");


        //create user to pass into call
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);


        //make call
        Call<User> callRegister = webService.registerUser(user);

        //get response
        callRegister.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("bn", "onResponse: got Response from server");
                if (response.isSuccessful()) {
                    final User userToReturn = response.body();
                    messageToReturn.putExtra("name", userToReturn.getName());
                    messageToReturn.putExtra("result", true);
                    Log.i("bbb", "onResponse: response successful");


                    //return data
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                    manager.sendBroadcast(messageToReturn);


                } else {
                    Log.i("aaa", "onResponse: made it");
                    try {
                        messageToReturn.putExtra("result",false);
                        Log.i("f", "onResponse: nope not successful ");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(RegisterService.this,error.getString("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("ERROR:", "onFailure: " + t);
                Toast.makeText(RegisterService.this,t.getMessage(),Toast.LENGTH_SHORT);
                return;
            }
        });


    }
}
