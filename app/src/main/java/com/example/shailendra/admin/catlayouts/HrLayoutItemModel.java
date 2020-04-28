package com.example.shailendra.admin.catlayouts;

public class HrLayoutItemModel {

    private String hrProductId;
    private String hrProductImage;
    private String hrProductName;
    private String hrProductPrice;
    private String hrProductCutPrice;
    private long hrStockInfo;
    private Boolean hrCodInfo;

    public HrLayoutItemModel(String hrProductId, String hrProductImage, String hrProductName, String hrProductPrice,
                             String hrProductCutPrice, long hrStockInfo, Boolean hrCodInfo) {
        this.hrProductId = hrProductId;
        this.hrProductImage = hrProductImage;
        this.hrProductName = hrProductName;
        this.hrProductPrice = hrProductPrice;
        this.hrProductCutPrice = hrProductCutPrice;
        this.hrStockInfo = hrStockInfo;
        this.hrCodInfo = hrCodInfo;
    }


    public String getHrProductId() {
        return hrProductId;
    }

    public void setHrProductId(String hrProductId) {
        this.hrProductId = hrProductId;
    }

    public String getHrProductImage() {
        return hrProductImage;
    }

    public void setHrProductImage(String hrProductImage) {
        this.hrProductImage = hrProductImage;
    }

    public String getHrProductName() {
        return hrProductName;
    }

    public void setHrProductName(String hrProductName) {
        this.hrProductName = hrProductName;
    }

    public String getHrProductPrice() {
        return hrProductPrice;
    }

    public void setHrProductPrice(String hrProductPrice) {
        this.hrProductPrice = hrProductPrice;
    }

    public String getHrProductCutPrice() {
        return hrProductCutPrice;
    }

    public void setHrProductCutPrice(String hrProductCutPrice) {
        this.hrProductCutPrice = hrProductCutPrice;
    }

    public long getHrStockInfo() {
        return hrStockInfo;
    }

    public void setHrStockInfo(long hrStockInfo) {
        this.hrStockInfo = hrStockInfo;
    }

    public Boolean getHrCodInfo() {
        return hrCodInfo;
    }

    public void setHrCodInfo(Boolean hrCodInfo) {
        this.hrCodInfo = hrCodInfo;
    }
}
