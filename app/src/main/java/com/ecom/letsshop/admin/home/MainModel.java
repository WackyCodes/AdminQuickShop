package com.ecom.letsshop.admin.home;

public class MainModel {


    private String mainImage;
    private String mainHeading;

    public MainModel(String mainImage, String mainHeading) {
        this.mainImage = mainImage;
        this.mainHeading = mainHeading;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainHeading() {
        return mainHeading;
    }

    public void setMainHeading(String mainHeading) {
        this.mainHeading = mainHeading;
    }
}
