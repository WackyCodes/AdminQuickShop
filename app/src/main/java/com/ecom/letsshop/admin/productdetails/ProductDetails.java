package com.ecom.letsshop.admin.productdetails;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.R;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.addnewitem.AddNewProductActivity;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class ProductDetails extends AppCompatActivity {

    // --- Product Details Image Layout...
    private ViewPager productImagesViewPager;
    private TabLayout productImagesIndicator;
    private ConstraintLayout productDescriptionLayout;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productCODText;
    private TextView productCODSubText;
    private TextView productStock;
    private TextView productOffPer;

    // --- Product Details Image Layout...
    private TextView productDetailsText;

    private ViewPager productDescriptionViewPager;
    private TabLayout productDescriptionIndicator;

    private String productID;

    private ConstraintLayout activityProductDetailLayout;
    // Dialogs...
    private Dialog dialog;

    // FirebaseFireStore...
    private FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot documentSnapshot;
    private DialogsClass dialogsClass = new DialogsClass(  );

    // Product Specification ...
    private List<ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();
    private String productDescription;
    // Temp Variables...
    private String tShortName;
    private String tOffPer;
    private long tStocks;
    private boolean tCod = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );
        activityProductDetailLayout = findViewById( R.id.activity_product_detail_constLayout );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // TODO : get product ID through Intent ...
        productID = getIntent().getStringExtra( "PRODUCT_ID" );
