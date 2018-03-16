package groupdenim.cmpt276.awalkingschoolbus.serverModel;

import android.content.Context;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import retrofit2.Call;


public class ServerSingleton {

    private static ServerSingleton instance;
    private String TOKEN;
    private String testingPurposeAPI = "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";


    private WebService proxy;

    private ServerSingleton() {
        // Build the server proxy
        proxy = ProxyBuilder.getProxy(testingPurposeAPI, null);
    }

    private void updateProxy(String token) {
        // Build the server proxy
        proxy = ProxyBuilder.getProxy(testingPurposeAPI, token);
    }

    public static ServerSingleton getInstance() {
        if (instance == null) {
            instance = new ServerSingleton();
        }
        return instance;
    }

    public void setToken(String token) {
        instance.TOKEN = token;
    }


    public void getUserListFromServer(Context context, ProxyBuilder.SimpleCallback<List<User>> callback) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<List<User>> caller = proxy.getUserList();
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void getUserById(Context context, ProxyBuilder.SimpleCallback<User> callback, long id) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        Call<User> caller = proxy.getUserById(id);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void getUserByEmail(Context context, ProxyBuilder.SimpleCallback<User> callback, String email) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<User> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void getMonitorUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback, Long id) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<List<User>> caller = proxy.getMonitorUser(id);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    public void monitorUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback, long currentId, long toAddId) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        User user = new User();
        user.setId(toAddId);
        Call<List<User>> caller = proxy.monitorUser(currentId, user);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    public void monitoredByUsers(Context context, ProxyBuilder.SimpleCallback<List<User>> callback, long otherId, long currentUser) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        User user = new User();
        user.setId(currentUser);
        Call<List<User>> caller = proxy.addMonitoredBy(otherId, user);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void stopMonitoringUser(Context context, ProxyBuilder.SimpleCallback<Void> callback, long currentUser, long otherUser) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        Call<Void> caller = proxy.stopMonitoring(currentUser, otherUser);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void deleteGroup(Context context, ProxyBuilder.SimpleCallback<Void> callback, long groupId) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        Call<Void> caller = proxy.deleteGroup(groupId);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void getGroupById(Context context, ProxyBuilder.SimpleCallback<Group> callback, long id) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<Group> caller = proxy.getGroupById(id);
        ProxyBuilder.callProxy(context, caller, callback);
    }


    public void createNewGroup(Context context, ProxyBuilder.SimpleCallback<Group> callback, Group group) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<Group> caller = proxy.createNewGroup(group);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void getGroupList(Context context, ProxyBuilder.SimpleCallback<List<Group>> callback) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<List<Group>> caller = proxy.getGroupList();
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void updateGroupById(Context context, ProxyBuilder.SimpleCallback<Group> callback,
                                long id, Group group) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<Group> caller = proxy.updateGroupById(id, group);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void addNewMemberOfGroup(Context context, ProxyBuilder.SimpleCallback<List<User>> callback,
                                long groupId, long userId) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }
        User user = new User();
        user.setId(userId);

        Call<List<User>> caller = proxy.addNewMemberOfGroup(groupId, user);
        ProxyBuilder.callProxy(context, caller, callback);
    }

    public void removeMemberFromGroup(Context context, ProxyBuilder.SimpleCallback<Void> callback,
                                    long groupId, long userId) {
        if (TOKEN != null) {
            updateProxy(TOKEN);
        }

        Call<Void> caller = proxy.removeMemberFromGroup(groupId, userId);
        ProxyBuilder.callProxy(context, caller, callback);
    }

}
