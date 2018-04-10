package groupdenim.cmpt276.awalkingschoolbus.userModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;

/**
 * Created by wwwfl on 2018-04-09.
 */

//The purpose the of the class is to store a details such as unlockables from the store
public class JSONGamification {
    private boolean[] unlockedCheapNames = new boolean[ShopNicknameItems.cheapNames.length];
    private boolean[] unlockedNormalNames = new boolean[ShopNicknameItems.fairNames.length];
    private boolean[] unlockedExpensiveName = new boolean[ShopNicknameItems.expensiveName.length];
    private String userNickname;

    public JSONGamification() {
        fill(unlockedCheapNames, Boolean.FALSE);
        fill(unlockedNormalNames, Boolean.FALSE);
        fill(unlockedExpensiveName, Boolean.FALSE);
        userNickname = "";
    }



    public boolean[] getUnlockedCheapNames() {
        return unlockedCheapNames;
    }

    public void setUnlockedCheapNames(boolean[] unlockedCheapNames) {
        this.unlockedCheapNames = unlockedCheapNames;
    }

    public boolean[] getUnlockedNormalNames() {
        return unlockedNormalNames;
    }

    public void setUnlockedNormalNames(boolean[] unlockedNormalNames) {
        this.unlockedNormalNames = unlockedNormalNames;
    }

    public boolean[] getUnlockedExpensiveName() {
        return unlockedExpensiveName;
    }

    public void setUnlockedExpensiveName(boolean[] unlockedExpensiveName) {
        this.unlockedExpensiveName = unlockedExpensiveName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
