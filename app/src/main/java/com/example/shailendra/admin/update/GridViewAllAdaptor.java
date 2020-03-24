package com.example.shailendra.admin.update;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.catlayouts.HrLayoutItemModel;
import com.example.shailendra.admin.productdetails.ProductDetails;

import java.util.List;

public class GridViewAllAdaptor extends BaseAdapter {

    private List <HrLayoutItemModel> gridProductList;

    public GridViewAllAdaptor(List <HrLayoutItemModel> gridProductList) {
        this.gridProductList = gridProductList;
    }

    @Override
    public int getCount() {
        return gridProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;

        if(convertView == null){
            view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.view_all_square_item_layout, null );
            ImageView itemImage = view.findViewById( R.id.hr_product_image );
            TextView itemName =  view.findViewById( R.id.hr_product_name );
            TextView itemPrice =  view.findViewById( R.id.hr_product_price );
            TextView itemCutPrice =  view.findViewById( R.id.hr_product_cut_price );
            TextView itemOffPer =  view.findViewById( R.id.hr_off_percentage );

            TextView itemStock =  view.findViewById( R.id.stock_text );


            final HrLayoutItemModel horizontalItemViewModel = gridProductList.get( position );
            itemName.setText( horizontalItemViewModel.getHrProductName() );
            itemPrice.setText( "Rs."+ horizontalItemViewModel.getHrProductPrice() + "/-" );
            itemCutPrice.setText( "Rs."+ horizontalItemViewModel.getHrProductCutPrice() + "/-" );

            Glide.with( view.getContext() ).load( horizontalItemViewModel.getHrProductImage() )
                    .apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( itemImage );
//
            int perOff = ((Integer.parseInt( horizontalItemViewModel.getHrProductCutPrice() ) - Integer.parseInt( horizontalItemViewModel.getHrProductPrice() )) * 100) /
                    Integer.parseInt(  horizontalItemViewModel.getHrProductCutPrice() );
            itemOffPer.setText( perOff + "% Off" );


            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoProductDetailIntent = new Intent(parent.getContext(), ProductDetails.class);
                    gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductList.get( position ).getHrProductId() );
                    parent.getContext().startActivity( gotoProductDetailIntent );
                }
            } );

        }else {
            view = convertView;
        }
        return  view;
    }
}
