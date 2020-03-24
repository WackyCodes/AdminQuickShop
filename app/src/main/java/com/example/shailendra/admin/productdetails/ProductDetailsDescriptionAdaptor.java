package com.example.shailendra.admin.productdetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsDescriptionAdaptor extends FragmentPagerAdapter {

    int totalTabs;
    private String productSpecificationText;
    private List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList;

    public ProductDetailsDescriptionAdaptor(FragmentManager fm, int totalTabs,String productSpecificationText,
                                            List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList) {
        super( fm );
        this.totalTabs = totalTabs;
        this.productSpecificationText = productSpecificationText;
        this.productDetailsSpecificationModelList = productDetailsSpecificationModelList;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProductDetailsDescriptionFragment productDetailsDescriptionFragment = new ProductDetailsDescriptionFragment();
                productDetailsDescriptionFragment.productDescription = productSpecificationText;
                return productDetailsDescriptionFragment;
            case 1:
                ProductDetailsSpecificationFragment productDetailsSpecificationFragment = new ProductDetailsSpecificationFragment();
                productDetailsSpecificationFragment.productDetailsSpecificationModelList = productDetailsSpecificationModelList;
                return productDetailsSpecificationFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
