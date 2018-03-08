package groupdenim.cmpt276.awalkingschoolbus;

import java.util.HashMap;
import java.util.Map;

public class GroupSingleton {
    private Map<String, Group> groups = new HashMap<>(); //groupName, Group
    private static GroupSingleton instance;

    private GroupSingleton() {}

    public static GroupSingleton getInstance() {
        if (instance == null) {
            instance = new GroupSingleton();
        }
        return instance;
    }

    public void addGroup(Group group) {
        groups.put(group.getGroupName(), group);
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public void setGroup(String groupName, Group group) {
        groups.put(groupName, group);
    }
}
