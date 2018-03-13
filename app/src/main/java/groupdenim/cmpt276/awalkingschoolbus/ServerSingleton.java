package groupdenim.cmpt276.awalkingschoolbus;

import retrofit2.Call;

public class ServerSingleton {
    private ServerSingleton instance;
    private User currentUser;

    private WebService proxy;

    private ServerSingleton() {
        // Build the server proxy
        proxy = ProxyBuilder.getProxy(getString(R.string.api_key), null);
    }

    private ServerSingleton getInstance() {
        if (instance == null) {
            instance = new ServerSingleton();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserById(Integer userId) {
    }


}