//        productID = "nSoOrHjzwyYwXfad31Ys";
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        // Clear the ptoductDetailTempList...
//        productDetailTempList.clear();
        // --- Product Details Image Layout...
        productName = findViewById( R.id.product_item_name );
        productPrice = findViewById( R.id.product_item_price );
        productCutPrice = findViewById( R.id.product_item_cut_price );
        productCODText = findViewById( R.id.product_item_cod_text );
        productCODSubText = findViewById( R.id.product_item_cod_sub_text );
        productStock = findViewById( R.id.product_item_stocks );
        productOffPer = findViewById( R.id.product_off_per );

        // --- Product Details Image Layout...
        productDescriptionLayout = findViewById( R.id.product_details_description_ConstLayout );
        productDetailsText = findViewById( R.id.product_details_text );

        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // create a list for testing...
        final List <String> productImageList = new ArrayList <>();

        dialog = dialogsClass.progressDialog( ProductDetails.this );
        dialog.show();
        // ---- Progress Dialog...

        firebaseFirestore = FirebaseFirestore.getInstance();

        // ---------- Product Description code----
        productDescriptionViewPager = findViewById( R.id.product_detail_viewpager );
        productDescriptionIndicator = findViewById( R.id.product_details_indicator );

        // Retrieve details from database...----------------
        if (!productID.isEmpty()){
            firebaseFirestore.collection( "PRODUCTS" ).document( productID )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        documentSnapshot = task.getResult();
                        // --- add Product Images...
                        for (long x = 1; x < (long)documentSnapshot.get( "product_image_num" )+1; x++){
                            productImageList.add( documentSnapshot.get( "product_image_"+x ).toString() );
                        }
                        // set adapter with viewpager...
                        ProductDetailsImagesAdapter productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
                        productImagesViewPager.setAdapter( productDetailsImagesAdapter );
                        // Add Product ID  in productDetailTempList
//                    productDetailTempList.add( 0, productID );
                        //add Main Image in productDetailTempList...
//                    productDetailTempList.add( 1, productImageList.get( 0 ) );
                        // Set other Details of  Product Details Image Layout.
                        // Product Name...
                        productName.setText( documentSnapshot.get( "product_full_name" ).toString() );
                        tShortName = documentSnapshot.get( "product_short_name" ).toString(); // Short Name...
                        //add name in Temp List
//                    productDetailTempList.add( 2, documentSnapshot.get( "product_full_name" ).toString() );
                        // Product Price...
                        productPrice.setText( "Rs. " + documentSnapshot.get( "product_price" ).toString() +"/-" );
                        // add price in  Temp List
//                    productDetailTempList.add( 3, documentSnapshot.get( "product_price" ).toString() );
                        // Product Cut Price...
                        productCutPrice.setText( "Rs. " + documentSnapshot.get( "product_cut_price" ).toString() +"/-" );
                        //add cut price in  Temp List
//                    productDetailTempList.add(4, documentSnapshot.get( "product_cut_price" ).toString() );
                        tOffPer = documentSnapshot.get( "product_off_per" ).toString();
                        productOffPer.setText( tOffPer + "% OFF" );

                        if ((boolean)documentSnapshot.get( "product_cod" )){
                            tCod = true;
                            productCODText.setVisibility( View.VISIBLE );
                            productCODSubText.setVisibility( View.VISIBLE );
                            //add COD in  Temp List
//                        productDetailTempList.add( 5, "COD" );
                        }else{
                            tCod = false;
                            productCODText.setVisibility( View.INVISIBLE );
                            productCODSubText.setVisibility( View.INVISIBLE );
                            //add COD in  Temp List
//                        productDetailTempList.add( 5, "NO_COD" );
                        }
                        if ((long)documentSnapshot.get( "product_stocks" ) > 0){
                            tStocks = (long)documentSnapshot.get( "product_stocks" );
                            productStock.setText( "In Stock (" + tStocks + ")"  );
                            //add COD in  Temp List
//                        productDetailTempList.add( 6, "IN_STOCK" );
                        }else{
                            productStock.setText( "Out of Stock" );
                            tStocks = 0;
                            //add COD in  Temp List
//                        productDetailTempList.add( 6, "OUT_OF_STOCK" );
                        }

                        // Set other Details of  Product Details Image Layout.
                        if ((boolean)documentSnapshot.get( "use_tab_layout" )){
                            // use tab layout...
                            productDescriptionLayout.setVisibility( View.VISIBLE );
                            // TODO : set Description data..
                            productDescription = documentSnapshot.get( "product_description" ).toString() ;
                            // TODO : set Specification data...
                            for (long x = 1; x < (long) documentSnapshot.get( "pro_sp_head_num" )+1; x++){
                                productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 0,
                                        documentSnapshot.get( "pro_sp_head_" + x ).toString() ) );
                                for (long i = 1; i < (long)documentSnapshot.get( "pro_sp_sub_head_"+x+"_num" )+1; i++){
                                    productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 1,
                                            documentSnapshot.get( "pro_sp_sub_head_" + x + i ).toString(), documentSnapshot.get( "pro_sp_sub_head_d_" + x + i ).toString() ) );
                                }
                            }

                            ProductDetailsDescriptionAdaptor productDetailsDescriptionAdaptor = new ProductDetailsDescriptionAdaptor( getSupportFragmentManager()
                                    , productDescriptionIndicator.getTabCount()
                                    , productDescription
                                    , productDetailsSpecificationModelList  );
                            productDescriptionViewPager.setAdapter( productDetailsDescriptionAdaptor );
                            productDetailsDescriptionAdaptor.notifyDataSetChanged();

                        }
                        else{
                            // don't use tabLayout...
                            productDescriptionLayout.setVisibility( View.GONE );
                        }
                        productDetailsText.setText( documentSnapshot.get( "product_details" ).toString() );
                        // check offline wishList Size.. and run DB query to update wishList if size is 0
                        /**  if (currentUser != null){

                         // for Wish list...
                         if (DBquery.myWishList.size() == 0){
                         DBquery.wishListQuery(ProductDetails.this, dialog, false);
                         }
                         // set color of wishList button based on wishList...
                         if (DBquery.myWishList.size() != 0){
                         if (DBquery.myWishList.contains( productID )){
                         ALREADY_ADDED_IN_WISHLIST = true;
                         addToWishListBtn.setSupportImageTintList( getResources().getColorStateList( R.color.colorRed ) );
                         }else{
                         ALREADY_ADDED_IN_WISHLIST = false;
                         }
                         dialog.dismiss();
                         }

                         // for cart list...
                         if (DBquery.myCartCheckList.size() == 0){
                         DBquery.cartListQuery(ProductDetails.this, false, dialog, PRODUCT_DETAILS_ACTIVITY);
                         }
                         if (DBquery.myCartCheckList.size() != 0){
                         for (int i = 0; i < DBquery.myCartCheckList.size(); i++) {
                         if( DBquery.myCartCheckList.get( i ).getProductId().equals( productID )){
                         ALREADY_ADDED_IN_CART = true;
                         break;
                         }else{
                         ALREADY_ADDED_IN_CART = false;
                         }
                         }
                         dialog.dismiss();
                         }

                         }else {
                         dialog.dismiss();
                         } **/

                        dialog.dismiss();
                    }
                    else{
                        String error = task.getException().getMessage();
                        showToast(error);
                        dialog.dismiss();
                    }
                }
            } );
        }
        else{
            showToast("Product ID not found..!");
            dialog.dismiss();
            finish();
        }
        // Retrieve details from database...----------------

        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );
        //----------- Product Images ---

        productDescriptionViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener( productDescriptionIndicator ) );
        productDescriptionIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDescriptionViewPager.setCurrentItem( tab.getPosition() );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

        // ---------- Product Description code----
    }

    // onCreate method End.. ----------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.product_detail_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        switch (id){
            case R.id.menu_change_stocks:
                updateStockOrDetails( 1, String.valueOf( tStocks ) );
                break;
            case R.id.menu_change_mrp:
                String mrp = productCutPrice.getText().toString();
                mrp = mrp.replaceAll( "[^0-9]", "" );
                String selling = productPrice.getText().toString();
                selling = selling.replaceAll( "[^0-9]", "" );
                updatePrice( Integer.parseInt( mrp.trim() ), Integer.parseInt( selling.trim() ), Integer.parseInt( tOffPer ) );
                break;
            case R.id.menu_change_cod:
                updateCod();
                break;
            case R.id.menu_change_name:
                updateNames( productName.getText().toString(), tShortName );
                break;
            case R.id.menu_change_details:
                updateStockOrDetails( 0, productDetailsText.getText().toString() );
                break;
            case R.id.menu_change_full:
                Intent gotoAddProductIntent = new Intent( this, AddNewProductActivity.class );
                gotoAddProductIntent.putExtra( "PRODUCT_ID", productID );
                gotoAddProductIntent.putExtra( "LAY_INDEX", StaticValues.UPDATE_P_LAY_INDEX );
                gotoAddProductIntent.putExtra( "CAT_INDEX", StaticValues.UPDATE_P_CAT_INDEX );
                gotoAddProductIntent.putExtra( "PRODUCT_CAT", StaticValues.UPDATE_PRODUCT_CAT );
                gotoAddProductIntent.putExtra( "UPDATE", true );
                startActivity( gotoAddProductIntent );
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    // Update Data.... Of Product,..------------------------------------------------------------------
    // 1 & 2 Stocks Or Details Update...
    private EditText dialogEditText;
    private void updateStockOrDetails(final int dType, @Nullable String StDetText){
        // dType == 1 : For Stocks...
        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( this );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_single_edit_text );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        final Button okBtn = quantityDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.dialog_cancel_btn );
        final TextView dialogTitle = quantityDialog.findViewById( R.id.dialog_title );
        dialogEditText = quantityDialog.findViewById( R.id.dialog_editText );
        if (dType == 1){
            dialogTitle.setText( "Enter Stock Quantity :" );
            dialogEditText.setInputType( InputType.TYPE_CLASS_NUMBER );
            dialogEditText.setSingleLine();
            dialogEditText.setHint( "Stocks" );
            if (StDetText != null){
                dialogEditText.setText( StDetText );
            }

        }else{
            dialogTitle.setText( "Enter Details :" );
            dialogEditText.setHint( "Details" );
            dialogEditText.setMinLines( 3 );
            dialogEditText.setSingleLine( false );
            if (StDetText != null)
                dialogEditText.setText( StDetText );
            dialogEditText.setInputType( InputType.TYPE_CLASS_TEXT );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialogEditText.setTextAlignment( EditText.TEXT_ALIGNMENT_TEXT_START );
            }
        }
        quantityDialog.show();
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (isNotEmptyEditText( dialogEditText ) && DialogsClass.isInternetConnect( ProductDetails.this )){
                   quantityDialog.dismiss();
                   dialog.show();
                   Map<String, Object> updateMap = new HashMap <>();
                   if (dType == 1){
                       updateMap.put( "product_stocks", Integer.parseInt( dialogEditText.getText().toString() ) );
                       updateOnDatabase( updateMap, dialog );
                   }else{
                       if (! dialogEditText.getText().toString().equals( productDetailsText.getText().toString() )){
                           updateMap.put( "product_details", dialogEditText.getText().toString() );
                           updateOnDatabase( updateMap, dialog );
                           // Update local...
                           productDetailsText.setText( dialogEditText.getText().toString() );
                       }else{
                           dialog.dismiss();
                           showToast( "Update Successfully..!" );
                       }
                   }
               }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();
            }
        } );

    }
    // 3 Price Update...
    private final int MRP_CHANGED = 10;
    private final int SELLING_CHANGED = 11;
    private void updatePrice(int mrpPrice, int sellingPrice, final int perOff){
        /**
         * 1. MRP Update..
         * 2. Selling Update..
         * 3. Discount in Rs. update..
         * 4. Per Discount Update...
         */
        final Dialog upDialog = new Dialog( this );
        upDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );

        upDialog.setContentView( R.layout.dialog_four_edit_text );
        final EditText newProMrpRate = upDialog.findViewById( R.id.dialog_editText_1 ); // MRP EdText
        final EditText newProSellingPrice = upDialog.findViewById( R.id.dialog_editText_2 ); // Selling Price EdText
        final TextView newProRsDiscount = upDialog.findViewById( R.id.dialog_text_1 ); // discount Text in Rs.
        final TextView newProPerDiscount = upDialog.findViewById( R.id.dialog_text_2 ); // discount Text in Per

        upDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        upDialog.setCancelable( false );
        // Common Button...
        final Button okBtn = upDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = upDialog.findViewById( R.id.dialog_cancel_btn );
        upDialog.show();
        newProMrpRate.setText( String.valueOf( mrpPrice ) );
        newProSellingPrice.setText( String.valueOf( sellingPrice ) );
        newProRsDiscount.setText( "Rs. " + (mrpPrice - sellingPrice) + "/-");
        newProPerDiscount.setText( tOffPer + "%");
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( isNotEmptyEditText( newProMrpRate ) && isNotEmptyEditText( newProSellingPrice )&& DialogsClass.isInternetConnect( ProductDetails.this )) {
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProMrpRate.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        // Update Data... :
                        upDialog.dismiss();
                        dialog.show();
                        Map<String, Object> updateMap = new HashMap <>();
                        updateMap.put( "product_cut_price",  newProMrpRate.getText().toString().trim() );
                        updateMap.put( "product_price",  newProSellingPrice.getText().toString().trim() );
                        updateMap.put( "product_off_per", tOffPer );
                        updateOnDatabase( updateMap, dialog );
                        // Set Local...
                        productCutPrice.setText( "Rs. " +  newProMrpRate.getText().toString().trim() +"/-" );
                        productPrice.setText( "Rs. " + newProSellingPrice.getText().toString() +"/-" );
                        productOffPer.setText( tOffPer + "% OFF" );
                    }
                }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDialog.dismiss();
            }
        } );

        newProMrpRate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProMrpRate.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProMrpRate.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }
            }
        } );
        newProSellingPrice.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if ( TextUtils.isEmpty( newProMrpRate.getText().toString().trim() )){
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProSellingPrice.setError( "Selling Price should be less or equal from MRP.!" );
                    }
                }else {
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProSellingPrice.setError( "Selling Price should be less or equal from MRP.!" );
                    }else{
                        // Code to set Value...
                        if (! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() ))
                            setAutoText( SELLING_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }else {
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
        } );
    }
    private void setAutoText(int reqType, EditText mrpEdTxt, EditText sellingEdTxt, TextView offPerEdTxt, TextView offRsEdTxt){
        int mrpValue = Integer.parseInt( mrpEdTxt.getText().toString().trim() );
        int sellValue = Integer.parseInt( sellingEdTxt.getText().toString().trim() );
        int offPerValue;
        int offRsValue;

        // if MRP and Selling Entered by user...
        switch (reqType){
            case SELLING_CHANGED:
                if (isNotEmptyEditText( mrpEdTxt )){
                    offRsValue = mrpValue - sellValue;
                    offRsEdTxt.setText( "Rs. " + String.valueOf( offRsValue ) + "/-");
                    offPerValue = (offRsValue * 100)/mrpValue;
                    tOffPer = String.valueOf( offPerValue );
                    offPerEdTxt.setText( String.valueOf( offPerValue ) + " %" );
                }
                break;
            case MRP_CHANGED:
                if (isNotEmptyEditText( sellingEdTxt )){
                    offRsValue = mrpValue - sellValue;
                    offRsEdTxt.setText( "Rs. " + String.valueOf( offRsValue ) + "/-" );
                    offPerValue = (offRsValue * 100)/mrpValue;
                    tOffPer = String.valueOf( offPerValue );
                    offPerEdTxt.setText( String.valueOf( offPerValue ) + " %" );
                }
                break;
            default:
                break;
        }
    }
    private boolean isNotEmptyEditText(EditText editText){
        if (TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required field..!" );
            return false;
        }else{
            return true;
        }
    }
    // 4 COD Update...
    private void updateCod(){
        final Dialog upDialog = new Dialog( this );
        upDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        upDialog.setContentView( R.layout.dialog_switch_button );
        final RadioGroup dialogRadio = upDialog.findViewById(R.id.dialog_radio_group );
        upDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        upDialog.setCancelable( false );
        // Common Button...
        final Button okBtn = upDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = upDialog.findViewById( R.id.dialog_cancel_btn );
        upDialog.show();
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DialogsClass.isInternetConnect( ProductDetails.this )){
                    upDialog.dismiss();
                    int mode = dialogRadio.getCheckedRadioButtonId();
                    dialog.show();
                    Map<String, Object> updateMap = new HashMap <>();
                    if (mode == upDialog.findViewById( R.id.dialog_yes ).getId()){
                        if ( !tCod ){
                            tCod = true;
                            updateMap.put( "product_cod", true );
                            updateOnDatabase( updateMap, dialog );
                            productCODText.setVisibility( View.VISIBLE );
                            productCODSubText.setVisibility( View.VISIBLE );
                        }else{
                            dialog.dismiss();
                            showToast( "Update Successfully..!" );
                        }
                    }
                    else if (mode == upDialog.findViewById( R.id.dialog_not ).getId()){
                        if (tCod){
                            tCod = false;
                            updateMap.put( "product_cod", false );
                            updateOnDatabase( updateMap, dialog );
                            productCODText.setVisibility( View.GONE );
                            productCODSubText.setVisibility( View.GONE );
                        }else{
                            dialog.dismiss();
                            showToast( "Update Successfully..!" );
                        }
                    }
                }
            }
        } );
        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDialog.dismiss();
            }
        } );

    }
    // 5 Update Names...
    private void updateNames(String fullName, String shortName){

        final Dialog upDialog = new Dialog( this );
        upDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        upDialog.setContentView( R.layout.dialog_double_edit_text );
        upDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        upDialog.setCancelable( false );
        // Common Button...
        final EditText dialogFullName = upDialog.findViewById( R.id.dialog_editText_1 ); // Full Name
        final EditText dialogShortName = upDialog.findViewById( R.id.dialog_editText_2 ); // Short Name
        TextView dialogText1 = upDialog.findViewById( R.id.dialog_title_1 ); // Full Name Text
        TextView dialogText2 = upDialog.findViewById( R.id.dialog_title_2 ); // Short Name Text
        dialogText1.setText( "Enter Full Name :" );
        dialogText2.setText( "Enter Short Name :" );
        dialogFullName.setText( fullName );
        dialogShortName.setText( shortName );
        final Button okBtn = upDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = upDialog.findViewById( R.id.dialog_cancel_btn );
        upDialog.show();
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Update...
                if (isNotEmptyEditText( dialogFullName ) && isNotEmptyEditText( dialogShortName ) && DialogsClass.isInternetConnect( ProductDetails.this )){
                    upDialog.dismiss();
                    dialog.show();
                    // Update..
                    if ( ! dialogFullName.getText().toString().equals( productName.getText().toString() ) || ! dialogShortName.getText().toString().equals( tShortName ) ){
                        Map<String, Object> updateMap = new HashMap <>();
                        updateMap.put( "product_full_name", dialogFullName.getText().toString());
                        updateMap.put( "product_short_name", dialogShortName.getText().toString());
                        updateOnDatabase( updateMap, dialog ); // update on database...
                        productName.setText( dialogFullName.getText().toString() );
                        tShortName = dialogShortName.getText().toString();
                    }else{
                        dialog.dismiss();
                        showToast( "Update Successfully..!" );
                    }
                }
            }
        } );
        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDialog.dismiss();
            }
        } );

    }

    private void updateOnDatabase(Map<String, Object> updateMap, final Dialog dialog){
        firebaseFirestore.collection( "PRODUCTS" ).document( productID ).update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    queryToDownloadProductListData( productID, dialog );
                    showToast( "Update Successfully..!" );
                }else{
                    dialog.dismiss();
                    showToast( "Failed..! Something went wrong.!" );
                }
            }
        } );
    }

    private static void queryToDownloadProductListData( final String productId, final Dialog dialog ){

        final int layoutIndex = StaticValues.UPDATE_P_LAY_INDEX;
        final int catIndex = StaticValues.UPDATE_P_CAT_INDEX;

        FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( productId )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // access the banners from database...
//                    tempHrGridList.add( new HrLayoutItemModel( productId
//                            , task.getResult().get( "product_image_1").toString()
//                            , task.getResult().get( "product_full_name" ).toString()
//                            , task.getResult().get( "product_price" ).toString()
//                            , task.getResult().get( "product_cut_price" ).toString()
//                            , (long) task.getResult().get( "product_stocks" )
//                            , (Boolean) task.getResult().get( "product_cod" ) ) );

                    HrLayoutItemModel hrLayoutItemModel = new HrLayoutItemModel( productId
                            , task.getResult().get( "product_image_1").toString()
                            , task.getResult().get( "product_full_name" ).toString()
                            , task.getResult().get( "product_price" ).toString()
                            , task.getResult().get( "product_cut_price" ).toString()
                            , (long) task.getResult().get( "product_stocks" )
                            , (Boolean) task.getResult().get( "product_cod" ) );

                   int listSize = commonCatList.get(catIndex).get( layoutIndex ).getHrAndGridProductsDetailsList().size();

                   for (int i=0; i < listSize; i++){
                       if (commonCatList.get(catIndex).get( layoutIndex ).getHrAndGridProductsDetailsList().get( i ).getHrProductId().equals( productId )){
                           commonCatList.get(catIndex).get( layoutIndex ).getHrAndGridProductsDetailsList().set( i, hrLayoutItemModel );
                           dialog.dismiss();
                           break;
                       }
                   }
                    dialog.dismiss();
                }
                else{
                    String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                }
            }
        } );
    }



}

