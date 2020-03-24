package com.example.shailendra.admin.productdetails;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.admin.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsSpecificationAdaptor extends RecyclerView.Adapter<ProductDetailsSpecificationAdaptor.ViewHolder> {

    public static List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();

    public ProductDetailsSpecificationAdaptor(List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList) {
        this.productDetailsSpecificationModelList = productDetailsSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productDetailsSpecificationModelList.get( position ).getType()){
            case 0:
                return ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_TITLE;
            case 1:
                return ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductDetailsSpecificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_TITLE:
                TextView spTitle = new TextView( parent.getContext() );
                spTitle.setTypeface( null, Typeface.BOLD );
                spTitle.setTextColor( Color.parseColor( "#000000" ) );
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                layoutParams.setMargins( setDp( 16, parent.getContext() ),setDp( 16, parent.getContext() )
                        ,setDp( 16, parent.getContext() ),setDp( 12, parent.getContext() ) );
                spTitle.setLayoutParams( layoutParams );
                return  new ViewHolder( spTitle );

            case ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_BODY:
                View view = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.product_detail_specification_item, parent, false );
                return new ViewHolder( view );

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsSpecificationAdaptor.ViewHolder holder, int position) {

        switch (productDetailsSpecificationModelList.get( position ).getType()){
            case ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_TITLE:
                holder.setSpTitle( productDetailsSpecificationModelList.get( position ).getSpTitle());
                break;
            case ProductDetailsSpecificationModel.PRODUCT_SPECIFICATION_BODY:
                String featureTitle = productDetailsSpecificationModelList.get( position ).getFeatureName();
                String featureDetails = productDetailsSpecificationModelList.get( position ).getFeatureValue();
                holder.setResource(featureTitle, featureDetails);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return productDetailsSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        TextView featureValue;
        TextView spTitle;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
        }

        public void setSpTitle(String title){
            spTitle = (TextView) itemView;
            spTitle.setText( title );
        }

        public void setResource(String featureTitle, String featureDetails) {
            featureName = itemView.findViewById( R.id.feature_name );
            featureValue = itemView.findViewById( R.id.feature_value );
            featureName.setText( featureTitle );
            featureValue.setText( featureDetails );
        }
    }

    private int setDp(int dp, Context context){
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp ,context.getResources().getDisplayMetrics() );
    }
}
