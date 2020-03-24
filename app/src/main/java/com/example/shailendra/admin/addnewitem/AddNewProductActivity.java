package com.example.shailendra.admin.addnewitem;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.StaticValues.GALLERY_CODE;
import static com.example.shailendra.admin.StaticValues.READ_EXTERNAL_MEMORY_CODE;

public class AddNewProductActivity extends AppCompatActivity {

    public AddNewProductActivity() {
        // Required empty public constructor
    }

    private final int MRP_CHANGED = 10;
    private final int SELLING_CHANGED = 11;
    private final int D_RS_CHANGED = 12;
    private final int D_PER_CHANGED = 13;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private DialogsClass dialogsClass = new DialogsClass();
    private Dialog dialog;
    private Dialog perDialog;

    //            <!--    Section 1: Add Images..-->
    private LinearLayout secAddImageLayout; // sec_1_add_image_layout
    private RecyclerView newProImageRecycler; // new_pro_images_recycler
    private Spinner newProMainImageSpin; // new_pro_main_image_spinner
    private Button newProUploadImageBtn; // new_prod_upload_images_btn

    public AddImageAdaptor imgAdaptor = new AddImageAdaptor();
    public static List<String> productImageSelectList = new ArrayList <>();
    public static List<UploadImageDataModel> uploadImageDataModelList = new ArrayList <>();

    public ArrayAdapter <String> dataAdapter;

//            <!-- Section 2: Add Information...-->
    private LinearLayout secAddInfoLayout; // sec_2_add_info_layout
    private EditText newProFullName; // new_pro_full_name
    private EditText newProdShortName; // new_pro_short_name
    private EditText newProMrpRate; // new_pro_mrp_rate
    private EditText newProSellingPrice; // new_pro_selling_price
    private TextView newProPerDiscount; // new_pro_per_discount
    private TextView newProRsDiscount; // new_pro_rs_discount
    private EditText newProStockAvailable; // new_pro_stock_available
    private Switch   newProCodSwitch; // new_pro_cod_switch

//            <!-- Section 3: Add Descriptions and Specifications...-->
    private LinearLayout secAddDesSpecifyLayout; // sec_3_add_des_specific_layout
    private Switch   useTabLayoutSwitch; // new_pro_tab_layout_switch_sec_3
    private EditText newProDescription; // new_pro_description
    private RecyclerView newProSpecificationRecycler; // new_pro_specification_recycler

    private AddSpecificationAdaptor addSpecificationAdaptor = new AddSpecificationAdaptor();
    private AddSpecificationFeatureAdaptor addSpecificationFeatureAdaptor;
    // Specification... and feature list...
    private List<AddSpecificationFeatureModel> specificationFeatureModelList;
    private List<AddSpecificationModel> specificationModelList = new ArrayList <>();

//           <!-- Section 4: Add Searching Tags...-->
    private LinearLayout secProDetailAndTagLayout; // sec_4_pro_details_and_tags_layout
    private EditText newProDetails; // new_pro_details
    private EditText newProSearchingTags; // new_pro_searching_tags

//    Submit Button...
    private Button newProSubmitBtn; // new_pro_upload_btn

    //==========
    private int tempVal = 0;
    private int tempVal2 = 0;
    private String tempStrVal;
    private String mainImageLink = null;
    private boolean isUploadImages = false;
    private String uploadProductID;
    private String productCat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_product );
        dialog = dialogsClass.progressDialog( this );
        perDialog = dialogsClass.progressPerDialog( this );

        uploadProductID = getIntent().getStringExtra( "PRODUCT_ID" );
        productCat = getIntent().getStringExtra( "PRODUCT_CAT" );

//            <!--    Section 1: Add Images..-->
        secAddImageLayout = findViewById( R.id.sec_1_add_image_layout );
        newProImageRecycler = findViewById( R.id.new_pro_images_recycler );
        newProMainImageSpin = findViewById( R.id.new_pro_main_image_spinner );
        newProUploadImageBtn = findViewById( R.id.new_prod_upload_images_btn );
