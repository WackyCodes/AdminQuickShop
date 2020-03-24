package com.example.shailendra.admin.catlayouts;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.addnewitem.AddNewProductActivity;
import com.example.shailendra.admin.productdetails.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HrLayoutItemAdaptor extends RecyclerView.Adapter<HrLayoutItemAdaptor.ViewHolder> {

    List<HrLayoutItemModel> hrLayoutItemModelList;
    String catTitle;

    public HrLayoutItemAdaptor(List <HrLayoutItemModel> hrLayoutItemModelList, String catTitle) {
        this.hrLayoutItemModelList = hrLayoutItemModelList;
        this.catTitle = catTitle;
    }

    @NonNull
    @Override
    public HrLayoutItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.square_shape_item, parent, false );
        return new ViewHolder( productView );
    }

    @Override
    public void onBindViewHolder(@NonNull HrLayoutItemAdaptor.ViewHolder holder, int position) {
        if (position < hrLayoutItemModelList.size()){
            HrLayoutItemModel hrLayoutItemModel = hrLayoutItemModelList.get( position );
            String productId = hrLayoutItemModel.getHrProductId();
            String imageLink = hrLayoutItemModel.getHrProductImage();
            String name = hrLayoutItemModel.getHrProductName();
            String price = hrLayoutItemModel.getHrProductPrice();
            String cutPrice = hrLayoutItemModel.getHrProductCutPrice();
            long stocks = hrLayoutItemModel.getHrStockInfo();
            holder.setData( productId, imageLink, name, price, cutPrice, stocks );
        }
        if (position == hrLayoutItemModelList.size()){
            holder.addNewItem();
        }

    }

    @Override
    public int getItemCount() {
        return hrLayoutItemModelList.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView hrProductStockInfo;
        // Layout///
        ConstraintLayout itemLayout;
        LinearLayout addNewItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            hrProductImage = itemView.findViewById( R.id.hr_product_image );
            hrProductName = itemView.findViewById( R.id.hr_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_off_percentage );
            hrProductStockInfo = itemView.findViewById( R.id.stock_text );

            itemLayout = itemView.findViewById( R.id.product_layout );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_Linearlayout );
        }

        private void setData(final String productId, String imgLink, String name, String price, String cutPrice, long stocks) {
            itemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );

            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );

            Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.square_placeholder ) ).into( hrProductImage );


            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( perOff + "% Off" );


            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

        }

        private void addNewItem(){
            itemLayout.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();

                    Map <String, Object> newProductMap = new HashMap <>();
                     /** Is_Update :
                        1. N - Created Product but Not add details yet.!
                        2. Y - Added Details and Update...
                        3. V - Visible to search...
                        4. I - InVisible : Stop from searching etc...
                     */
                    newProductMap.put( "a_is_update", "N" );
                    newProductMap.put( "a_no_of_uses", 0 );
                    newProductMap.put( "a_product_cat", catTitle );

                    final Dialog dialog = new DialogsClass().progressDialog( itemView.getContext() );
                    dialog.show();
                    FirebaseFirestore.getInstance().collection( "PRODUCTS" ).add( newProductMap )
                            .addOnCompleteListener( new OnCompleteListener <DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task <DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        // Shift to add product details...
                                        String docId = task.getResult().getId();
                                        Toast.makeText( itemView.getContext(), "Product Id : "+docId, Toast.LENGTH_SHORT ).show();

                                        Intent gotoAddProductIntent = new Intent(itemView.getContext(), AddNewProductActivity.class );
                                        gotoAddProductIntent.putExtra( "PRODUCT_ID", docId );
                                        gotoAddProductIntent.putExtra( "PRODUCT_CAT", catTitle );
                                        itemView.getContext().startActivity( gotoAddProductIntent );
                                    }else{
                                        // Show Toast..  to try again...
                                        Toast.makeText( itemView.getContext(), "Creating New Product Id Failed..! Please Try Again.!", Toast.LENGTH_SHORT ).show();
                                    }
                                    dialog.dismiss();
                                }
                            } );


                }
            } );
        }

    }

}
