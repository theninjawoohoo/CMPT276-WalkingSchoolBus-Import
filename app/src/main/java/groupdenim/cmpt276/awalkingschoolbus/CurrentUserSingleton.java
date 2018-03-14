package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-03-13.
 */

class CurrentUserSingleton {

    private static Context context;

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<User> leadsGroups = new ArrayList<>();

    private List<User> memberOfGroups = new ArrayList<>();

    private List<User> monitoredByUsers = new ArrayList<>();

    private List<User> monitorsUsers = new ArrayList<>();

    private static final CurrentUserSingleton instance = new CurrentUserSingleton();

    static CurrentUserSingleton getInstance(Context context) {

        if(instance ==null)
        {
            updateUserSingleton(context);

        }

        return instance;
    }

    public static void updateUserSingleton(Context context)
    {
        ProxyBuilder.SimpleCallback<User> callback = userObj -> setFields(userObj);
        //306 should be nini
        ServerSingleton.getInstance().getUserById(context,callback, instance.id);

    }

    public static void setFields(User user)
    {
        instance.setEmail(user.getEmail());
        instance.setName(user.getName());
        instance.setId(user.getId());
        instance.setPassword(user.getPassword());
        instance.setLeadsGroups(user.getLeadsGroups());
        instance.setMemberOfGroups(user.getMemberOfGroups());
        instance.setMonitorsUsers(user.getMonitorsUsers());
        instance.setMonitoredByUsers(user.getMonitoredByUsers());
        Log.i("a", "setFields: aaaa");
    }

    public void setMemberOfGroups(List<User> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public void setLeadsGroups(List<User> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public void setId(Long id) {
        instance.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private CurrentUserSingleton() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<User> getLeadsGroups() {
        return leadsGroups;
    }

    public List<User> getMemberOfGroups() {
        return memberOfGroups;
    }

    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }


}
