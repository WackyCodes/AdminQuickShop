package com.ecom.letsshop.admin.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecom.letsshop.admin.CheckInternetConnection;
import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.database.DBquery;
import com.ecom.letsshop.admin.database.UpdateImages;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.StaticValues.areaCodeAndNameList;
import static com.ecom.letsshop.admin.StaticValues.areaNameList;
import static com.ecom.letsshop.admin.StaticValues.cityNameList;
import static com.ecom.letsshop.admin.StaticValues.tempProductAreaCode;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.database.DBquery.homeCategoryIconList;

public class MainFragment extends Fragment {

    private static final int GALLERY_CODE = 121;
    private static final int READ_EXTERNAL_MEMORY_CODE = 122;
    private static final int UPDATE_CODE = 123;
    private static final int ADD_CODE = 124;
    private static final int UPLOAD_CODE = 125;
    private static final int UPDATE_EMAIL = 1;
    private static final int UPDATE_PASS = 2;
    // Image Variables...
    private Uri imageUri = null;

    // Getting Reference of CheckInternetConnection
    CheckInternetConnection checkInternetCON = new CheckInternetConnection();


    public static MainAdaptor homeCatAdaptor;
    public static RecyclerView mainRecycler;
//    public static List<BannerAndCatModel> catList = new ArrayList <>();

    public static List<List<CommonCatModel>> commonCatList = new ArrayList <>();

    // Add New Category Item Layout....
    public static RelativeLayout addNewCatItemRelativeLayout;
    private ImageView catImage;
    private EditText catTitleText;
    private TextView catOKBtn;
    private TextView catCancelBtn;
    private TextView changeCatIcon;
    private TextView uploadCatIcon;

    private FrameLayout mainFrameLayout;
    DialogsClass dialogsClass = new DialogsClass();

    public static SwipeRefreshLayout homeSwipeRefreshLayout;
    public static TextView locationText;

