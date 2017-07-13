package bupt.androidsipchat.datestruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheju on 2017/7/12.
 */

public class DialogMessage {
    public String selfName;
    public String toName;
    public int idSequence = 0;
    public static int nums = 0;

    public boolean isChatRoom = false;
    public int id = 0;

    public List<MessageStruct> messageStructs = new ArrayList<>();

    public DialogMessage(String s, String t) {
        id = nums;
        nums++;
        selfName = s;
        toName = t;
    }


}
