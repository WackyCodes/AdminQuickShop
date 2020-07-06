package com.ecom.letsshop.admin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class GetDataFromMemory extends Fragment{

    private static final int GALLERY_CODE = 121;
    private static final int READ_EXTERNAL_MEMORY_CODE = 122;
    private static final int UPDATE_CODE = 123;
    private static final int ADD_CODE = 124;
    private static final int UPLOAD_CODE = 125;
    private static final int UPDATE_EMAIL = 1;
    private static final int UPDATE_PASS = 2;

    Context context;

    public GetDataFromMemory(Context context) {
        this.context = context;
    }


    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( context.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
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

    // Add User Image Process....
    private void  addProfileImage(){
        if (isPermissionGranted( )){
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }else{
            // Nothing...
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult( requestCode, resultCode, data );
//        if (requestCode == GALLERY_CODE ){
//            if (resultCode == getActivity().RESULT_OK){
//                if (data != null){
////                    imageUri = data.getData();
////                    Glide.with( this ).load( imageUri ).into( catImage );
//                }else{
//                    showToast(  "Image not Found..!" );
//                }
//            }
//        }
//    }

    private void showToast(String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

}
