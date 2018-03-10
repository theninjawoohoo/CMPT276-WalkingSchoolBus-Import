package groupdenim.cmpt276.awalkingschoolbus;

import java.util.HashMap;
import java.util.Map;

public class UserSingleton {
    private static UserSingleton instance;
    private static Map<String, User> userMap = new HashMap<>(); //userEmail, User
    private static String currentUserEmail = "tempEmail"; //get it from the login

    private UserSingleton() {}

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void addUser(User user) {
        userMap.put(user.getEmail(), user);
    }

    public User getUser(String userEmail) {
        return userMap.get(userEmail);
    }
}