    private static FakeAdaptor fakeAdaptor = new FakeAdaptor();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );
        final Dialog dialog = dialogsClass.progressDialog( getContext() );

        mainFrameLayout = view.findViewById( R.id.main_fragment_frameLayout );
        locationText = view.findViewById( R.id.location_textView );

        if (StaticValues.userAreaName != null){
            locationText.setText( StaticValues.userCityName + ", " + StaticValues.userAreaName );
        }else{
            locationText.setText( StaticValues.userCityName );
        }

        locationText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Select Location..
                getLocationAddress(getContext());
            }
        } );


        // Refresh Progress...
        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...

        // Initiation...
        mainRecycler = view.findViewById( R.id.main_recycler );
        addNewCatItemRelativeLayout = view.findViewById( R.id.add_new_cat_iem_layout );

        // Assign The New Category variables...
        catImage = view.findViewById( R.id.category_icon );
        catTitleText = view.findViewById( R.id.category_name );
        catOKBtn = view.findViewById( R.id.category_ok_txt );
        catCancelBtn = view.findViewById( R.id.category_cancel_txt );
        changeCatIcon = view.findViewById( R.id.change_cat_icon );
        uploadCatIcon = view.findViewById( R.id.upload_cat_icon );
        changeCatIcon.setVisibility( View.GONE );
        uploadCatIcon.setVisibility( View.GONE );

        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( getContext() );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        mainRecycler.setLayoutManager( homeCatLayoutManager );

        if (homeCategoryIconList.size() == 0){
            dialog.show();
            DBquery.getQueryCategoryIcon(dialog, mainRecycler);
        }else{
            // Adaptor...
            if (homeCatAdaptor == null){
                homeCatAdaptor = new MainAdaptor( homeCategoryIconList );
            }
            mainRecycler.setAdapter( homeCatAdaptor );
            homeCatAdaptor.notifyDataSetChanged();
        }

        ///////////////////////////////////////////////////////////
        // Code to add new product item....
        catImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileImage();
                // Code to add Icon image...
                changeCatIcon.setVisibility( View.VISIBLE );
                uploadCatIcon.setVisibility( View.VISIBLE );
            }
        } );
        changeCatIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Icon...
                uploadCatIcon.setEnabled( true );
                addProfileImage();
            }
        } );
        uploadCatIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload task...
                if (imageUri != null && ! TextUtils.isEmpty( catTitleText.getText().toString()) ){
                    Dialog perDialog = dialogsClass.progressPerDialog( getContext() );
                    perDialog.show();
//                    String uploadPath = "cat_icon/name_of_image.jpg";
                    uploadCatIcon.setEnabled( false );
                    UpdateImages.uploadImageOnFirebaseStorage( getContext(), perDialog, imageUri, catImage,
                            "cat_icon",catTitleText.getText().toString() );
                }else {
                    if (imageUri == null ){
                        showToast( "Please select Image first.!" );
                    }else{
                       showToast( "Please fill required field.!" );
                    }
                }
            }
        } );
        catOKBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the action perform methods...
                String uploadLink = UpdateImages.uploadImageLink;
                if ( ( uploadLink != null ) && ! TextUtils.isEmpty( catTitleText.getText().toString() )){
                    // Update on database...
                    dialog.show();
                    updateImageLinkOnDatabase( dialog, uploadLink, catTitleText.getText().toString(), homeCategoryIconList.size() );
                }else{
                    // Show Error...
                    if (TextUtils.isEmpty( catTitleText.getText().toString() )){
                        catTitleText.setError( "Please fill information.!" );
                    }
                    showToast( "Please upload image or fill required information first." );
                }
            }
        } );
        catCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition...
                String uploadLink = UpdateImages.uploadImageLink;
                if ( uploadLink != null ){
                    Dialog dialog = dialogsClass.progressDialog( getContext() );
                    dialog.show();
                    UpdateImages.deleteImageFromFirebase( getContext(), dialog, "cat_icon", catTitleText.getText().toString());
                }
                addNewCatItemRelativeLayout.setVisibility( View.GONE );
                mainRecycler.setVisibility( View.VISIBLE );
                changeCatIcon.setVisibility( View.GONE );
                uploadCatIcon.setVisibility( View.GONE );
                // clear catch...
                UpdateImages.uploadImageLink = null;
                imageUri = null;
                catImage.setImageResource( R.drawable.ic_add_black_24dp );
                catTitleText.setText( "" );
            }
        } );
        ////////////////////////////////////////////////////////////////

        // ----= Refresh Layout... check is Null.?
        if (homeSwipeRefreshLayout != null)
            homeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    homeSwipeRefreshLayout.setRefreshing( true );

                    if (!checkInternetCON.checkInternet( getContext() )){
                        homeCategoryIconList.clear();
                        commonCatList.clear();
                        mainRecycler.setAdapter( fakeAdaptor );
                        dialog.show();
                        DBquery.getQueryCategoryIcon(dialog, mainRecycler);
                    }else{
                        MainFragment.homeSwipeRefreshLayout.setRefreshing( false );
                    }

                }
            });
        // ----= Refresh Layout...

        return view;
    }

    @Override
    public void onDestroyView() {
        mainFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( mainFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }


    // Add User Image Process....
    private void  addProfileImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                getActivity().requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
            }
        }else{
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                showToast( "Permission Denied..! Please Grant Permission first.!!");
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == getActivity().RESULT_OK){
                if (data != null){
                    imageUri = data.getData();
                     Glide.with( this ).load( imageUri ).into( catImage );
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
    }

    private void updateImageLinkOnDatabase(final Dialog dialog, final String link, final String catName, int index){
        // Query To update Image Link...
        Map<String, Object> catMap = new HashMap <>();
        catMap.put( "index", index);
        catMap.put( "icon", link);
        catMap.put( "categoryName", catName);
        catMap.put( "catNameValue", catName);

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( catName.toUpperCase() )
                    .set( catMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
                @Override
                public void onComplete(@NonNull Task <Void> task) {
                    if (task.isSuccessful()){
                        homeCategoryIconList.add( new BannerAndCatModel( link, catName ) );
                        commonCatList.add( new ArrayList <CommonCatModel>() );
//                    DBquery.homeCategoryIconList.add( new BannerAndCatModel( String.valueOf( R.drawable.ic_home_black_24dp ), catName ) );
                        if ( homeCatAdaptor!= null){
                            homeCatAdaptor.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                        showToast( "Added Successfully..!!" );
                        addNewCatItemRelativeLayout.setVisibility( View.GONE );
                        mainRecycler.setVisibility( View.VISIBLE );
                        // clear catch...
                        UpdateImages.uploadImageLink = null;
                        imageUri = null;
                        catImage.setImageResource( R.drawable.ic_add_black_24dp );
                        catTitleText.setText( "" );
                    }
                    else{
                        dialog.dismiss();
                        showToast( "Failed..!! "+ task.getException().getMessage());
                        // clear catch...
                        UpdateImages.uploadImageLink = null;
                        imageUri = null;
                        catImage.setImageResource( R.drawable.ic_add_black_24dp );
                        catTitleText.setText( "" );
                    }
                    }
            } );
        }else{
            dialog.dismiss();
        }


    }

    // Add User Image Process....

    private void showToast(String msg){
        Toast.makeText( getContext(), msg, Toast.LENGTH_SHORT ).show();
    }

    ////////////////////////////////////////////////////
    // ---------------- Location Address................
    private static String tempCityName = null;
    private static String tempAreaCode = null;
    private static String tempAreaName = null;
    private static ArrayAdapter <String> areaAdapter;

    private static void getLocationAddress(final Context context){
        final Dialog dialog = new DialogsClass().progressDialog( context );

        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( context );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_select_location );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        Button okBtn = quantityDialog.findViewById( R.id.location_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.location_cancel_btn );
        final Spinner citySpinner = quantityDialog.findViewById( R.id.city_spinner );
        final Spinner areaSpinner = quantityDialog.findViewById( R.id.area_spinner );

        // Set Area Code spinner...
        areaAdapter = new ArrayAdapter <String>(context,
                android.R.layout.simple_spinner_item, areaNameList );
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);
        areaSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    tempAreaName = areaNameList.get( position );
                    tempAreaCode = areaCodeAndNameList.get( position ).getAreaCode();
                }
                else
                    tempAreaCode = null;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );


        // Set city code Spinner
        ArrayAdapter <String> cityAdapter = new ArrayAdapter <String>( context, android.R.layout.simple_spinner_item, cityNameList );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter( cityAdapter );
        citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    tempCityName = cityNameList.get( position );
                    if ( ( areaNameList.size() == 0) || (tempCityName != StaticValues.userCityName ) ){
                        dialog.show();
                        getAreaListQuery(dialog, areaAdapter, tempCityName);
                    }
                }
                else
                    tempCityName = null;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tempCityName != null && tempAreaCode != null && tempAreaCode != null){
                    // Accept...
                    StaticValues.userCityName = tempCityName;
                    StaticValues.tempProductAreaCode = tempAreaCode;
                    StaticValues.userAreaName = tempAreaName;
                    // : Refresh... the layout.!
                    quantityDialog.dismiss();

                    //  : Refresh home Page...
                    homeCategoryIconList.clear();
                    commonCatList.clear();
                    mainRecycler.setAdapter( fakeAdaptor );
                    dialog.show();
                    DBquery.getQueryCategoryIcon(dialog, mainRecycler);

                }else if (tempCityName == null){
                    Toast.makeText( context, "Please select City.!", Toast.LENGTH_SHORT ).show();
                }else{
                    Toast.makeText( context, "Please Select Your Area..!", Toast.LENGTH_SHORT ).show();
                }

            }
        } );
        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityDialog.dismiss();
            }
        } );
        quantityDialog.show();

        // Request query to get city List...
        if (cityNameList.size() == 0 && StaticValues.adminData.getAdminType().toUpperCase().equals( "A" )){
            dialog.show();
            getCityNameListQuery( dialog, cityAdapter );
        }else if (cityNameList.size() == 0 && StaticValues.userCityName != null){
            cityNameList.add( "Select City" );
            cityNameList.add( StaticValues.userCityName );
            cityAdapter.notifyDataSetChanged();
        }

    }

    public static void getCityNameListQuery(final Dialog dialog, @Nullable final ArrayAdapter <String> arrayAdapter) {

        // City Request..
        cityNameList.clear();
        cityNameList.add( "Select City" );

        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        cityNameList.add( documentSnapshot.get( "city" ).toString() );
                        if (arrayAdapter != null){
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    //showToast( task.getException().getMessage() );
                }
            }
        } );
    }

    private static void getAreaListQuery(final Dialog dialog, @Nullable final ArrayAdapter <String> arrayAdapter, String cityName){
//        // Area Request...
        areaNameList.clear();
        areaNameList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                areaNameList.add( documentSnapshot.get( "location_name" ).toString() );
                                areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                        , documentSnapshot.get( "location_name" ).toString()  ) );

                                if (arrayAdapter != null){
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            dialog.dismiss();

                        }else{
                            dialog.dismiss();

                        }
                    }
                } );

    }






}
