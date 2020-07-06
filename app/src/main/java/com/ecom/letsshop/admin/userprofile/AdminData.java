package com.ecom.letsshop.admin.userprofile;

public class AdminData {

    private String adminName;
    private String adminMobile;
    private String adminEmail;
    private boolean adminPermission;
    private String adminType;
    private String adminPhoto;
    private String adminAddress;
    private String adminAddPin;
    private String adminId;
    private String adminAuthId;
    private String adminCityName;
    private String adminAreaCode;


    public AdminData() {
    }


    public AdminData(String adminName, String adminMobile, String adminEmail, boolean adminPermission,
                     String adminType, String adminPhoto, String adminAddress, String adminAddPin, String adminId) {
        this.adminName = adminName;
        this.adminMobile = adminMobile;
        this.adminEmail = adminEmail;
        this.adminPermission = adminPermission;
        this.adminType = adminType;
        this.adminPhoto = adminPhoto;
        this.adminAddress = adminAddress;
        this.adminAddPin = adminAddPin;
        this.adminId = adminId;
    }

    public AdminData(String adminName, String adminMobile, String adminEmail, boolean adminPermission, String adminType, String adminPhoto,
                     String adminAddress, String adminAddPin, String adminId, String adminAuthId, String adminCityName, String adminAreaCode) {
        this.adminName = adminName;
        this.adminMobile = adminMobile;
        this.adminEmail = adminEmail;
        this.adminPermission = adminPermission;
        this.adminType = adminType;
        this.adminPhoto = adminPhoto;
        this.adminAddress = adminAddress;
        this.adminAddPin = adminAddPin;
        this.adminId = adminId;
        this.adminAuthId = adminAuthId;
        this.adminCityName = adminCityName;
        this.adminAreaCode = adminAreaCode;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(boolean adminPermission) {
        this.adminPermission = adminPermission;
    }

    public String getAdminType() {
        return adminType;
    }

    public void setAdminType(String adminType) {
        this.adminType = adminType;
    }

    public String getAdminPhoto() {
        return adminPhoto;
    }

    public void setAdminPhoto(String adminPhoto) {
        this.adminPhoto = adminPhoto;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public String getAdminAddPin() {
        return adminAddPin;
    }

    public void setAdminAddPin(String adminAddPin) {
        this.adminAddPin = adminAddPin;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminAuthId() {
        return adminAuthId;
    }

    public void setAdminAuthId(String adminAuthId) {
        this.adminAuthId = adminAuthId;
    }

    public String getAdminCityName() {
        return adminCityName;
    }

    public void setAdminCityName(String adminCityName) {
        this.adminCityName = adminCityName;
    }

    public String getAdminAreaCode() {
        return adminAreaCode;
    }

    public void setAdminAreaCode(String adminAreaCode) {
        this.adminAreaCode = adminAreaCode;
    }
}
