package groupdenim.cmpt276.awalkingschoolbus.mapModels;

/**
 * Created by wwwfl on 2018-03-20.
 */

public class GroupMeetingDeletionSingleton {
    private static GroupMeetingDeletionSingleton instance;
    private String description;

    public static GroupMeetingDeletionSingleton getInstance(){
        if (instance == null) {
            instance = new GroupMeetingDeletionSingleton();
        }
        return instance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
