package groupdenim.cmpt276.awalkingschoolbus;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Farhan on 2018-03-06.
 */

public interface WebService {

    String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    String FEED_LOGIN = "/login";
    String FEED_REGISTER = "/users/signup";
    String APIKEY= "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(FEED_LOGIN)
    Call<Void> getLogin();

    @Headers("apiKey: " + APIKEY)
    @POST(FEED_REGISTER)
    Call<User> registerUser(@Body User user);
}