//            <!-- Section 2: Add Information...-->
        secAddInfoLayout = findViewById( R.id.sec_2_add_info_layout );
        newProFullName = findViewById( R.id.new_pro_full_name );
        newProdShortName = findViewById( R.id.new_pro_short_name );
        newProMrpRate = findViewById( R.id.new_pro_mrp_rate );
        newProSellingPrice = findViewById( R.id.new_pro_selling_price );
        newProPerDiscount = findViewById( R.id.new_pro_per_discount );
        newProRsDiscount = findViewById( R.id.new_pro_rs_discount );
        newProStockAvailable = findViewById( R.id.new_pro_stock_available );
        newProCodSwitch = findViewById( R.id.new_pro_cod_switch );
//            <!-- Section 3: Add Descriptions and Specifications...-->
        secAddDesSpecifyLayout = findViewById( R.id.sec_3_add_des_specific_layout );
        useTabLayoutSwitch = findViewById( R.id.new_pro_tab_layout_switch_sec_3 );
        newProDescription = findViewById( R.id.new_pro_description );
        newProSpecificationRecycler = findViewById( R.id.new_pro_specification_recycler );
//           <!-- Section 4: Add Searching Tags...-->
        secProDetailAndTagLayout = findViewById( R.id.sec_4_pro_details_and_tags_layout );
        newProDetails = findViewById( R.id.new_pro_details );
        newProSearchingTags = findViewById( R.id.new_pro_searching_tags );
