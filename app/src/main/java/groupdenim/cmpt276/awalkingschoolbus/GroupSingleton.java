package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Joziak on 3/3/2018.
 */

public class GroupSingleton {
    private List<Group> groups = new ArrayList<>();
    private static GroupSingleton instance;

    private GroupSingleton() {}

    public static GroupSingleton getInstance() {
        if (instance == null) {
            instance = new GroupSingleton();
        }
        return instance;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }
}
