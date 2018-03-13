package groupdenim.cmpt276.awalkingschoolbus;

import java.util.HashMap;
import java.util.Map;


public class UserSingleton {


    private static Map<String, User> userMap ; //userEmail, UserServer

    private static String currentUserEmail = "tempEmail"; //get it from the login
    private static String currentUserToken="";

    private UserSingleton() {}


    public static void addStuff()
    {
        currentUserEmail="jack@";
        User userServerOne =new User("Jack",currentUserEmail);
        userServerOne.getGroups().add("1");
        userServerOne.getPeopleUserIsMonitoring().add("iris@"); userServerOne.getPeopleUserIsMonitoring().add("paul@");
        userServerOne.getPeopleMonitoringUser().add("brian@");  userServerOne.getPeopleMonitoringUser().add("josh@");


        User userServerTwo =new User("Iris","iris@");
        User userServerThree =new User("Paul","paul@");
        User userServerFour =new User("Brian","brian@");
        User userServerFive =new User("Josh","josh@");
        User userServerSix =new User("Steve","steve@");

        userMap.put("jack@", userServerOne);
        userMap.put("iris@", userServerTwo);
        userMap.put("paul@", userServerThree);
        userMap.put("josh@", userServerFive);
        userMap.put("brian@", userServerFour);
        userMap.put("steve@", userServerSix);

    }

    public static Map<String, User> getUserMap() {
        if(userMap==null) {
            userMap = new HashMap<>();
            addStuff();
        }

        return userMap;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

}
