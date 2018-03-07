package groupdenim.cmpt276.awalkingschoolbus;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;




/**
 * Created by Farhan on 2018-03-06.
 */

public class RegisterService extends IntentService {
    public static final String SERVICE = "Register Service";
    public final String USER = "UserReturned";

    public RegisterService() {
        super("RegisterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(SERVICE, "onHandleIntent: has been called");
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

        //information on returned user stored here
        User returned = new User();

        //get response
        try {
            //Log.i(SERVICE, "onHandleIntent: " + callRegister.execute().body());
            if (callRegister.execute().isSuccessful()) {
                returned = callRegister.execute().body();
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(SERVICE, "onHandleIntent: " + e.getMessage());
        }


        //return data
        Log.i(SERVICE, "Got this far");
        Intent message = new Intent(SERVICE);

        if (result) {
            message.putExtra("name", returned.getName());
        }
        message.putExtra("result",result);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(message);

    }
}
