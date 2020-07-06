package com.ecom.letsshop.admin.addnewitem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;
import com.ecom.letsshop.admin.database.UpdateImages;
import com.ecom.letsshop.admin.home.CommonCatActivity;
import com.ecom.letsshop.admin.home.CommonCatModel;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.StaticValues.ADD_NEW_PRODUCT_ITEM;
import static com.ecom.letsshop.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static com.ecom.letsshop.admin.StaticValues.GALLERY_CODE;
import static com.ecom.letsshop.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static com.ecom.letsshop.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.tempProductAreaCode;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class AddNewLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private DialogsClass dialogsClass = new DialogsClass();
    private Dialog dialog;

    // Add New Banner in Slider....
    private LinearLayout bannerDialogLayoutFrame;
    private ImageView bannerDialogBannerImage;
    private ImageView bannerDialogStripImage;
    private TextView bannerDialogAddImage;
    private EditText bannerDialogColorCode;
    private TextView bannerDialogPickColor;
    private TextView bannerDialogUploadImage;
    private TextView bannerDialogCancelBtn;
    private TextView bannerDialogOkBtn;
    private int bannerDialogType;
    private Uri bannerImageUri = null;
    // Add New Banner in Slider....

    private int catIndex = -1;
    private String categoryTitle;
    private int layoutType;
    private int layoutIndex = -1;
    private String layoutId = null;
    private boolean isTaskIsUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_layout );
        dialog = dialogsClass.progressDialog( this );
        dialog.show();

        // get Value from Intent
        categoryTitle = getIntent().getStringExtra( "CAT_TITLE" );
        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );
        layoutType = getIntent().getIntExtra( "LAY_TYPE", -1 );
        layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1 );
        isTaskIsUpdate = getIntent().getBooleanExtra( "TASK_UPDATE", false );

        // Add New Banner in Slider....
        bannerDialogLayoutFrame = findViewById( R.id.add_new_banner_item_LinearLay );
        bannerDialogBannerImage = findViewById( R.id.banner_image );
        bannerDialogStripImage = findViewById( R.id.strip_image );
        bannerDialogAddImage = findViewById( R.id.change_banner );
        bannerDialogColorCode = findViewById( R.id.banner_bg );
        bannerDialogPickColor = findViewById( R.id.color_pick_text );
        bannerDialogUploadImage = findViewById( R.id.upload_bnner );
        bannerDialogCancelBtn = findViewById( R.id.banner_cancel_txt );
        bannerDialogOkBtn = findViewById( R.id.banner_ok_txt );

        bannerDialogAddImage.setOnClickListener( this );
        bannerDialogPickColor.setOnClickListener( this );
        bannerDialogUploadImage.setOnClickListener( this );
        bannerDialogCancelBtn.setOnClickListener( this );
        bannerDialogOkBtn.setOnClickListener( this );
        // Add New Banner in Slider....

        if (isTaskIsUpdate){
            layoutId = commonCatList.get( catIndex ).get( layoutIndex ).getLayoutID();
        }else{
            layoutId = getLayoutId();
        }

        switch (layoutType){
            case BANNER_SLIDER_LAYOUT_CONTAINER:
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
            case GRID_ITEM_LAYOUT_CONTAINER:
                dialog.dismiss();
                addNewLayout(layoutType);
                break;
            case STRIP_AD_LAYOUT_CONTAINER:
            case BANNER_AD_LAYOUT_CONTAINER:
            case BANNER_SLIDER_CONTAINER_ITEM:
                dialog.dismiss();
                // show the Banner Dialog...
                bannerDialogLayoutFrame.setVisibility( View.VISIBLE );
                bannerDialogType = layoutType;
                if (isTaskIsUpdate ){
                    if ( UpdateImages.uploadImageBgColor != null){
                        bannerDialogColorCode.setText( UpdateImages.uploadImageBgColor.substring( 1 ) );
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (bannerDialogType == STRIP_AD_LAYOUT_CONTAINER){
                                bannerDialogStripImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( UpdateImages.uploadImageBgColor ) ));
                                Glide.with( this ).load( UpdateImages.uploadImageLink )
                                        .apply( new RequestOptions().placeholder( R.drawable.strip_ad_placeholder ) ).into( bannerDialogStripImage );
                            }else{
                                bannerDialogBannerImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( UpdateImages.uploadImageBgColor  ) ));
                                Glide.with( this ).load( UpdateImages.uploadImageLink )
                                        .apply( new RequestOptions().placeholder( R.drawable.banner_placeholder ) ).into( bannerDialogBannerImage );
                            }
                        }
                    }
                }
                else{
                    if (layoutType == STRIP_AD_LAYOUT_CONTAINER){
                        bannerDialogBannerImage.setVisibility( View.GONE );
                        bannerDialogStripImage.setVisibility( View.VISIBLE );
                    }else{
                        bannerDialogStripImage.setVisibility( View.GONE );
                        bannerDialogBannerImage.setVisibility( View.VISIBLE );
                    }
                    UpdateImages.uploadImageLink = null;
                    bannerImageUri = null;
                    bannerDialogColorCode.setText( "" );
                }
                break;
            case ADD_NEW_PRODUCT_ITEM:
                // Show New Product Frame...
                break;
            default:
                    break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (UpdateImages.uploadImageLink != null ){
            showAlertDialogForBanner();
        }else{
            finish(); // Finish this Activity..
        }
    }

    @Override
    public void onClick(View v) {
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
        if (v == bannerDialogAddImage){
            // Add Image..
            if (isPermissionGranted( )){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }
        }
        else if (v == bannerDialogPickColor){
            // Color Picker Dialog..
            bannerDialogColorPicker();
        }
        else if (v == bannerDialogUploadImage){
            if (bannerImageUri != null ){
                Dialog perDialog = dialogsClass.progressPerDialog( this );
                perDialog.show();
                switch (bannerDialogType){
                    case STRIP_AD_LAYOUT_CONTAINER:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogStripImage
                                , "ads/" + categoryTitle
                                , "strip_ad_"+ commonCatList.get( catIndex ).size()  );
                        break;
                    case BANNER_AD_LAYOUT_CONTAINER:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , "ads/" + categoryTitle
                                , "banner_ad_"+ commonCatList.get( catIndex ).size()  );
                        break;
                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , "banners/" + categoryTitle + "/" + layoutIndex
                                , "banner" +  commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().size() );
                        break;
                    default:
                        break;
                }
            }else {
                showToast( "Please select Image First..!" );
            }
        }
        else if (v == bannerDialogCancelBtn){
            if (UpdateImages.uploadImageLink != null ){
                showAlertDialogForBanner();
            } else{
                finish(); // Finish this Activity..
            }
        }
        else if (v == bannerDialogOkBtn){
            if (UpdateImages.uploadImageLink != null && !TextUtils.isEmpty( bannerDialogColorCode.getText().toString()) ){
                // Upload Data on Database...
                addNewLayout( bannerDialogType );
//                finish(); // Finish this Activity..
            }else
            if (UpdateImages.uploadImageLink == null && TextUtils.isEmpty( bannerDialogColorCode.getText().toString()) ){
                // Upload Data on Database...
                showToast( "Upload Image And Pick Background Color.!" );
            } else
            if (UpdateImages.uploadImageLink == null){
                showToast( "Upload Image First..!" );
            }else
            if (TextUtils.isEmpty( bannerDialogColorCode.getText().toString())){
                bannerDialogColorCode.setError( "Required..!" );
                showToast( "Please Pick Color Or Enter Manually..!!" );
            }else {
                showToast( "Something went wrong..!" );
            }
        }
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
    }

    private void addNewLayout(int type){
        if (catIndex != -1){
//            commonCatList.get( catIndex ).add( new CommonCatModel(  ) )
            switch (type){
//                case CAT_ITEM_LAYOUT_CONTAINER:        //-- 0
                case BANNER_SLIDER_LAYOUT_CONTAINER:           //-- 1
                    Map<String, Object> layoutMap = new HashMap <>();
                    layoutMap.put( "index", commonCatList.get( catIndex ).size() );
                    layoutMap.put( "layout_id", layoutId);
                    layoutMap.put( "layout_bg", "#DADADA" );
                    layoutMap.put( "visibility", false );
                    layoutMap.put( "no_of_banners", 0 );
                    layoutMap.put( "view_type", BANNER_SLIDER_LAYOUT_CONTAINER );
                    dialog.show();
                    uploadNewLayoutOnDatabase( layoutMap, BANNER_SLIDER_LAYOUT_CONTAINER, dialog );
                    break;
                case BANNER_SLIDER_CONTAINER_ITEM:
                    Map<String, Object> bSliderItem = new HashMap <>();
                    // We have to add all the list data in this map  And Set this list on the database location...
                    String bannerBgColor = "#"+  bannerDialogColorCode.getText().toString().trim();
                    String bannerImgLink = UpdateImages.uploadImageLink;
                    commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().add( new BannerAndCatModel( bannerImgLink, bannerBgColor ) );

                    final List <BannerAndCatModel> bannerSliderModelList = commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList();
                    bSliderItem.put( "index", layoutIndex );
                    bSliderItem.put( "layout_id", commonCatList.get( catIndex ).get( layoutIndex ).getLayoutID() );
                    bSliderItem.put( "layout_bg", "#DADADA" );
                    bSliderItem.put( "visibility", true );
                    bSliderItem.put( "no_of_banners", bannerSliderModelList.size() );
                    bSliderItem.put( "view_type", BANNER_SLIDER_LAYOUT_CONTAINER );

                    for (int bsInd = 1; bsInd <= bannerSliderModelList.size(); bsInd++){
                        bSliderItem.put( "banner_"+ bsInd, bannerSliderModelList.get( bsInd-1 ).getImageLink() );
                        bSliderItem.put( "banner_"+ bsInd + "_bg", bannerSliderModelList.get( bsInd-1 ).getTitleOrBgColor() );
                    }
                    dialog.show();
                    uploadNewLayoutOnDatabase( bSliderItem, BANNER_SLIDER_CONTAINER_ITEM, dialog );
                    ///
                    break;
                case STRIP_AD_LAYOUT_CONTAINER:         //-- 2
                    Map<String, Object> stripMap = new HashMap <>();
                    stripMap.put( "index", commonCatList.get( catIndex ).size() );
                    stripMap.put( "layout_id", layoutId );
                    stripMap.put( "layout_bg", "#DADADA" );
                    stripMap.put( "visibility", true );
                    stripMap.put( "strip_ad", UpdateImages.uploadImageLink );
                    stripMap.put( "strip_bg", "#" + bannerDialogColorCode.getText().toString().trim() );
                    stripMap.put( "view_type", STRIP_AD_LAYOUT_CONTAINER );
                    dialog.show();
                    uploadNewLayoutOnDatabase( stripMap, STRIP_AD_LAYOUT_CONTAINER, dialog );
                    break;
                case BANNER_AD_LAYOUT_CONTAINER:         //-- 3
                    Map<String, Object> bannerAdMap = new HashMap <>();
                    bannerAdMap.put( "index", commonCatList.get( catIndex ).size() );
                    bannerAdMap.put( "layout_id", layoutId );
                    bannerAdMap.put( "layout_bg", "#DADADA" );
                    bannerAdMap.put( "visibility", true );
                    bannerAdMap.put( "banner_ad",  UpdateImages.uploadImageLink  );
                    bannerAdMap.put( "banner_bg", "#" + bannerDialogColorCode.getText().toString().trim() );
                    bannerAdMap.put( "view_type", BANNER_AD_LAYOUT_CONTAINER );
                    dialog.show();
                    uploadNewLayoutOnDatabase( bannerAdMap, BANNER_AD_LAYOUT_CONTAINER, dialog );
                    break;
                case HORIZONTAL_ITEM_LAYOUT_CONTAINER:     //-- 4
                    addNewHrGridLayout( HORIZONTAL_ITEM_LAYOUT_CONTAINER );
                    break;
                case GRID_ITEM_LAYOUT_CONTAINER:            //-- 5
                    addNewHrGridLayout( GRID_ITEM_LAYOUT_CONTAINER );
                    break;
                default:
                    break;
            }
//            commonCatAdaptor.notifyDataSetChanged();
        }
    }

    private void addNewHrGridLayout(final int view_type){
        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( this );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_single_edit_text );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        final Button okBtn = quantityDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.dialog_cancel_btn );
        final EditText dialogEditText = quantityDialog.findViewById( R.id.dialog_editText );
        final TextView dialogTitle = quantityDialog.findViewById( R.id.dialog_title );
        dialogTitle.setText( "Enter Layout Title :" );
        quantityDialog.show();

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( TextUtils.isEmpty(dialogEditText.getText().toString().trim())) {
                    dialogEditText.setError( "Please Fill Require field..!" );
                }
                else {
                    quantityDialog.dismiss();
                    final Dialog dialog = dialogsClass.progressDialog( AddNewLayoutActivity.this );
                    dialog.show();
                    final String layout_title = dialogEditText.getText().toString();
                    Map<String, Object> layoutMap = new HashMap <>();
                    layoutMap.put( "layout_title", layout_title );
                    layoutMap.put( "no_of_products", 0 );
                    layoutMap.put( "index", commonCatList.get( catIndex ).size() );
                    layoutMap.put( "layout_id", layoutId );
                    layoutMap.put( "layout_bg", "#DADADA" );
                    layoutMap.put( "visibility", false );
                    layoutMap.put( "view_type",view_type );
                    uploadNewLayoutOnDatabase(layoutMap, view_type, dialog);
                }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();
                finish();
            }
        } );

    }
    private void uploadNewLayoutOnDatabase(final Map<String, Object> layoutMap, final int view_type, final Dialog dialog ){
        String catName = categoryTitle;
        // we are set our unique Id... Because we need this id to update data...
        final String documentId = layoutMap.get( "layout_id" ).toString();

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( catName.toUpperCase() )
                    .collection( "LAYOUTS" ).document( documentId ).set( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                switch (view_type){
                                    case BANNER_SLIDER_LAYOUT_CONTAINER:
                                        commonCatList.get( catIndex ).add( new CommonCatModel( view_type, documentId, new ArrayList <BannerAndCatModel>() ) );
                                        break;
                                    case BANNER_SLIDER_CONTAINER_ITEM:
                                        // Notify Data Changed..  of Adaptor...
                                        break;
                                    case STRIP_AD_LAYOUT_CONTAINER:
                                        commonCatList.get( catIndex ).add( new CommonCatModel( view_type, documentId,
                                                layoutMap.get( "strip_ad" ).toString(), layoutMap.get( "strip_bg" ).toString() ) );
                                        break;
                                    case BANNER_AD_LAYOUT_CONTAINER:
                                        commonCatList.get( catIndex ).add( new CommonCatModel( view_type, documentId,
                                                layoutMap.get( "banner_ad" ).toString(), layoutMap.get( "banner_bg" ).toString() ) );
                                        break;
                                    case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                                    case GRID_ITEM_LAYOUT_CONTAINER:
                                        commonCatList.get( catIndex ).add( new CommonCatModel( view_type, documentId,
                                                layoutMap.get( "layout_title" ).toString(), new ArrayList <String>(),
                                                new ArrayList <HrLayoutItemModel>() ) );
                                        break;
                                    default:
                                        break;
                                }
                                CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                                showToast( "Added Successfully..!" );
                                finish();
                            }else{
                                dialog.dismiss();
                                if (view_type == BANNER_SLIDER_CONTAINER_ITEM){
                                    commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().remove(
                                            commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().size()-1 );
                                }
                                showToast( "failed..! Error : " + task.getException().getMessage() );
                            }
                        }
                    } );
        }else{
            dialog.dismiss();
        }

    }

    // Color Picker...Dialog.
    private void bannerDialogColorPicker(){
        new ColorPickerDialog.Builder( this, AlertDialog.THEME_HOLO_DARK )
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setCancelable( false )
                .setPositiveButton(("Confirm"),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                String color = "#" + envelope.getHexCode();
                                bannerDialogColorCode.setText( envelope.getHexCode() );
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    if (bannerDialogType == STRIP_AD_LAYOUT_CONTAINER){
                                        bannerDialogStripImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( color ) ));
                                    }else{
                                        bannerDialogBannerImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( color ) ));
                                    }
                                }
                            }
                        })
                .setNegativeButton(("Cancel"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    }

    // ----------
    private String getLayoutId(){
        String layout = null;
        int sizeOfList = commonCatList.get( catIndex ).size();
        List<String> tempIdList = new ArrayList <>();

        for (int i = 0; i < sizeOfList; i++){
            tempIdList.add( commonCatList.get( catIndex ).get( i ).getLayoutID() ); //  "layout_id", "layout_"+
        }

        for (int i = 0; i <= sizeOfList; i++){
            layout = "layout_"+ i;
            if ( !tempIdList.contains( layout )){
//                layoutId = layout;
                break;
            }
        }
        return layout;
    }

    private void showAlertDialogForBanner(){
        // Show Warning dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Do You Want to Cancel adding Layout.?" );
        builder.setCancelable( false );
        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
                dialog.show();
                switch (bannerDialogType){
                    case STRIP_AD_LAYOUT_CONTAINER:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                , "ads/" + categoryTitle
                                , "strip_ad_"+ commonCatList.get( catIndex ).size()  );
                        break;
                    case BANNER_AD_LAYOUT_CONTAINER:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                , "ads/" + categoryTitle
                                , "banner_ad_"+ commonCatList.get( catIndex ).size()  );
                        break;
                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                ,"banners/" + categoryTitle + "/" + layoutIndex
                                , "banner" +  commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().size()  );
                        break;
                    default:
                        break;
                }// Finish this Activity..
            }
        } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
            }
        } ).show();
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    // Get Result of Image...
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    bannerImageUri = data.getData();
                    UpdateImages.uploadImageLink = null;
                    if (bannerDialogType == STRIP_AD_LAYOUT_CONTAINER){
                        Glide.with( this ).load( bannerImageUri ).into( bannerDialogStripImage );
                    }else{
                        Glide.with( this ).load( bannerImageUri ).into( bannerDialogBannerImage );
                    }
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
    }
    // Permission...
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                // TODO : YES
                return true;
            }else{
                // TODO : Requiest For Permission..!
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
                return false;
            }
        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] !=  PackageManager.PERMISSION_GRANTED){
                showToast( "Permission granted..!" );
            }else{
                showToast( "Permission Denied..! Please Grant Permission first.!!");
            }
        }
    }


}

