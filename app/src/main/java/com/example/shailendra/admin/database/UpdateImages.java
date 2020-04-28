package com.example.shailendra.admin.database;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.addnewitem.AddNewProductActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class UpdateImages {

    public static String uploadImageLink;
    public static String uploadImageBgColor;

   public static boolean isDeletedFile = false;
   public static boolean isUploadSuccess = false;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    public static void uploadImageOnFirebaseStorage(final Context context, final Dialog dialog, final Uri imageUri,
                                                    final ImageView imageView, final String uploadPath, final String fileName){
        uploadImageLink = "";
        isUploadSuccess = false;
        if (imageUri != null){
            final StorageReference storageRef = storageReference.child( uploadPath + "/" + fileName + ".jpg" );

            Glide.with( context ).asBitmap().load( imageUri ).optionalCenterCrop().into( new ImageViewTarget <Bitmap>( imageView ){
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition <? super Bitmap> transition) {
                    super.onResourceReady( resource, transition );

                    final TextView perText = dialog.findViewById( R.id.process_per_complete_text );
                    perText.setVisibility( View.VISIBLE );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnCompleteListener( new OnCompleteListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                // Query To get Download Link...
                                storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Uri> task) {
                                        if (task.isSuccessful()){
                                            isUploadSuccess = true;
//                                                imageUri = task.getResult();
                                            uploadImageLink = task.getResult().toString();
                                            Glide.with( context ).load( imageUri ).into( imageView );
                                            dialog.dismiss();
                                        }else{
                                            // Failed Query to getDownload Link...
                                            // TODO : Again Request to Get Download Link Or Query to Delete The Image....
                                            deleteImageFromFirebase(context, dialog, uploadPath, fileName);
                                            isUploadSuccess = false;
                                            dialog.dismiss();
                                            showToast( task.getException().getMessage().toString(), context );
                                        }
                                    }
                                } );
                            }else{
                                dialog.dismiss();
                            }
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            showToast( "Failed to upload..! ", context );
                        }
                    } ).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            perText.setText("Uploading " + progress + "% completed");
                        }
                    }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            perText.setText( "Uploading Pause.! \n Check your net connection.!" );
                        }
                    });
                }
                @Override
                protected void setResource(@Nullable Bitmap resource) {
                    // Set Default image...
//                        profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                }
            } );
        }
        else {
            dialog.dismiss();
            showToast("Please Select Image First...!!", context );
        }

    }
    public static void deleteImageFromFirebase(final Context context, final Dialog dialog, String downloadPath, String fileName){
        isDeletedFile = false;
//        dialog.show();
        StorageReference deleteRef = storageReference.child(  downloadPath + "/" + fileName + ".jpg" );
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                isDeletedFile = true;
                uploadImageLink = null;
                // After Delete from Server.. We have to  update on Database...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dialog.dismiss();
                isDeletedFile = false;
                showToast( "Failed..!! "+ exception.getMessage(),context );
            }
        });

    }

    // Update Database...
    private static void updateImageLinkOnDatabase(final Context context, String link, final boolean isUpload){
        // Query To update Image Link...
        FirebaseFirestore.getInstance().collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .update( "user_image", link ).addOnSuccessListener( new OnSuccessListener <Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isUpload){
//                    dialog.dismiss();
                    showToast( "Upload Image Successfully..!!", context );
                }else{
//                    dialog.dismiss();
                    showToast( "Profile Image deleted successfully..!!", context );
                }
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
                showToast( "Failed..!! "+ e.getMessage(), context );
            }
        } );
    }

    private static void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }


    // There are three methods to upload data on storage...

    // 1. Upload from data in memory
    //      https://firebase.google.com/docs/storage/android/upload-files#upload_from_data_in_memory
    private void uploadFromDataInMemory(){

//        StorageReference storage = FirebaseStorage.getInstance().getReference();
        // Create a storage reference from our app
//        StorageReference storageRef = storage.getStorage().getReference();

// Create a reference to "mountains.jpg"
//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
//        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
//        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
//        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        // Get the data from an ImageView as bytes
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
    }

    // 2. Upload from a stream
    //      https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_stream
    private void uploadFromAStream(){
//        InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));

//        uploadTask = mountainsRef.putStream(stream);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
    }

    // 3. Upload from a local file
    //      https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_local_file
    private void uploadFromALocalFile(){
//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
//        uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
    }
    private void uploadPogress(){
        // Observe state change events such as progress, pause, and resume
//        uploadTask.addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                System.out.println("Upload is " + progress + "% done");
//            }
//        }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("Upload is paused");
//            }
//        });
    }



    // User Profile Image Upload...
/**
    private void uploadImageOnFirebaseStorage(){
        // dialog.show();
        if (alreadyAddedImage){
            deleteImageFromFirebase( true );
        }else{
            if (imageUri != null){
                final StorageReference storageRef = storageReference.child( "profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg" );

                Glide.with( getContext() ).asBitmap().load( imageUri ).circleCrop().into( new ImageViewTarget <Bitmap>( catImage ){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition <? super Bitmap> transition) {
                        super.onResourceReady( resource, transition );

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageRef.putBytes(data);
                        uploadTask.addOnCompleteListener( new OnCompleteListener <UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){
                                    // Query To get Download Link...
                                    storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Uri> task) {
                                            if (task.isSuccessful()){
                                                imageUri = task.getResult();
                                                imageLink = task.getResult().toString();
                                                Glide.with( getContext() ).load( imageUri ).into( catImage );
                                                updateImageLinkOnDatabase( imageLink, true );
                                            }else{
                                                // Failed Query to getDownload Link...
                                                // TODO : Again Request to Get Download Link Or Query to Delete The Image....
                                                deleteImageFromFirebase(false);
//                                                dialog.dismiss();
                                                showToast( task.getException().getMessage().toString() );
                                            }
                                        }
                                    } );
                                }else{

                                }
                            }
                        } ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        } );

                    }
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        // Set Default image...
//                        profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                    }
                } );
            }
            else {
                showToast("Please Select Image First...!!");
            }
        }

    }
    private void deleteImageFromFirebase(final boolean isUpdateRequest){
//        dialog.show();
        StorageReference deleteRef = storageReference.child( "profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg" );
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isUpdateRequest){
                    // means  Update Request...
                    alreadyAddedImage = false;
                    uploadImageOnFirebaseStorage();
                }else{
                    alreadyAddedImage = false;
                    imageLink = "";
                    imageUri = null;
//                    profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                    // After Delete from Server.. We have to  upadate on Database...
                    updateImageLinkOnDatabase( imageLink, false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                dialog.dismiss();
                showToast( "Failed..!! "+ exception.getMessage() );
            }
        });

    }

 **/


}
