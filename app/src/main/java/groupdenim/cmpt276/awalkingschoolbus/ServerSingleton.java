package groupdenim.cmpt276.awalkingschoolbus;

import java.util.HashMap;
import java.util.Map;


public class ServerSingleton {


    private static Map<String, User> userMap ; //userEmail, User

    private static String currentUserEmail = "tempEmail"; //get it from the login
    private static String currentUserToken="";

    private ServerSingleton() {}


    public static void addStuff()
    {
        currentUserEmail="jack@";
        User userOne=new User("Jack",currentUserEmail);
        userOne.getGroups().add("1");
        userOne.getPeopleUserIsMonitoring().add("iris@"); userOne.getPeopleUserIsMonitoring().add("paul@");
        userOne.getPeopleMonitoringUser().add("brian@");  userOne.getPeopleMonitoringUser().add("josh@");


        User userTwo=new User("Iris","iris@");
        User userThree=new User("Paul","paul@");
        User userFour=new User("Brian","brian@");
        User userFive=new User("Josh","josh@");
        User userSix=new User("Steve","steve@");

        userMap.put("jack@",userOne);
        userMap.put("iris@",userTwo);
        userMap.put("paul@",userThree);
        userMap.put("josh@",userFive);
        userMap.put("brian@",userFour);
        userMap.put("steve@",userSix);

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
