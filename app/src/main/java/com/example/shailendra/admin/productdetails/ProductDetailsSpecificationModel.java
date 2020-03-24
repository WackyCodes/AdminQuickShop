package com.example.shailendra.admin.productdetails;

public class ProductDetailsSpecificationModel {
    public static final int PRODUCT_SPECIFICATION_TITLE = 0;
    public static final int PRODUCT_SPECIFICATION_BODY = 1;

    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    // Specification title...
    private String spTitle;

    public ProductDetailsSpecificationModel(int type, String spTitle) {
        this.type = type;
        this.spTitle = spTitle;
    }
    public String getSpTitle() {
        return spTitle;
    }
    public void setSpTitle(String spTitle) {
        this.spTitle = spTitle;
    }
// Specification title...

    // Specification body...
    private String featureName;
    private String featureValue;

    public ProductDetailsSpecificationModel(int type, String featureName, String featureValue) {
        this.type = type;
        this.featureName = featureName;
        this.featureValue = featureValue;
    }
    public String getFeatureName() {
        return featureName;
    }
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    public String getFeatureValue() {
        return featureValue;
    }
    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    // Specification body...


}
