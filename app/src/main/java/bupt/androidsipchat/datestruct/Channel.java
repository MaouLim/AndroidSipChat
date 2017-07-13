package bupt.androidsipchat.datestruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheju on 2017/7/13.
 */

public class Channel {
    String title;
    String subTitle;
    String content;

    public List<MessageStruct> getChannelMessage() {
        return channelMessage;
    }

    public void setChannelMessage(List<MessageStruct> channelMessage) {
        this.channelMessage = channelMessage;
    }

    List<MessageStruct> channelMessage = new ArrayList<>();
    int id;
    public static int nums = 0;


    public Channel(String title, String subTitle, String content) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        id = nums;
        nums++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
