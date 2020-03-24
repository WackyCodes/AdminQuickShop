package com.example.shailendra.admin.notifications;

public class NotificationsModel {

    private int notifyType;
    private String notifyImage;
    private String headText;
    private String subHeadingText;

    public NotificationsModel(int notifyType, String notifyImage, String headText, String subHeadingText) {
        this.notifyType = notifyType;
        this.notifyImage = notifyImage;
        this.headText = headText;
        this.subHeadingText = subHeadingText;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public String getSubHeadingText() {
        return subHeadingText;
    }

    public void setSubHeadingText(String subHeadingText) {
        this.subHeadingText = subHeadingText;
    }

    public String getNotifyImage() {
        return notifyImage;
    }

    public void setNotifyImage(String notifyImage) {
        this.notifyImage = notifyImage;
    }
}
