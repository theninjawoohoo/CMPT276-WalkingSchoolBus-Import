package groupdenim.cmpt276.awalkingschoolbus;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farhan on 2018-03-04.
 */

public class LoginRequest extends StringRequest{

    private static final String LOGIN_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/login";
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> requestHeaders = new HashMap<>();
    private String apiKey = "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";
    private String requestBody = "";
    JSONObject jsonBody = new JSONObject();

    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, LOGIN_URL, listener, errorListener);
        requestParams.put("email", email);
        requestParams.put("password", password);
        requestHeaders.put("apiKey", apiKey);
        requestHeaders.put("Content-Type", "application/json");

        createJsonObject(email,password);
    }

    private void createJsonObject(String email, String password) {
        //create json object
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //convert json to string
        requestBody = jsonBody.toString();
    }

    @Override
    public Map<String, String> getParams() {
        return requestParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return requestHeaders;
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        return requestBody.getBytes();
    }
}
