package bupt.androidsipchat.datestruct;

/**
 * Created by sheju on 2017/7/4.
 *
 */

public class MessageStruct {
    private int messageImage;
    private String title;
    private String content;
    private int messageId;
    private int viewType;
    private String subTitle;


    public MessageStruct(int messageImage, String title, String content, int messageId) {
        this.messageImage = messageImage;
        this.title = title;
        this.content = content;
        this.messageId = messageId;
    }


    public int getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(int messageImage) {
        this.messageImage = messageImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
