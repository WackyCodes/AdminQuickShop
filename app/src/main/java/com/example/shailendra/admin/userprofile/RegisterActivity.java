package com.example.shailendra.admin.userprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shailendra.admin.CheckInternetConnection;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.MainActivity;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.home.AreaCodeAndName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static com.example.shailendra.admin.StaticValues.STORAGE_PERM;
import static com.example.shailendra.admin.StaticValues.areaCodeAndNameList;
import static com.example.shailendra.admin.StaticValues.areaNameList;
import static com.example.shailendra.admin.StaticValues.userCityName;
import static com.example.shailendra.admin.database.DBquery.currentUser;

public class RegisterActivity extends AppCompatActivity {

    public static int setFragmentRequest = -1;
    public static int comeFromActivity = -1;
    private FrameLayout parentFrameLayout;
    private static FirebaseFirestore firebaseFirestore;

    // Check User Layout...
    private LinearLayout checkUserLayout;
    private EditText checkMobileEditT;
    private Button checkContinueBtn;
    private Spinner citySpinner;

    public static List <String> cityList = new ArrayList <>();
    private ArrayAdapter <String> dataAdapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        parentFrameLayout = findViewById( R.id.register_frameLayout);
        dialog = new DialogsClass().progressDialog( this );

        firebaseFirestore = FirebaseFirestore.getInstance();
        if(isInternetConnect()){
            firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        boolean isAllowed = (boolean)task.getResult().get( StaticValues.APP_VERSION );
                        if ( isAllowed ){
                            askSTORAGE_PERMISSION();
                        }else{
                            showDeniedDialog(null, null, true);
                        }

                    }else {
                        finish();
                    }
                }
            } );
        }

        // Check User Assign...
        checkUserLayout = findViewById( R.id.regi_check_user_LinearLayout );
        checkMobileEditT = findViewById( R.id.mobile_editText );
        checkContinueBtn = findViewById( R.id.continue_btn );
        citySpinner = findViewById( R.id.city_spinner );

        checkUserLayout.setVisibility( View.GONE );
        parentFrameLayout.setVisibility( View.VISIBLE );

        // OnCheck Continue Btn Click listener....
        checkContinueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call A function To precess...
                if (isValidMobile(checkMobileEditT) && userCityName != null){
                    dialog.show();
                    checkUserExist( userCityName.toUpperCase().trim(), checkMobileEditT.getText().toString().trim(), dialog, false );
                }else{
                    if (userCityName == null){
                        showToast( "Please Select City Name.!" );
                    }else showToast( "Something Went Wrong.!" );
                }
            }
        } );

        //---------------- City Name Adaptor and Spinner...
        if (cityList.size() <= 1){
            getCityNameList();
        }
        dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, cityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(dataAdapter);
        citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0)
                    userCityName = cityList.get( position );
                else
                    userCityName = null;
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );

    }

    private void checkCurrentUser(){
        // Check crrUser...
        if ( currentUser != null ){
            // Check permission....
            parentFrameLayout.setVisibility( View.VISIBLE );
            checkUserLayout.setVisibility( View.GONE );

            // TODO: Check Permission First And Then Move Ahead...
            //---------------------------------
            File cityFile = new File(getApplicationContext()
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() + "/city/"), "city.txt");

            File docPath = new File(getApplicationContext()
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() + "/documentId/" ), "documentId.txt");

            if (cityFile.exists() && docPath.exists()){
                // Read Data from File...
                dialog.show();
                checkUserExist( readFileFromLocal( cityFile ).trim(), readFileFromLocal( docPath ).trim(), dialog, true );

            }else{
                showToast( "local Data has been deleted..!" );
                getCityNameList();
            }

            //---------------------------------
