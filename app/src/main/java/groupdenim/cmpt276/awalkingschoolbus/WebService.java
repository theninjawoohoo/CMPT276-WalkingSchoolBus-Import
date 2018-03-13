package groupdenim.cmpt276.awalkingschoolbus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Farhan on 2018-03-06.
 */

public interface WebService {

    String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    String FEED_LOGIN = "/login";
    String FEED_REGISTER = "/users/signup";
    String FEED_GETUSERLIST = "/users";
    String APIKEY= "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";



    //@Headers("apiKey: " + APIKEY)
    @POST(FEED_LOGIN)
    Call<Void> getLogin(@Body UserServer userServer);

    //@Headers("apiKey: " + APIKEY)
    @POST(FEED_REGISTER)
    Call<UserServer> registerUser(@Body UserServer userServer);

    @GET(FEED_GETUSERLIST)
    Call<List<UserServer>> getUserList();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userId);
}
