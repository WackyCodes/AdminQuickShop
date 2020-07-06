package com.ecom.letsshop.admin.productdetails;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecom.letsshop.admin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsDescriptionFragment extends Fragment {

    private TextView productDetailDescription;
    public static String productDescription;

    public ProductDetailsDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_product_details_description, container, false );

        productDetailDescription = view.findViewById( R.id.product_details_description );
        productDetailDescription.setText( productDescription );
//        productDetailDescription.notify();

        return view;
    }

}