//            startActivity( new Intent( this, MainActivity.class ) );
//            finish();
        }else{
            // DO: When user not sign in...
            // Set Visible to select city and Mobile
            // Query To get City Name...
            getCityNameList();

        }
    }

    private void showDeniedDialog(@Nullable String title, @Nullable String detail, final boolean isFinished ){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( this );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        Button okBtn = permissionDialog.findViewById( R.id.per_ok_button );
        TextView perTitle = permissionDialog.findViewById( R.id.per_text_head );
        TextView perDetails = permissionDialog.findViewById( R.id.per_text_des );
        if (title != null)
            perTitle.setText( title );
        if (detail != null)
            perDetails.setText( detail );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();
                // TODO : Delete the current user Account..
//                if (currentUser != null){
//                    startActivity( new Intent(RegisterActivity.this, DeleteUserActivity.class) );
//                    finish();
//                }else{
//                    finish();
//                }

                // Finish the activity,,,
                if (isFinished) {
                    finish();
                }
            }
        } );
        permissionDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
//            SignInFragment.disableCloseSignFormButton = false;
        }
        return super.onKeyDown( keyCode, event );
    }
    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        fragmentTransaction.add( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private boolean isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( this )) {
            return false;
        } else {
            return true;
        }

    }
    private boolean isValidMobile(EditText wRef){
        String userPhone = wRef.getText().toString().trim();
        if (TextUtils.isEmpty( userPhone )){
            wRef.setError( "Please Enter Mobile..!" );
            return false;
        }else if (userPhone.length() < 10){
            wRef.setError( "Please Enter Correct Mobile..!" );
            return false;
        }
        return true;
    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    // Query to Load City Name....
    public void getCityNameList( ) {
        cityList.clear();
        cityList.add( "Select City" );

        firebaseFirestore.collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        cityList.add( documentSnapshot.get( "city" ).toString() );
                        dataAdapter.notifyDataSetChanged();
                    }
                    // $ Changes...
                    parentFrameLayout.setVisibility( View.GONE );
                    checkUserLayout.setVisibility( View.VISIBLE );

                } else {
                    //showToast( task.getException().getMessage() );
                }
            }
        } );
    }

    // Check User Is Exist...
    public void checkUserExist(@NonNull final String cityName, @NonNull final String documentId
            , final Dialog dialog, final boolean isActiveUser){

//        CollectionReference collectionReference = firebaseFirestore.collection( "ADMIN_PER" ).document( cityTemp.toUpperCase() )
//                .collection( "PERMISSION" );

        firebaseFirestore.collection( "ADMIN_PER" ).document( cityName.toUpperCase() )
                .collection( "PERMISSION" ).document( documentId ).get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task){
                if (task.isSuccessful()){
                    // Get Data...
                    if ( task.getResult().get( "permission" ) != null){
                        dialog.dismiss();
                        Boolean adminPermission = task.getResult().getBoolean( "permission" );
                        if (adminPermission.booleanValue()){

                            String type = task.getResult().get( "type" ).toString(); // Get User Type... Admin Position..

                            String email = task.getResult().get( "email" ).toString();
                            String mobile = task.getResult().get( "mobile" ).toString();
                            if ( task.getResult().get( "id" ) != null){ /// ? Error : NullValue.!
                                long id = (long)task.getResult().get( "id" );
                                StaticValues.adminData.setAdminId( String.valueOf( id ) );
                            }

//                                String areaCode = task.getResult().get( "area_code" ).toString();

                            StaticValues.adminData.setAdminEmail( email );
                            StaticValues.adminData.setAdminPermission( adminPermission.booleanValue() );
                            StaticValues.adminData.setAdminType( type );
                            StaticValues.adminData.setAdminMobile( mobile );
                            StaticValues.adminData.setAdminCityName( cityName );

                            if (isActiveUser){
                                // code to MainActivity....
                                StaticValues.adminData.setAdminType( type );

                                if (type.equals( StaticValues.TYPE_FOUNDER )){
                                    // Load All Area Code... To that certain city... And Set first as default...
                                    StaticValues.tempProductAreaCode = null;
                                    getAreaListQuery(dialog,  cityName);

                                }else{
                                    // Load Area Code for user...
                                    String areaCode = task.getResult().get( "area_code" ).toString();
                                    StaticValues.adminData.setAdminAreaCode( areaCode );
                                    StaticValues.tempProductAreaCode = areaCode;
                                    userCityName = cityName;
                                    startActivity( new Intent( RegisterActivity.this, MainActivity.class ) );
                                    RegisterActivity.this.finish();
                                }
                            }
                            else{

                                String areaCode = task.getResult().get( "area_code" ).toString();
                                StaticValues.adminData.setAdminAreaCode( areaCode );
                                StaticValues.tempProductAreaCode = areaCode;
                                // Write Data in Local...
                                writeFileInLocal( "city", cityName );
                                writeFileInLocal( "documentId", documentId );

                                // Get All Area code for new user...
                                getAreaListQuery(dialog,  cityName);

                                // TODO : Set Fragment...
                                checkUserLayout.setVisibility( View.GONE );
                                parentFrameLayout.setVisibility( View.VISIBLE );
                                setFragment( new SignInFragment() );
                            }
                        }else{
                            // Denied Dialog
                            showDeniedDialog("Permission denied.!"
                                    , "Sorry, You Don't have permission to use this App. Please contact to your Root Admin.!"
                                    , true);
                        }
                    }else{
                        // Show alert Msg to have not permission by Root admin...
                        showDeniedDialog("Not Registered...!"
                                , "Sorry, This Mobile number is not found on server.! Please reselect your city name or contact to your root admin to add you."
                                , false);
                        dialog.dismiss();
                    }
                }else{
                    dialog.dismiss();
                    showToast( "Mobile Not Registered..! Error : " + task.getException().getMessage() );
                }
            }
        } );


    }

    // Write a local file in local file...
    private void writeFileInLocal(String fileName, String textMsg){
        try {
//            FileOutputStream fileOS = openFileOutput( fileName, MODE_PRIVATE );
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOS );
//            outputStreamWriter.write( textMsg );
//            outputStreamWriter.close();
            File folder1 = new File(getApplicationContext()
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName);
            folder1.mkdirs();
            File pdfFile = new File(folder1, fileName + ".txt");
//            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream( pdfFile );
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream );
            outputStreamWriter.write( textMsg );
            outputStreamWriter.close();

            if (folder1.exists()){
                showToast( "Created file.!" );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFileFromLocal(File filePath){
        String msg = "";
        try {

//            File filePath = new File(getApplicationContext()
//                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName + "/"+ fileName + ".txt");

            FileInputStream fileIS = new FileInputStream( filePath );

//            FileInputStream fileIS = openFileInput( fileName );
            InputStreamReader inputStreamReader = new InputStreamReader( fileIS );
            char[] inputBuffer = new char[100];
            int charRead;
            while(( charRead = inputStreamReader.read( inputBuffer )) > 0){
                String readString = String.copyValueOf( inputBuffer, 0, charRead );
                msg += readString;
            }
            inputStreamReader.close();

        }catch (Exception e){
            showToast( e.getMessage() );
        }finally{
            return msg;
        }

    }

    ///////////////////////////////////  TEMP  /////////////////////////////

    public void askSTORAGE_PERMISSION(){
        if(ContextCompat.checkSelfPermission( RegisterActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){

            checkCurrentUser();

            // Jump to Main Activity or User registration Activity...
//            new Handler( ).postDelayed( new Runnable() {
//                @Override
//                public void run() {
//                    startActivity( new Intent(WelcomeActivity.this, RegistrationActivity.class ) );
//                    finish();
//                }
//            },home_time );

        }else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( RegisterActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERM );
                        }
                    } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    requestStoragePermission();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERM );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERM){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                showToast( "Permission is GRANTED..." );

                checkCurrentUser();
//                new Handler( ).postDelayed( new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity( new Intent(RegisterActivity.this, RegistrationActivity.class ) );
//                        finish();
//                    }
//                },home_time );
            }
            else{
                showToast( "Permission DENIED!" );
                requestStoragePermission();
                finish();
            }
        }
    }

    //---------------------------------
    public void getAreaListQuery(final Dialog dialog, final String cityName ){
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

                                if (StaticValues.tempProductAreaCode == null){
                                    userCityName = cityName;
                                    StaticValues.tempProductAreaCode = documentSnapshot.get( "location_id" ).toString();
                                }

                                if (currentUser != null){
                                    startActivity( new Intent( RegisterActivity.this, MainActivity.class ) );
                                    RegisterActivity.this.finish();
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




