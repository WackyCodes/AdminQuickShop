package com.ecom.letsshop.admin.imagecompresser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecom.letsshop.admin.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static com.ecom.letsshop.admin.StaticValues.GALLERY_CODE;
import static com.ecom.letsshop.admin.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.getPickImageChooserIntent;

public class ImageCompress extends AppCompatActivity {

    // Layouts...
    private LinearLayout layoutOne;
    private LinearLayout layoutTwo;

    // 1.
    private Button addImageBtn;
    // 2.
    private CropImageView cropImageView;
    private Button dialogAddButton;

    Button compressedBtn;
    ImageView croppedImage;

    // 4.
    private ImageView compressedImage;

    Drawable drawable;
    Bitmap bitmap1, bitmap2;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;

    // Cropping Image...

    private Uri imageUri;

    private TextView sizeBefore;
    private TextView sizeAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_image_compress );

        // Layout...
        layoutOne = findViewById( R.id.layout_one );
        layoutTwo = findViewById( R.id.layout_two );

        layoutTwo.setVisibility( View.INVISIBLE );

        // 1.
        addImageBtn = findViewById( R.id.addNewImage );

        // 2.
        cropImageView = findViewById( R.id.cropImageView );
        dialogAddButton = findViewById( R.id.dialog_add_image_btn );

        bytearrayoutputstream = new ByteArrayOutputStream();

        compressedBtn = (Button)findViewById(R.id.compressBtn);

        croppedImage = (ImageView)findViewById(R.id.croppedImage);

        // 4.
        compressedImage = findViewById( R.id.compressedImage );
        // 5.
        sizeBefore = findViewById( R.id.sizeBeforeText );
        sizeAfter = findViewById( R.id.sizeAfterText );


        compressedBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                compressedImage.setImageResource( R.drawable.square_placeholder );
//                drawable = getResources().getDrawable(R.drawable.welcome_image);
//
//                bitmap1 = ((BitmapDrawable)drawable).getBitmap();

                 bitmap1 = ((BitmapDrawable)croppedImage.getDrawable()).getBitmap();

                bitmap1.compress( Bitmap.CompressFormat.JPEG,35,bytearrayoutputstream);

                BYTE = bytearrayoutputstream.toByteArray();

                bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                compressedImage.setImageBitmap(bitmap2);

                Toast.makeText( ImageCompress.this, "Image Compressed Successfully", Toast.LENGTH_LONG).show();

                sizeBefore.setText( "Size Before : "+ getImageSize( croppedImage ) +"KB" );
                sizeAfter.setText( "Size After : "+ getImageSize( compressedImage ) +"KB" );


            }
        });


        // 4..
//        cropImageView.setImageUriAsync(uri);
// or (prefer using uri for performance and better user experience)
//        cropImageView.setImageBitmap(bitmap);

        //5..
        // subscribe to async event using cropImageView.setOnCropImageCompleteListener(listener)
       // cropImageView.getCroppedImageAsync();
// or
       // Bitmap cropped = cropImageView.getCroppedImage();



        // Step 1.
        addImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileImage();

            }
        } );


    }


    //----------------------------------------------------------------------------------------------
    // Add User Image Process....
    private void  addProfileImage(){
        imageUri = null;
        // --------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ImageCompress.this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                String[] mimeTypes = {"image/jpeg", "image/png"};
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                ImageCompress.this.requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
            }
        }else{
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            String[] mimeTypes = {"image/jpeg", "image/png"};
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Get Image from external storage...
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    imageUri = data.getData();
                    startCropImageActivity(imageUri);
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                Glide.with( this ).load( resultUri ).into( croppedImage );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast( error.getMessage() );
            }
        }else {
            showToast( " Error, Request Code : " + requestCode );
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                String[] mimeTypes = {"image/jpeg", "image/png"};
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }else{
                showToast( "Permission Denied..! Please Grant Permission first.!!" );
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    private void showToast(String s) {
        Toast.makeText( this, s, Toast.LENGTH_SHORT ).show();
    }


    private long getImageSize(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long sizeOfImage = imageInByte.length/1024;
        return sizeOfImage;
    }

}
