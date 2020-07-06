package com.ecom.letsshop.admin.catlayouts;

public class BannerAndCatModel {

    // Home Cat Item List...
    private String imageLink;
    private String titleOrBgColor;

    public BannerAndCatModel(String imageLink, String titleOrBgColor) {
        this.imageLink = imageLink;
        this.titleOrBgColor = titleOrBgColor;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTitleOrBgColor() {
        return titleOrBgColor;
    }

    public void setTitleOrBgColor(String titleOrBgColor) {
        this.titleOrBgColor = titleOrBgColor;
    }
}
