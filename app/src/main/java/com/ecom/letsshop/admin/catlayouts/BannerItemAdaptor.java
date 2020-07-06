package com.ecom.letsshop.admin.catlayouts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.StaticMethods;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.addnewitem.AddNewLayoutActivity;
import com.ecom.letsshop.admin.update.ViewAllActivity;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static com.ecom.letsshop.admin.StaticValues.tempProductAreaCode;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class BannerItemAdaptor extends RecyclerView.Adapter<BannerItemAdaptor.ViewHolder> {

    List <BannerAndCatModel> bannerAndCatModelList;
    boolean isViewAll;
    int layoutIndex;
    int catIndex;
    String categoryTitle;

    public BannerItemAdaptor(List <BannerAndCatModel> bannerAndCatModelList, boolean isViewAll, int layoutIndex, int catIndex, String categoryTitle) {
        this.bannerAndCatModelList = bannerAndCatModelList;
        this.isViewAll = isViewAll;
        this.layoutIndex = layoutIndex;
        this.catIndex = catIndex;
        this.categoryTitle = categoryTitle;
    }

    @NonNull
    @Override
    public BannerItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.banner_slider_item_layout, parent, false );
        return new ViewHolder( bannerView );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerItemAdaptor.ViewHolder holder, int position) {
        if (position < bannerAndCatModelList.size() ){
            String imgLink = bannerAndCatModelList.get( position ).getImageLink();
            String bgColor = bannerAndCatModelList.get( position ).getTitleOrBgColor();
            holder.setData( imgLink, bgColor );
        }
        if ( !isViewAll && position == bannerAndCatModelList.size() ){
            holder.setAddNew();
        }
    }

    @Override
    public int getItemCount() {
        if (isViewAll){
            return bannerAndCatModelList.size();
        }else {
            return bannerAndCatModelList.size()+1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bannerImage;
        private LinearLayout addNewItemLayout;
        private ImageView editBannerLayBtn;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerImage = itemView.findViewById( R.id.banner_slider_Image );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_Linearlayout );
            editBannerLayBtn = itemView.findViewById( R.id.update_item_lay_imgBtn );
        }

        private void setData(String imgLink, String bgColor ){
            if (isViewAll){
                editBannerLayBtn.setVisibility( View.VISIBLE );
            }
            bannerImage.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.banner_placeholder ) ).into( bannerImage );
            // Set Backgrgound color...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bannerImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( bgColor ) ));
            }
        }
        private void setAddNew(){
            bannerImage.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start Activity...
                    Intent intent = new Intent( itemView.getContext(), AddNewLayoutActivity.class );
                    intent.putExtra( "CAT_TITLE", categoryTitle );
                    intent.putExtra( "CAT_INDEX", catIndex );
                    intent.putExtra( "LAY_TYPE", BANNER_SLIDER_CONTAINER_ITEM );
                    intent.putExtra( "LAY_INDEX", layoutIndex );
                    intent.putExtra( "TASK_UPDATE", false );
                    itemView.getContext().startActivity( intent );
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();
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
//                            copyMoveList.clear();
//                            for (int i = 0; i < layoutIndexList.size(); i++){
//                                temp = layoutIndexList.get( i );
//                                copyMoveList.add( temp + "::" + selectedItemTag);
//                            }
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
//                            removeProductFromDocument(dialog, context, productIndex, productId, layoutId );
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
//        final HrLayoutItemModel hrLayoutItemModel = new HrLayoutItemModel( horizontalProductList.get( crrIndex ).getHrProductId()
//                , horizontalProductList.get( crrIndex ).getHrProductImage()
//                , horizontalProductList.get( crrIndex ).getHrProductName()
//                , horizontalProductList.get( crrIndex ).getHrProductPrice()
//                , horizontalProductList.get( crrIndex ).getHrProductCutPrice()
//                , horizontalProductList.get( crrIndex ).getHrStockInfo()
//                , horizontalProductList.get( crrIndex ).getHrCodInfo() );
//----------------------------

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( "" ) //catTitle.toUpperCase()
                    .collection( "LAYOUTS" ).document( layCopyMoveID ).set( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                // Success Message...
                                if (isMoveRequest){
//                                    removeProductFromDocument(dialog, context, crrIndex, productId, layoutId );
                                }else {
                                    dialog.dismiss();
                                    showToast( context, "Update Successfully.!" );
                                }
                                // Update in Local List...
                                commonCatList.get( catIndex ).get( upIndex ).setHrAndGridLayoutProductIdList( productIdList );
//                                commonCatList.get( catIndex ).get( upIndex ).getHrAndGridProductsDetailsList().add( hrLayoutItemModel );
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
//        for (int i = 0; i < horizontalProductList.size(); i++){
//            tempProductList.add( horizontalProductList.get( i ).getHrProductId() );
//            if ( !productId.equals( horizontalProductList.get( i ).getHrProductId() )){
//                layoutMap.put("product_id_"+(1+i), horizontalProductList.get( i ).getHrProductId() );
//            }
//        }
//        layoutMap.put( "no_of_products", horizontalProductList.size()-1 );

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( "catTitle.toUpperCase()" )
                    .collection( "LAYOUTS" ).document( documentId ).update( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                //  Change in Local data...
//                                commonCatList.get( catIndex ).get( layIndex ).getHrAndGridProductsDetailsList().remove( productIndex );
//                                commonCatList.get( catIndex ).get( layIndex ).getHrAndGridLayoutProductIdList().remove( productIndex );
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
