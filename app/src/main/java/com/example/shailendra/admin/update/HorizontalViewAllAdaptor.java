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
import com.example.shailendra.admin.catlayouts.HrLayoutItemModel;
import com.example.shailendra.admin.database.UpdateImages;
import com.example.shailendra.admin.productdetails.ProductDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.home.MainFragment.commonCatList;

public class HorizontalViewAllAdaptor extends RecyclerView.Adapter <HorizontalViewAllAdaptor.ViewHolder> {

    private List <HrLayoutItemModel> horizontalProductList;
    private List <String> layoutIdList;
    private List <Integer> layoutIndexList;
    private String layoutId;
    private int catIndex;

    public HorizontalViewAllAdaptor(List <HrLayoutItemModel> horizontalProductList, List <String> layoutIdList, List <Integer> layoutIndexList, String layoutId, int catIndex) {
        this.horizontalProductList = horizontalProductList;
        this.layoutIdList = layoutIdList;
        this.layoutIndexList = layoutIndexList;
        this.layoutId = layoutId;
        this.catIndex = catIndex;
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

            editLayButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList <String>();
                    dogsList.add( "Move::" + StaticValues.ID_MOVE );
                    dogsList.add( "Copy::" + StaticValues.ID_COPY );
                    dogsList.add( "Delete::" + StaticValues.ID_DELETE );
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

                    List<String> productIdList = new ArrayList <>();
                    productIdList = commonCatList.get( catIndex ).get( ind ).getHrAndGridLayoutProductIdList();
                    productIdList.add( productId );

                    layCopyMoveID = commonCatList.get( catIndex ).get( ind ).getLayoutID();
                    //-----------------------------------
                    Map <String, Object> layoutMap = new HashMap <>();
                    layoutMap.put( "layout_title", commonCatList.get( catIndex ).get( ind ).getHrAndGridLayoutTitle() );
                    layoutMap.put( "no_of_products", productIdList.size() );
                    layoutMap.put( "index", ind );
                    layoutMap.put( "layout_id", layCopyMoveID );
                    layoutMap.put( "visibility", true );
                    layoutMap.put( "view_type", commonCatList.get( catIndex ).get( ind ).getLayoutType() );

                    for (int i = 0; i < productIdList.size(); i++ ){
                        layoutMap.put( "product_id_"+(1+i), productIdList.get( i ) );
                    }
                    //-----------------------------------

                    switch (Integer.parseInt( selectedItemTag )){
                        case StaticValues.ID_COPY:
                            // TODO: Query To copy...
                            // Pass : Map, isMoveReq, Dialog, DocId_CPY, DocId_Remove
                            popupWindow.dismiss();
                            break;
                        case StaticValues.ID_MOVE:
                            // TODO: Query to Move...
                            /////------- 00000000 ----------
                            popupWindow.dismiss();
                            //jkd
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
                            for (int i = 0; i < layoutIdList.size(); i++){
                                temp = layoutIndexList.get( i );
                                copyMoveList.add( temp + "::" + selectedItemTag);
                            }
                            // convert to simple array...
                            popUpContents = new String[copyMoveList.size()];
                            copyMoveList.toArray(popUpContents);
                            popupWindow.dismiss();
                            popupWindowDogs( context, true, productIndex, productId, popUpContents ).showAsDropDown( view );
                            break;
                        case StaticValues.ID_DELETE:
                            // TODO : DELETE

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
        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private void updateDataQuery(Map <String, Object> layoutMap, boolean isMoveRequest, Dialog dialog, String docId_CPY,@Nullable String docIdRemove){

    }


}



/*


 */
