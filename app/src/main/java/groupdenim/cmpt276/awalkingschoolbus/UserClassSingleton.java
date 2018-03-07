package groupdenim.cmpt276.awalkingschoolbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-03-06.
 */

public class UserClassSingleton
{

    private static List<Member> memberList =new ArrayList<>();

    public static List<Member> getInstance() {
        if (memberList == null) {
            memberList = new ArrayList<>();
        }
        return memberList;
    }



}
