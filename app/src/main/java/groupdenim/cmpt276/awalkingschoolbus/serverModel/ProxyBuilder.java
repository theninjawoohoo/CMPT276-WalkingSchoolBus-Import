package groupdenim.cmpt276.awalkingschoolbus.serverModel;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Header;

/**
 * Created by Farhan on 2018-03-10.
 * ProxyBuilder code given by Dr.Fraser.
 */

/**
 * General support for getting the Retrofit proxy object.
 * Adds:
 *   - Logging
 *   - Setting API key header
 *   - Setting Authorization header (and managing token received)
 *
 * For more on Retrofit, see http://square.github.io/retrofit/
 */

public class ProxyBuilder {
    private static final String FAIL_STATUS = "FAIL";
    // Select a server (top one is production one; others are for testing)
    private static final String SERVER_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443/";
//    private static final String SERVER_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:9443/";
//    private static final String SERVER_URL = "https://localhost:8443/";


    // Allow client-code to register callback for when the token is received.
    // NOTE: the current proxy does not upgrade to using the token!
    private static SimpleCallback<String> receivedTokenCallback;
    public static void setOnTokenReceiveCallback(SimpleCallback<String> callback) {
        receivedTokenCallback = callback;
    }

    /**
     * Return the proxy that client code can use to call server.
     * @param apiKey   Your group's API key to communicate with the server.
     * @return proxy object to call the server.
     */


    public static WebService getProxy(String apiKey) {
        return getProxy(apiKey, null);
    }

    /**
     * Return the proxy that client code can use to call server.
     * @param apiKey   Your group's API key to communicate with the server.
     * @param token    The token you have been issued
     * @return proxy object to call the server.
     */
    public static WebService getProxy(String apiKey, String token) {
        // Enable Logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new AddHeaderInterceptor(apiKey, token))
                .build();

        // Build Retrofit proxy object for server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(WebService.class);
    }




    /**
     * Interface for simplifying the callbacks from the server.
     */
    public interface SimpleCallback<T> {
        void callback(T ans);
        //String callback(String token);
    };

    /**
     * Simplify the calling of the "Call"
     * - Handle error checking in one place and log on failure.
     * - Callback to simplified interface on success.
     * @param caller    Call object returned by the proxy
     * @param callback  Client-code to execute when we have a good answer for them.
     * @param <T>       The type of data that Call object is expected to fetch
     */
    public static <T extends Object> void callProxy(Call<T> caller, final SimpleCallback<T> callback) {
        callProxy(null, caller, callback);
    }
    /**
     * Simplify the calling of the "Call"
     * - Handle error checking in one place and put up toast & log on failure.
     * - Callback to simplified interface on success.
     * @param context   Current activity for showing toast if there's an error.
     * @param caller    Call object returned by the proxy
     * @param callback  Client-code to execute when we have a good answer for them.
     * @param <T>       The type of data that Call object is expected to fetch
     */
    public static <T extends Object> void callProxy(
            final Context context, Call<T> caller, final SimpleCallback<T> callback) {
        caller.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {

                // Process the response
                if (response.errorBody() == null) {
                    // Check for authentication token:
                    String tokenInHeader = response.headers().get("Authorization");
                    if (tokenInHeader != null) {
                        if (receivedTokenCallback != null) {
                            receivedTokenCallback.callback(tokenInHeader);
                        } else {
                            // We got the token, but nobody wanted it!
                            Log.w("ProxyBuilder", "WARNING: Received token but no callback registered for it!");
                        }
                    }

                    if (callback != null) {
                        T body = response.body();
                        callback.callback(body);
                    }
                } else {
                    String message;
                    try {
                        message = "CALL TO SERVER FAILED:\n" + response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        message = "Unable to decode response (body or error's body).";
                    }
                    showFailure(message);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                String message = "Server Error: " + t.getMessage();
                showFailure(message);
            }
            private void showFailure(String message) {
                Log.e("ProxyBuilder", message);
                if (context != null) {
                    Intent intent = new Intent(FAIL_STATUS);
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
                    manager.sendBroadcast(intent);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }







    /*
        --------------------------------
        PRIVATE
        --------------------------------
     */
    private static class AddHeaderInterceptor implements Interceptor {
        private String apiKey;
        private String token;

        public AddHeaderInterceptor(String apiKey, String token) {
            this.apiKey = apiKey;
            this.token = token;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder();
            builder.header("permissions-enabled", "true");
            // Add API header
            if (apiKey != null) {
                builder.header("apiKey", apiKey);
            }
            // Add Token
            if (token != null) {
                builder.header("Authorization", token);
            }
            Request modifiedRequest = builder.build();

            return chain.proceed(modifiedRequest);
        }
    }
}