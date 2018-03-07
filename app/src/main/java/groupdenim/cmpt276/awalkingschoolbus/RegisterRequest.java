package groupdenim.cmpt276.awalkingschoolbus;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Farhan on 2018-03-03.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/signup";
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> requestHeaders = new HashMap<>();
    private String apiKey = "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";
    private String requestBody = "";
    JSONObject jsonBody = new JSONObject();

    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener error) {

        super(Request.Method.POST,REGISTER_URL, listener, error);
        //set parameters and headers
        requestParams.put("name", "test");
        requestParams.put("email", email);
        requestParams.put("password", password);
        requestHeaders.put("apiKey", apiKey);
        requestHeaders.put("Content-Type", "application/json");

        //create json object to pass to the request body. To be used by getBody().
        createJsonObject(name,email,password);
    }

    private void createJsonObject(String name, String email, String password) {
        //create json object
        try {
            jsonBody.put("name", name);
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