//        submit Button.. Assigning...
        newProSubmitBtn = findViewById( R.id.new_pro_upload_btn );

        //------------------------------------------------------------------------------------
        // Add Image Recycler....
        LinearLayoutManager imgLayoutManager = new LinearLayoutManager( this );
        imgLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        newProImageRecycler.setLayoutManager( imgLayoutManager );
        newProImageRecycler.setAdapter( imgAdaptor );
        imgAdaptor.notifyDataSetChanged();

        // upload Image Btn...
        newProUploadImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productImageSelectList.size() - 1 == uploadImageDataModelList.size()){
                    if (productImageSelectList.size() > 1 ){
                        showToast( "Upload Successfully..! Please add new Image to upload again.!");
                    }else{
                        showToast( "Please add Images first.!!" );
                    }
                }
                else{
                   uploadProductImages();
                }
            }
        } );

        //----------- Spinner...
        productImageSelectList.add( "Select Image" );
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productImageSelectList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newProMainImageSpin.setAdapter(dataAdapter);
        newProMainImageSpin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position > 0 ){
                    showToast( "Image " + position + " Selected..!" );
                    mainImageLink = uploadImageDataModelList.get( ( position -1 )).getImgLink();
                }else{
                    if (productImageSelectList.size() > 1)
                        showToast( "Try Again..!" );
                    else showToast( "Upload Images..!" );
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                if (productImageSelectList.size() == 1 || ! isUploadImages)
                    showToast( "Upload Images first..!" );
            }
        } );

        // ------------  Sec 2 -- Text Watcher...
        priceAndDiscountTextWatcher();

        // ------ Sec - 3 Pro Specification...
        LinearLayoutManager spLayoutManager = new LinearLayoutManager( this );
        spLayoutManager.setOrientation( RecyclerView.VERTICAL );
        spLayoutManager.setSmoothScrollbarEnabled( true );
        newProSpecificationRecycler.setLayoutManager( spLayoutManager );
        newProSpecificationRecycler.setAdapter( addSpecificationAdaptor );
        addSpecificationAdaptor.notifyDataSetChanged();
        // Submit Button Click Action...
        newProSubmitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (isValidFieldsData()){
                    updateProduct(); // TODO: JOB..!
                }
            }
        } );
    }
    // End of onCreate method.....

    private void priceAndDiscountTextWatcher(){
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
//        newProRsDiscount.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                if ( ! TextUtils.isEmpty( newProRsDiscount.getText().toString().trim() )){
//////                    tempVal2 = Integer.parseInt( newProRsDiscount.getText().toString() );
//////                }
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                setAutoText( D_RS_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (tempVal2 !=  Integer.parseInt( newProRsDiscount.getText().toString() )){
//                    setAutoText( D_RS_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//                }
//            }
//        } );
//        newProPerDiscount.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                if ( ! TextUtils.isEmpty( newProPerDiscount.getText().toString().trim() )){
////                    tempVal = Integer.parseInt( newProPerDiscount.getText().toString() );
////                }
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                setAutoText( D_PER_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (tempVal !=  Integer.parseInt( newProPerDiscount.getText().toString() )){
//                    setAutoText( D_PER_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//                }
//            }
//        } );

        // Switch Checked... - Section 3..//
        useTabLayoutSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked ){
                    // Code to Show TabView...
                    secAddDesSpecifyLayout.setVisibility( View.VISIBLE );
                }else{
                    // Hide the TabView...
                    secAddDesSpecifyLayout.setVisibility( View.GONE );
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
                    offRsEdTxt.setText( String.valueOf( offRsValue ) );
                    offPerValue = (offRsValue * 100)/mrpValue;
                    offPerEdTxt.setText( String.valueOf( offPerValue ) );
                }
                break;
            case MRP_CHANGED:
                if (isNotEmptyEditText( sellingEdTxt )){
                    offRsValue = mrpValue - sellValue;
                    tempVal2 = offRsValue;
                    offRsEdTxt.setText( String.valueOf( offRsValue ) );
                    offPerValue = (offRsValue * 100)/mrpValue;
                    tempVal = offPerValue;
                    offPerEdTxt.setText( String.valueOf( offPerValue ) );
                }
                break;
            default:
                break;
        }
    }

    // Validation For section 2...
    private boolean isValidFieldsData(){
        if (uploadImageDataModelList.size() == 0){
            dialog.dismiss();
            dialogsClass.alertDialog( this, null, "Add Product Images..!", null ).show();
            return false;
        } else if( !isUploadImages ){
            dialog.dismiss();
            dialogsClass.alertDialog( this, null, "Upload Images first..!", null ).show();
            return false;
        }
        if (isNotEmptyEditText( newProFullName ) && isNotEmptyEditText( newProdShortName )&&
                isNotEmptyEditText( newProMrpRate )&& isNotEmptyEditText( newProSellingPrice )&&
                isNotEmptyEditText( newProStockAvailable ) && isNotEmptyEditText( newProDetails ) &&
                isNotEmptyEditText( newProSearchingTags ) ){
            if (Integer.parseInt( newProMrpRate.getText().toString()) < Integer.parseInt( newProSellingPrice.getText().toString() )){
                dialog.dismiss();
                dialogsClass.alertDialog( this, null, "Selling price can not greater than MRP rate..!", null ).show();
                return false;
            }
            if (mainImageLink == null){
                dialog.dismiss();
                dialogsClass.alertDialog( this, null, "Select Main Image..!", null ).show();
                return false;
            }
//            uploadImageDataModelList.size()
            if (useTabLayoutSwitch.isChecked()){
                if ( !isNotEmptyEditText( newProDescription ) ){
                    dialog.dismiss();
                    dialogsClass.alertDialog( this, null, "Add Description..!", null ).show();
                    return false;
                } else
                if( specificationModelList.size() == 0 ){
                    dialog.dismiss();
                    dialogsClass.alertDialog( this, null, "Add Specification data..!", null ).show();
                    return false;
                } else {
                    return true;
                }
            }else{
                return true;
            }
        }
        else{
            dialog.dismiss();
            dialogsClass.alertDialog( this, null, "Add Missing Fields..!", null ).show();
            return false;
        }
    }
    // Validation For section 2...

    private boolean isNotEmptyEditText(EditText editText){
        if (TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required field..!" );
            return false;
        }else{
            return true;
        }
    }
    private boolean isValidSize(EditText editText, int size){
        if (editText.getText().toString().length() < size){
            editText.setError( "Require min length : "+ size + "..!");
            return false;
        }else{
            return true;
        }
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
//                    productImgsUriList.add( data.getData() );
//                    productImageLinkList.add( data.getData().toString() );
                    uploadImageDataModelList.add( new UploadImageDataModel( data.getData().toString(), "") );
                    isUploadImages = false;
                    imgAdaptor.notifyDataSetChanged();
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

    //----------
    private String getNewImageName(){
        String newName = "";
        int sizeofList = uploadImageDataModelList.size();
        for (int i = 0; i < sizeofList; i++ ){
            if( ! String.valueOf( i ).equals( uploadImageDataModelList.get( i ).getImgName() ) ){
                newName = String.valueOf( i );
                uploadImageDataModelList.get( i ).setImgName( newName );
                break;
            }else if ( i == sizeofList - 1){
                newName = String.valueOf( sizeofList );
            }
        }

        return newName;
    }
    public void uploadProductImages(){
        // Upload Method...
        perDialog.show();
        final TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
        perText.setVisibility( View.VISIBLE );
        for (int x = productImageSelectList.size() - 1; x < uploadImageDataModelList.size(); x++){
            tempStrVal = getNewImageName();
            productImageSelectList.add( "" );
            StorageReference storageRef = storageReference.child( "products/" + uploadProductID + "/" + tempStrVal + ".jpg" );
            uploadImageAsItAs( Uri.parse( uploadImageDataModelList.get( x ).getImgLink() ), storageRef, tempStrVal, x);
        }
    }
    private void uploadImageAsItAs( Uri fileUri,  final StorageReference storageRef, final String fileName, final int uploadedSize ){

        final UploadTask uploadTask = storageRef.putFile(fileUri);
//        final int num = uploadedSize + 1;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showToast( "Failed to upload..! Something went wrong");
                perDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateListData( storageRef, fileName, uploadedSize);
            }
        }).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                int num = uploadedSize + 1;
                perDialog.show();
                TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                perText.setText( "Image " + (uploadedSize + 1) + " Uploading " + progress + "% completed");
            }
        }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
                perText.setText( "Uploading Pause.! \n Check your net connection.!" );
            }
        });
    }
    private void updateListData( final StorageReference storageReference, final String fileName, final int uploadedSize){

        storageReference.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
            @Override
            public void onComplete(@NonNull Task <Uri> task) {
                if (task.isSuccessful()){
//                            dialog.dismiss();
//                            uploadImageLink = task.getResult().toString();
//                            int num = uploadedSize + 1;
                    uploadImageDataModelList.get( uploadedSize ).setImgLink( task.getResult().toString() );
                    uploadImageDataModelList.get( uploadedSize ).setImgName( fileName );
//                            AddNewProductActivity.productImageLinkList.add( uploadedSize,  task.getResult().toString() );
                    productImageSelectList.set( (uploadedSize + 1), "Image " + (uploadedSize + 1) );
                    dataAdapter.notifyDataSetChanged();
                    perDialog.dismiss();
                    if ( uploadedSize == uploadImageDataModelList.size() - 1 ){
                        showToast( "Upload Images Successfully..!" );
                        isUploadImages = true;
                        imgAdaptor.notifyDataSetChanged();
                    }
                }else{
                    // Failed Query to getDownload Link...
                    perDialog.dismiss();
                    showToast( task.getException().getMessage().toString() );
                }
            }
        } );

    }
    private void removeImageFromList( int position ){

        if ( position < productImageSelectList.size()-1){
            // Meaning We also have to delete from database...
            int tempSize = productImageSelectList.size() - 2;
            productImageSelectList.clear();
            productImageSelectList.add( "Select Image" );
            for (int i = 1; i <= tempSize; i++){
                productImageSelectList.add( "Image "+ i );
                dataAdapter.notifyDataSetChanged();
            }
        }
        uploadImageDataModelList.remove( position );
        imgAdaptor.notifyDataSetChanged();
    }
    //----------

    // --------------- Upload Data on Database.. ---------------------------------------------------
    private void updateProduct(){
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.clear();
        // Primary - fields..
        /** Is_Update :
            1. N - Created Product but Not add details yet.!
            2. Y - Added Details and Update...
            3. V - Visible to search...
            4. I - InVisible : Stop from searching etc..
         */
        updateMap.put( "a_is_update", "Y" );
        updateMap.put( "a_no_of_uses", 0 );
        updateMap.put( "a_product_cat", productCat );
        // Primary - fields..
        updateMap.put( "product_full_name", newProFullName.getText().toString() );
        updateMap.put( "product_short_name", newProdShortName.getText().toString() );
        updateMap.put( "product_price", newProSellingPrice.getText().toString().trim() );
        updateMap.put( "product_cut_price", newProMrpRate.getText().toString().trim() );
        updateMap.put( "product_off_per", newProRsDiscount.getText().toString() );
        updateMap.put( "product_off_rupees", newProPerDiscount.getText().toString() );

        updateMap.put( "product_stocks", Integer.parseInt( newProStockAvailable.getText().toString() ) );
//        updateMap.put( "product_quantity", .getText().toString() );
        updateMap.put( "product_details", newProDetails.getText().toString() );
        if (newProCodSwitch.isChecked()){
            updateMap.put( "product_cod", true );
        }else{
            updateMap.put( "product_cod", false );
        }
        // Images...
        updateMap.put( "product_main_image", mainImageLink );
        updateMap.put( "product_image_num", uploadImageDataModelList.size() );
        for(int i = 0; i < uploadImageDataModelList.size(); i++){
            updateMap.put( "product_image_"+(i+1), uploadImageDataModelList.get( i ).getImgLink() );
        }
        // Searching Tag...
        String tags = newProSearchingTags.getText().toString();
        tags = tags.replaceAll( "[#@&]*"," ");
        String [] sTags = tags.split( "[ ]," );
        List<String> tagList = new ArrayList <>();
//        tagList.clear();
        for (String temp : sTags){
            temp = temp.replaceAll( " ", "" );
            tagList.add( temp );
        }
        updateMap.put( "tags", tagList );

        if (useTabLayoutSwitch.isChecked()){
            updateMap.put( "use_tab_layout", true );
            // Specification and descriptions...
            updateMap.put( "product_description", newProDescription.getText().toString() );
            updateMap.put( "pro_sp_head_num",specificationModelList.size() );
            for (int i = 0; i < specificationModelList.size(); i++){
                updateMap.put( "pro_sp_head_"+(i+1), specificationModelList.get( i ).getSpHeading() );
                // This is for assign data of subList...
                updateMap.put( "pro_sp_sub_head_"+(i+1)+"_num", specificationModelList.get( i ).getSpecificationFeatureModelList().size() );
                for(int j = 0; j < specificationModelList.get( i ).getSpecificationFeatureModelList().size(); j++){
                    updateMap.put( "pro_sp_sub_head_" +(i+1) + "" + (j+1), specificationModelList.get( i ).getSpecificationFeatureModelList().get( j ).getFeatureName() );
                    updateMap.put( "pro_sp_sub_head_d_" +(i+1) + "" + (j+1), specificationModelList.get( i ).getSpecificationFeatureModelList().get( j ).getFeatureDetails() );
                }
            }
        }else{
            updateMap.put( "use_tab_layout", false );
        }

        firebaseFirestore.collection( "PRODUCTS" ).document( uploadProductID ).set( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    showToast( "Product Added Successfully..!" );
                    finish();
                }else{
                    dialog.dismiss();
                    showToast( "Failed to Add Product..!" );
                }
            }
        } );
    }

    // --------------- Upload Data on Database.. ---------------------------------------------------
    // ----------------  Image Adaptor and Model Class ---------------------------------------------
    private class AddImageAdaptor extends RecyclerView.Adapter<AddImageAdaptor.ViewHolder> {
        public AddImageAdaptor() { }

        @NonNull
        @Override
        public AddImageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View imageView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.new_pro_image_item, parent, false );
            return new ViewHolder( imageView );
        }

        @Override
        public void onBindViewHolder(@NonNull AddImageAdaptor.ViewHolder holder, int position) {
            // Data set...
            int size = uploadImageDataModelList.size();
            if (position < size){
                holder.setData( uploadImageDataModelList.get( position ).getImgLink(), position );
            }else if (size < 6){
                holder.addNewImage();
            }

        }

        @Override
        public int getItemCount() {
            if (uploadImageDataModelList.size() < 6){
                return uploadImageDataModelList.size() + 1;
            }else{
                return uploadImageDataModelList.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView itemImage;
            private TextView itemImageNo;
            private ImageButton editImage;
            private LinearLayout addNewImage;
            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                itemImage = itemView.findViewById( R.id.image_view );
                addNewImage = itemView.findViewById( R.id.add_image_layout );
                editImage = itemView.findViewById( R.id.edit_imgBtn );
                itemImageNo = itemView.findViewById( R.id.pro_image_no );
            }
            private void setData(String imgLink, final int position){
                int imgNo = position + 1;
                addNewImage.setVisibility( View.GONE );
                editImage.setVisibility( View.VISIBLE );
                itemImage.setVisibility( View.VISIBLE );
                itemImageNo.setVisibility( View.VISIBLE );
                itemImageNo.setText( String.valueOf( imgNo ) );

                if ( position < productImageSelectList.size()-1){
                    // Use Link...
                    Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( itemImage );
                }else {
                    // Use Uri...
                    Glide.with( itemView.getContext() ).load( Uri.parse( imgLink ) ).into( itemImage );
                }

                editImage.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : Action....
                        removeImageFromList( position );
                    }
                } );
            }
            private void addNewImage(){
                editImage.setVisibility( View.GONE );
                itemImage.setVisibility( View.GONE );
                itemImageNo.setVisibility( View.GONE );
                addNewImage.setVisibility( View.VISIBLE );
                addNewImage.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPermissionGranted( )){
                            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                            galleryIntent.setType( "image/*" );
                            startActivityForResult( galleryIntent, GALLERY_CODE );
                        }
                    }
                } );
            }
        }
    }
    private class UploadImageDataModel {
        String imgLink;
        String imgName;

        public UploadImageDataModel(String imgLink, String imgName) {
            this.imgLink = imgLink;
            this.imgName = imgName;
        }

        public String getImgLink() {
            return imgLink;
        }

        public void setImgLink(String imgLink) {
            this.imgLink = imgLink;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }
    }
    // ----------------  Specification Adaptor and Model Class -------------------------------------
    private class AddSpecificationAdaptor extends RecyclerView.Adapter<AddSpecificationAdaptor.ViewHolder> {

        @NonNull
        @Override
        public AddSpecificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View spLayoutView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.add_new_product_specification_lay, parent, false );
            return new ViewHolder( spLayoutView );
        }
        @Override
        public void onBindViewHolder(@NonNull AddSpecificationAdaptor.ViewHolder holder, int position) {
            holder.setData( position );
        }
        @Override
        public int getItemCount() {
            if (specificationModelList.size() == 0){
                return 1;
            }else
                return specificationModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private EditText sp_headingEdit;
            private TextView sp_headingText;
            private ImageButton sp_edit_btn;
            private ImageButton sp_delete_btn;
            private RecyclerView sp_feature_recycler;
            private EditText sp_feature_name;
            private EditText sp_feature_details;
            private TextView sp_feature_add_btn;
            private TextView sp_feature_done_btn;
            private TextView sp_new_layout_btn;

            private LinearLayout sp_add_done_feature_layout;
            private LinearLayout sp_add_new_layout;
            private LinearLayout sp_layout_top;
            private String title;


            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                sp_headingEdit = itemView.findViewById( R.id.add_new_sp_title_editText );
                sp_headingText = itemView.findViewById( R.id.add_new_sp_title_TextView );
                sp_edit_btn = itemView.findViewById( R.id.add_new_edit_sp_layout );
                sp_delete_btn = itemView.findViewById( R.id.add_new_delete_sp_layout );
                sp_feature_recycler = itemView.findViewById( R.id.add_new_sp_recyclerView );
                sp_feature_name = itemView.findViewById( R.id.add_new_feature_name );
                sp_feature_details = itemView.findViewById( R.id.add_new_feature_details );
                sp_feature_add_btn = itemView.findViewById( R.id.add_new_feature_add_btn );
                sp_feature_done_btn = itemView.findViewById( R.id.add_new_sp_lay_done_btn );
                sp_new_layout_btn = itemView.findViewById( R.id.add_new_sp_lay_btn );

                sp_add_done_feature_layout = itemView.findViewById( R.id.add_new_add_done_layout );
                sp_add_new_layout = itemView.findViewById( R.id.add_new_sp_lay_btn_layout );
                sp_layout_top = itemView.findViewById( R.id.layout_first );
            }

            private void setData( final int position ){
                specificationFeatureModelList = new ArrayList <>();
                if (specificationModelList.size() > 0){
                    // Visible...
                    sp_edit_btn.setVisibility( View.VISIBLE );
                    sp_delete_btn.setVisibility( View.VISIBLE );
                    sp_layout_top.setVisibility( View.VISIBLE );
                    sp_add_new_layout.setVisibility( View.GONE );

                    specificationFeatureModelList = specificationModelList.get( position ).getSpecificationFeatureModelList();

                    addSpecificationFeatureAdaptor = new AddSpecificationFeatureAdaptor( position );

                    title = specificationModelList.get( position ).getSpHeading();
                    if (position != specificationModelList.size() - 1){
                        sp_add_done_feature_layout.setVisibility( View.GONE );
                        sp_headingEdit.setVisibility( View.GONE );
                        sp_headingText.setVisibility( View.VISIBLE );
                        sp_headingText.setText( title );
                    }else {
                        sp_add_done_feature_layout.setVisibility( View.VISIBLE );
                        sp_headingEdit.setVisibility( View.VISIBLE );
                        sp_headingEdit.setText( title );
                        sp_headingText.setVisibility( View.GONE );
                    }

                }else{
                    // visible Bottom and invisible Top...
                    sp_layout_top.setVisibility( View.GONE );
                    sp_add_new_layout.setVisibility( View.VISIBLE );

                    addSpecificationFeatureAdaptor = new AddSpecificationFeatureAdaptor( -1 );
                }
                //------------Recycler View...---------------
                LinearLayoutManager layoutManager = new LinearLayoutManager( itemView.getContext() );
                layoutManager.setOrientation( RecyclerView.VERTICAL );
                sp_feature_recycler.setLayoutManager( layoutManager );
                sp_feature_recycler.setAdapter( addSpecificationFeatureAdaptor );
                addSpecificationFeatureAdaptor.notifyDataSetChanged();
                //------------Recycler View...---------------

                // Click on Add Btn;
                sp_feature_add_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check : isData fill on editText
                        if (isNotEmptyEditText( sp_feature_name ) && isNotEmptyEditText( sp_feature_details ) && isNotEmptyEditText( sp_headingEdit )){
                            // Add Data in list...
//                            specificationFeatureModelList.add( new AddSpecificationFeatureModel( sp_feature_name.getText().toString(),
//                                    sp_feature_details.getText().toString() ) );
                            if (specificationModelList.get( position ).getSpHeading() == null){
                                specificationModelList.get( position ).setSpHeading( title );
                            }
                            specificationModelList.get( position ).getSpecificationFeatureModelList().add(
                                    new AddSpecificationFeatureModel( sp_feature_name.getText().toString(),
                                    sp_feature_details.getText().toString() ) );
                            sp_feature_name.setText( "" );
                            sp_feature_details.setText( "" );
                            addSpecificationFeatureAdaptor.notifyDataSetChanged();
                        }
                    }
                } );
                // Click on Done Btn;
                sp_feature_done_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // hide add_done_layout and visible edit and delete btn...
                        // check is exit data in list...
                        if (isNotEmptyEditText( sp_headingEdit ) && specificationFeatureModelList.size()>0){
                            title = sp_headingEdit.getText().toString();
                            sp_headingEdit.setVisibility( View.GONE );
                            sp_headingText.setVisibility( View.VISIBLE );
                            sp_headingText.setText( title );

                            specificationModelList.get( position ).setSpHeading( title );
                            addSpecificationFeatureAdaptor.notifyDataSetChanged();
                            sp_add_done_feature_layout.setVisibility( View.GONE );
                            //------
                            if (position == specificationModelList.size()-1){
                                sp_add_new_layout.setVisibility( View.VISIBLE );
                            }
                        }else{
                            if (specificationFeatureModelList.size() == 0){
                                showToast( "Add Some feature first..!" );
                            }else{
                                showToast( "Add Heading or Feature Title.!" );
                            }
                        }
                    }
                } );
                // Click on New Layout Btn;
                sp_new_layout_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        specificationModelList.add( new AddSpecificationModel( "", new ArrayList <AddSpecificationFeatureModel>()) );
                        addSpecificationAdaptor.notifyDataSetChanged();
                        sp_layout_top.setVisibility( View.VISIBLE );
                        sp_add_new_layout.setVisibility( View.GONE );
                    }
                } );
                // Click on edit Layout Btn;
                sp_edit_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (specificationModelList.size() > 0){
                            // set Visibility of layout...
                            sp_headingEdit.setVisibility( View.VISIBLE );
                            sp_headingEdit.setText( title );
                            sp_headingText.setVisibility( View.GONE );
                            sp_add_done_feature_layout.setVisibility( View.VISIBLE );
                        }else {
                         showToast( "Add Data before editing..!" );
                        }
                    }
                } );
                // Click on delete Layout Btn;
                sp_delete_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete Layout...
                        if (specificationModelList.size() != 0){
                            specificationModelList.remove( position );
                            addSpecificationAdaptor.notifyDataSetChanged();
                        }else {
                            showToast( "Data not found..!" );
                        }
                    }
                } );
            }
        }
    }
    private class AddSpecificationModel{
        private String spHeading;
        private List<AddSpecificationFeatureModel> specificationFeatureModelList;

        public AddSpecificationModel(String spHeading, List <AddSpecificationFeatureModel> specificationFeatureModelList) {
            this.spHeading = spHeading;
            this.specificationFeatureModelList = specificationFeatureModelList;
        }

        public String getSpHeading() {
            return spHeading;
        }

        public void setSpHeading(String spHeading) {
            this.spHeading = spHeading;
        }

        public List <AddSpecificationFeatureModel> getSpecificationFeatureModelList() {
            return specificationFeatureModelList;
        }

        public void setSpecificationFeatureModelList(List <AddSpecificationFeatureModel> specificationFeatureModelList) {
            this.specificationFeatureModelList = specificationFeatureModelList;
        }
    }
    private class AddSpecificationFeatureAdaptor extends  RecyclerView.Adapter<AddSpecificationFeatureAdaptor.ViewHolder>{
        private int index;
        public AddSpecificationFeatureAdaptor( int index ) {
            this.index = index;
        }
        @NonNull
        @Override
        public AddSpecificationFeatureAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View spFeatureView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_detail_specification_item, parent, false );
            return new ViewHolder( spFeatureView );
        }
        @Override
        public void onBindViewHolder(@NonNull AddSpecificationFeatureAdaptor.ViewHolder holder, int position) {
            if(index >= 0){
                String name = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureName();
                String details = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureDetails();
                holder.setData( name, details, position );
            }
        }
        @Override
        public int getItemCount() {
            if (index >= 0)
                return specificationModelList.get( index ).getSpecificationFeatureModelList().size();
            else
                return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView featureName;
            private TextView featureDetails;
            private ImageButton featureDeleteBtn;
            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                featureName = itemView.findViewById( R.id.feature_name );
                featureDetails = itemView.findViewById( R.id.feature_value );
                featureDeleteBtn = itemView.findViewById( R.id.add_new_pro_sp_feature_delete_btn );
            }
            private void setData(String name, String detail, final int pos){
                featureDeleteBtn.setVisibility( View.VISIBLE );
                featureName.setText( name );
                featureDetails.setText( detail );
                featureDeleteBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        specificationModelList.get( index ).getSpecificationFeatureModelList().remove( pos );
                        addSpecificationFeatureAdaptor.notifyDataSetChanged();
                    }
                } );
            }
        }
    }
    private class AddSpecificationFeatureModel {
        private String featureName;
        private String featureDetails;

        public AddSpecificationFeatureModel(String featureName, String featureDetails) {
            this.featureName = featureName;
            this.featureDetails = featureDetails;
        }

        public String getFeatureName() {
            return featureName;
        }

        public void setFeatureName(String featureName) {
            this.featureName = featureName;
        }

        public String getFeatureDetails() {
            return featureDetails;
        }

        public void setFeatureDetails(String featureDetails) {
            this.featureDetails = featureDetails;
        }
    }
    // ----------------  Specification Adaptor and Model Class -------------------------------------

    //-------------- End--------------------
    // WackyCodes....
}
