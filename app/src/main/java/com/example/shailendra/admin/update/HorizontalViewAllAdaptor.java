package com.example.shailendra.admin.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticMethods;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.addnewitem.AddNewLayoutActivity;
import com.example.shailendra.admin.catlayouts.BannerAndCatModel;
import com.example.shailendra.admin.catlayouts.HrLayoutItemModel;
import com.example.shailendra.admin.database.UpdateImages;
import com.example.shailendra.admin.home.CommonCatActivity;
import com.example.shailendra.admin.home.CommonCatModel;
import com.example.shailendra.admin.productdetails.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static com.example.shailendra.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.tempProductAreaCode;
import static com.example.shailendra.admin.StaticValues.userCityName;
import static com.example.shailendra.admin.home.MainFragment.commonCatList;

public class HorizontalViewAllAdaptor extends RecyclerView.Adapter <HorizontalViewAllAdaptor.ViewHolder> {

    private List <HrLayoutItemModel> horizontalProductList;
    private List <String> layoutIdList;
    private List <Integer> layoutIndexList;
    private String layoutId;
    private int layIndex;
    private int catIndex;
    private String catTitle;

    public HorizontalViewAllAdaptor(List <HrLayoutItemModel> horizontalProductList, List <Integer> layoutIndexList, String layoutId,int layIndex, int catIndex, String catTitle) {
        this.horizontalProductList = horizontalProductList;
        this.layoutIndexList = layoutIndexList;
        this.layoutId = layoutId;
        this.layIndex = layIndex;
        this.catIndex = catIndex;
        this.catTitle = catTitle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.view_all_horizontal_item_layout, parent, false );
        return new ViewHolder( view );
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HrLayoutItemModel hrLayoutItemModel = horizontalProductList.get( position );
        String productId = hrLayoutItemModel.getHrProductId();
        String imageLink = hrLayoutItemModel.getHrProductImage();
        String name = hrLayoutItemModel.getHrProductName();
        String price = hrLayoutItemModel.getHrProductPrice();
        String cutPrice = hrLayoutItemModel.getHrProductCutPrice();
        long stocks = hrLayoutItemModel.getHrStockInfo();

        holder.setData( productId, position, imageLink, name, price, cutPrice, stocks );

    }
    @Override
    public int getItemCount() {
        return horizontalProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Dialog dialog;
        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView hrProductStockInfo;
        ImageButton editLayButton;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            hrProductImage = itemView.findViewById( R.id.hr_product_image );
            hrProductName = itemView.findViewById( R.id.hr_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_off_percentage );
            hrProductStockInfo = itemView.findViewById( R.id.stock_text );
            editLayButton = itemView.findViewById( R.id.imageButton );
        }

        private void setData(final String productId, final int index, String imgLink, String name, String price, String cutPrice, long stocks) {

            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );

            if (stocks > 0){
                hrProductStockInfo.setText( "In Stock ("+ stocks + ")" );
            }else{
                hrProductStockInfo.setText( "Out of Stock" );
            }
            Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.square_placeholder ) ).into( hrProductImage );

            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( perOff + "% Off" );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StaticValues.UPDATE_P_LAY_INDEX = layIndex;
                    StaticValues.UPDATE_P_CAT_INDEX = catIndex;
                    StaticValues.UPDATE_PRODUCT_CAT = catTitle;
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

            editLayButton.setEnabled( false );
            editLayButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList <String>();
