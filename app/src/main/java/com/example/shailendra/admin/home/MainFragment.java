package com.example.shailendra.admin.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.MainActivity;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.catlayouts.BannerAndCatModel;
import com.example.shailendra.admin.database.DBquery;
import com.example.shailendra.admin.database.UpdateImages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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

    DialogsClass dialogsClass = new DialogsClass();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );
        final Dialog dialog = dialogsClass.progressDialog( getContext() );

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

        if (DBquery.homeCategoryIconList.size() == 0){
            dialog.show();
            DBquery.getQueryCategoryIcon(dialog);
        }
        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( getContext() );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        mainRecycler.setLayoutManager( homeCatLayoutManager );
        // Adaptor...
        homeCatAdaptor = new MainAdaptor( DBquery.homeCategoryIconList );
        mainRecycler.setAdapter( homeCatAdaptor );
        homeCatAdaptor.notifyDataSetChanged();

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
                       showToast( "Please fill requred field.!" );
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
                    updateImageLinkOnDatabase( dialog, uploadLink, catTitleText.getText().toString(), DBquery.homeCategoryIconList.size() );
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

        return view;
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

        FirebaseFirestore.getInstance().collection( "CATEGORIES" ).document( catName.toUpperCase() )
                .set( catMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    DBquery.homeCategoryIconList.add( new BannerAndCatModel( link, catName ) );
                    commonCatList.add( new ArrayList <CommonCatModel>() );
//                    DBquery.homeCategoryIconList.add( new BannerAndCatModel( String.valueOf( R.drawable.ic_home_black_24dp ), catName ) );
                    homeCatAdaptor.notifyDataSetChanged();
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

    }

    // Add User Image Process....

    private void showToast(String msg){
        Toast.makeText( getContext(), msg, Toast.LENGTH_SHORT ).show();
    }



}
