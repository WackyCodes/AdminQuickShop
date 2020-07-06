package com.ecom.letsshop.admin.notifications;

public class NotificationsModel {

    private int notifyType;
    private String notificationId;
    private String notifyImage;
    private String headText;
    private String subHeadingText;

    // Notification for OrderRequest...
    private String notifyOrderId;
    private String notifyUserId;

    public NotificationsModel(int notifyType, String notifyOrderId, String notifyUserId) {
        this.notifyType = notifyType;
        this.notifyOrderId = notifyOrderId;
        this.notifyUserId = notifyUserId;
    }

    public String getNotifyOrderId() {
        return notifyOrderId;
    }

    public void setNotifyOrderId(String notifyOrderId) {
        this.notifyOrderId = notifyOrderId;
    }

    public String getNotifyUserId() {
        return notifyUserId;
    }

    public void setNotifyUserId(String notifyUserId) {
        this.notifyUserId = notifyUserId;
    }

    public NotificationsModel(int notifyType, String notificationId, String notifyImage, String headText, String subHeadingText) {
        this.notifyType = notifyType;
        this.notificationId = notificationId;
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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
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
