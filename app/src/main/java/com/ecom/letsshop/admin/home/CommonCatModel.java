package com.ecom.letsshop.admin.home;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;

import java.util.List;

public class CommonCatModel {
    private int layoutType;
    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    private String layoutID;
    public String getLayoutID() {
        return layoutID;
    }
    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }

    //------- Layout for Category icon and Banner Slider...
    private List<BannerAndCatModel> bannerAndCatModelList;
    public CommonCatModel(int layoutType, String layoutID, List <BannerAndCatModel> bannerAndCatModelList) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.bannerAndCatModelList = bannerAndCatModelList;
    }
    public List <BannerAndCatModel> getBannerAndCatModelList() {
        return bannerAndCatModelList;
    }
    public void setBannerAndCatModelList(List <BannerAndCatModel> bannerAndCatModelList) {
        this.bannerAndCatModelList = bannerAndCatModelList;
    }
    //------- Layout for Category icon and Banner Slider...

    //------ Layout for Strip and Banner ad....
    private String stripAndBannerAdImg;
    private String stripAndBannerAdBg;
    public CommonCatModel(int layoutType, String layoutID, String stripAndBannerAdImg, String stripAndBannerAdBg) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.stripAndBannerAdImg = stripAndBannerAdImg;
        this.stripAndBannerAdBg = stripAndBannerAdBg;
    }
    public String getStripAndBannerAdImg() {
        return stripAndBannerAdImg;
    }
    public void setStripAndBannerAdImg(String stripAndBannerAdImg) {
        this.stripAndBannerAdImg = stripAndBannerAdImg;
    }
    public String getStripAndBannerAdBg() {
        return stripAndBannerAdBg;
    }
    public void setStripAndBannerAdBg(String stripAndBannerAdBg) {
        this.stripAndBannerAdBg = stripAndBannerAdBg;
    }
    //------ Layout for Strip and banner ad....

    // ------- Horizontal and Grid View Item View ..----------------
    // Product Id List...
    private String hrAndGridLayoutTitle;
    private  List<String> hrAndGridLayoutProductIdList;
    private  List<HrLayoutItemModel> hrAndGridProductsDetailsList;
    public CommonCatModel(int layoutType, String layoutID, String hrAndGridLayoutTitle, List <String> hrAndGridLayoutProductIdList, List <HrLayoutItemModel> hrAndGridProductsDetailsList) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.hrAndGridLayoutTitle = hrAndGridLayoutTitle;
        this.hrAndGridLayoutProductIdList = hrAndGridLayoutProductIdList;
        this.hrAndGridProductsDetailsList = hrAndGridProductsDetailsList;
    }
    public String getHrAndGridLayoutTitle() {
        return hrAndGridLayoutTitle;
    }
    public void setHrAndGridLayoutTitle(String hrAndGridLayoutTitle) {
        this.hrAndGridLayoutTitle = hrAndGridLayoutTitle;
    }
    public List <String> getHrAndGridLayoutProductIdList() {
        return hrAndGridLayoutProductIdList;
    }
    public void setHrAndGridLayoutProductIdList(List <String> hrAndGridLayoutProductIdList) {
        this.hrAndGridLayoutProductIdList = hrAndGridLayoutProductIdList;
    }
    public List <HrLayoutItemModel> getHrAndGridProductsDetailsList() {
        return hrAndGridProductsDetailsList;
    }
    public void setHrAndGridProductsDetailsList(List <HrLayoutItemModel> hrAndGridProductsDetailsList) {
        this.hrAndGridProductsDetailsList = hrAndGridProductsDetailsList;
    }
    // ------- Horizontal and Grid View Item View ..----------------

    // Add New Layout....
    public CommonCatModel(int layoutType) {
        this.layoutType = layoutType;
    }


}
