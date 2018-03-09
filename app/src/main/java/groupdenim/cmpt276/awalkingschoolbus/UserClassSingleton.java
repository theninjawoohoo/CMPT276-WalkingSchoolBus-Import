package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-03-06.
 */

public class UserClassSingleton
{

    private static List<User> userList =new ArrayList<>();

    public static List<User> getInstance() {
        if (userList == null) {
            userList = new ArrayList<>();
        }
        return userList;
    }



}
