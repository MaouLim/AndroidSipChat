package bupt.androidsipchat.datestruct;

import bupt.androidsipchat.R;

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

    private int specialId;


    public MessageStruct(int messageImage, String title, String content, int messageId) {
        this.messageImage = R.drawable.lamu;
        this.title = title;
        this.content = content;
        this.messageId = messageId;
    }

    public MessageStruct(String title, String content, int messageId) {
        this.title = title;
        this.content = content;
        this.messageId = messageId;
    }

    public MessageStruct(MessageStruct m) {
        this.title = m.getTitle();
        this.content = m.getContent();
        this.messageId = m.getMessageId();
        this.subTitle = m.getSubTitle();
        this.viewType = m.getViewType();
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

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }
}