//                    dogsList.add( "Move::" + StaticValues.ID_MOVE );
//                    dogsList.add( "Copy::" + StaticValues.ID_COPY );
//                    dogsList.add( "Delete::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(), false, index, productId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );

        }

    }
    // More Option CLick...
    public PopupWindow popupWindowDogs(final Context context, final boolean isCopyMove, final int productIndex, final String productId, String popUpContents[]) {
        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(context);
        final Dialog dialog = new DialogsClass().progressDialog( context );
        // the drop down list is a list view
        ListView listViewDogs = new ListView(context);
        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter( StaticMethods.dogsAdapter( popUpContents, context ));
        listViewDogs.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                if (!dialog.isShowing()){
                    dialog.show();
                }
                if (isCopyMove){
                    String layCopyMoveID = ((TextView) view).getText().toString();
                    String selectedItemTag = ((TextView) view).getTag().toString();
                    int ind = Integer.parseInt( layCopyMoveID );

                    switch (Integer.parseInt( selectedItemTag )){
                        case StaticValues.ID_COPY:
                            // Pass : Map, isMoveReq, Dialog, DocId_CPY,
                            updateDataQuery( context, dialog, ind, productIndex,false, productId );
                            popupWindow.dismiss();
                            break;
                        case StaticValues.ID_MOVE:
                            updateDataQuery( context, dialog, ind, productIndex,true, productId );
                            popupWindow.dismiss();
                            break;
                        default:
                            break;
                    }

                }
                else{
                    String selectedItemTag = ((TextView) view).getTag().toString();
                    int temp;
                    List<String> copyMoveList = new ArrayList <String>();
                    String[] popUpContents;
                    switch (Integer.parseInt( selectedItemTag )){
                        case StaticValues.ID_COPY:
                        case StaticValues.ID_MOVE:
                            //  MOVE & COPY
                            copyMoveList.clear();
                            for (int i = 0; i < layoutIndexList.size(); i++){
                                temp = layoutIndexList.get( i );
                                copyMoveList.add( temp + "::" + selectedItemTag);
                            }
                            // convert to simple array...
                            popUpContents = new String[copyMoveList.size()];
                            copyMoveList.toArray(popUpContents);
                            popupWindow.dismiss();
                            dialog.dismiss();
                            PopupWindow popupWindowDogs = popupWindowDogs( context, true, productIndex, productId, popUpContents );
                            popupWindowDogs.showAsDropDown( view );
//                            popupWindowDogs.setHeight(  );
                            break;
                        case StaticValues.ID_DELETE:
                            removeProductFromDocument(dialog, context, productIndex, productId, layoutId );
                            popupWindow.dismiss();
                            break;
                        default:
                            break;
                    }
                }

            }
        } );

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth( WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight( WindowManager.LayoutParams.WRAP_CONTENT);
        // set the list view as pop up window content...
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private List<String> productIdList = new ArrayList <>();
    private void updateDataQuery(final Context context, final Dialog dialog, final int upIndex, final int crrIndex,
                                 final boolean isMoveRequest, final String productId ){
        /*
         1. context
         2. index : where we have to copy or move
         3. product ID
         4. dialog
         5. isMoveRequest
         */
        productIdList = commonCatList.get( catIndex ).get( upIndex ).getHrAndGridLayoutProductIdList();
        productIdList.add( productId );

        String layCopyMoveID = commonCatList.get( catIndex ).get( upIndex ).getLayoutID();

        Map <String, Object> layoutMap = new HashMap <>();
        layoutMap.put( "layout_title", commonCatList.get( catIndex ).get( upIndex ).getHrAndGridLayoutTitle() );
        layoutMap.put( "no_of_products", productIdList.size() );
        layoutMap.put( "index", upIndex );
        layoutMap.put( "layout_id", layCopyMoveID );
        layoutMap.put( "visibility", true );
        layoutMap.put( "view_type", commonCatList.get( catIndex ).get( upIndex ).getLayoutType() );

        for (int i = 0; i < productIdList.size(); i++ ){
            layoutMap.put( "product_id_"+(1+i), productIdList.get( i ) );
        }

        // Create Model to Update in Local...
        final HrLayoutItemModel hrLayoutItemModel = new HrLayoutItemModel( horizontalProductList.get( crrIndex ).getHrProductId()
                , horizontalProductList.get( crrIndex ).getHrProductImage()
                , horizontalProductList.get( crrIndex ).getHrProductName()
                , horizontalProductList.get( crrIndex ).getHrProductPrice()
                , horizontalProductList.get( crrIndex ).getHrProductCutPrice()
                , horizontalProductList.get( crrIndex ).getHrStockInfo()
                , horizontalProductList.get( crrIndex ).getHrCodInfo() );
//----------------------------

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( catTitle.toUpperCase() )
                    .collection( "LAYOUTS" ).document( layCopyMoveID ).set( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                // Success Message...
                                if (isMoveRequest){
                                    removeProductFromDocument(dialog, context, crrIndex, productId, layoutId );
                                }else {
                                    dialog.dismiss();
                                    showToast( context, "Update Successfully.!" );
                                }
                                // Update in Local List...
                                commonCatList.get( catIndex ).get( upIndex ).setHrAndGridLayoutProductIdList( productIdList );
                                commonCatList.get( catIndex ).get( upIndex ).getHrAndGridProductsDetailsList().add( hrLayoutItemModel );
//                            String hrProductId, String hrProductImage, String hrProductName, String hrProductPrice,
//                                    String hrProductCutPrice, long hrStockInfo, Boolean hrCodInfo)
                            }else{
                                dialog.dismiss();
                            }
                        }
                    } );
        }else{
            dialog.dismiss();
            showToast( context, "Something Went Wrong.!" );
        }

    }

    private void removeProductFromDocument(final Dialog dialog, final Context context, final int productIndex, String productId, String documentId){
        Map <String, Object> layoutMap = new HashMap <>();
        List<String> tempProductList = new ArrayList <>();
        for (int i = 0; i < horizontalProductList.size(); i++){
            tempProductList.add( horizontalProductList.get( i ).getHrProductId() );
            if ( !productId.equals( horizontalProductList.get( i ).getHrProductId() )){
                layoutMap.put("product_id_"+(1+i), horizontalProductList.get( i ).getHrProductId() );
            }
        }
        layoutMap.put( "no_of_products", horizontalProductList.size()-1 );

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( catTitle.toUpperCase() )
                    .collection( "LAYOUTS" ).document( documentId ).update( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                //  Change in Local data...
                                commonCatList.get( catIndex ).get( layIndex ).getHrAndGridProductsDetailsList().remove( productIndex );
                                commonCatList.get( catIndex ).get( layIndex ).getHrAndGridLayoutProductIdList().remove( productIndex );
                                ViewAllActivity.horizontalItemViewAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                                showToast( context, "Success.!" );
                            }else{
                                dialog.dismiss();
                                showToast( context, "Something went wrong, Process failed.!" );
                            }
                        }
                    } );

        }else{
            dialog.dismiss();
            showToast( context, "Something Went Wrong.!" );
        }


    }

    private void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

}

/*




 */
