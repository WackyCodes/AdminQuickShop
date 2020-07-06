package com.ecom.letsshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecom.letsshop.admin.database.DBquery;
import com.ecom.letsshop.admin.home.AreaCodeAndName;
import com.ecom.letsshop.admin.home.MainFragment;
import com.ecom.letsshop.admin.imagecompresser.ImageCompress;
import com.ecom.letsshop.admin.notifications.NotificationsActivity;
import com.ecom.letsshop.admin.order.NewOrder;
import com.ecom.letsshop.admin.userprofile.AdminMemberList;
import com.ecom.letsshop.admin.userprofile.RegisterActivity;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.letsshop.admin.StaticValues.ADD_MEMBER_FRAGMENT;
import static com.ecom.letsshop.admin.StaticValues.MAIN_FRAGMENT;
import static com.ecom.letsshop.admin.StaticValues.MEMBER_LIST_FRAGMENT;
import static com.ecom.letsshop.admin.StaticValues.ORDER_LIST_FRAGMENT;
import static com.ecom.letsshop.admin.StaticValues.areaCodeAndNameList;
import static com.ecom.letsshop.admin.StaticValues.cityNameList;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.database.DBquery.currentUser;
import static com.ecom.letsshop.admin.database.DBquery.firebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout mainHomeContentFrame;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Toolbar toolbar;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;
    public static TextView badgeNotifyCount;

    Dialog dialog;
    public static int wCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        dialog = new DialogsClass().progressDialog( this );
        mainHomeContentFrame = findViewById( R.id.main_content_FrameLayout );

        toolbar = findViewById( R.id.toolBar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );

//        setFragment( new HomeFragment(), M_HOME_FRAGMENT );
//        drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( R.string.app_name );
        } catch (NullPointerException ignored) {
        }
        // Search Method setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );
        drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        if (StaticValues.adminData.getAdminType().equals( StaticValues.TYPE_DELIVERY_BOY )){
            // For Delivery Boy...
            setFragment( new NewOrder(), ORDER_LIST_FRAGMENT );
            navigationView.getMenu().getItem( 1 ).setChecked( true );
            navigationView.getMenu().getItem( 0 ).setEnabled( false );
            navigationView.getMenu().getItem( 2 ).setEnabled( false );
            navigationView.getMenu().getItem( 3 ).setEnabled( false );
        }else if( StaticValues.adminData.getAdminType().equals( StaticValues.TYPE_PRODUCT_MANAGER )){
            // Product Manager Access.....
            navigationView.getMenu().getItem( 2 ).setEnabled( false );
            navigationView.getMenu().getItem( 3 ).setEnabled( false );
        }else{
            // Founder...
            setFragment( new MainFragment(), MAIN_FRAGMENT );
        }

//        TextView dEmail = drawer.findViewById( R.id.drawer_userEmail );
//        TextView dName =drawer.findViewById( R.id.drawer_UserName );
//        if (StaticValues.adminData.getAdminName() != null)
//            dName.setText( " " + StaticValues.adminData.getAdminName() );
//        if (StaticValues.adminData.getAdminEmail() != null){
//            dEmail.setText( " " + StaticValues.adminData.getAdminEmail() );
//        }

        if ( DBquery.orderModelList.size() == 0){
            DBquery.getOrderListQuery(  );
        }

    }

    @Override
    public void onBackPressed() {
        if (StaticValues.adminData.getAdminType().equals( StaticValues.TYPE_DELIVERY_BOY )){
            super.onBackPressed();
            navigationView.getMenu().getItem( 0 ).setEnabled( false );
            navigationView.getMenu().getItem( 2 ).setEnabled( false );
            navigationView.getMenu().getItem( 3 ).setEnabled( false );
        }else
        switch (wCurrentFragment){
            case MAIN_FRAGMENT:
                super.onBackPressed();
                break;
            case ORDER_LIST_FRAGMENT:
            case MEMBER_LIST_FRAGMENT:
                setBackFragment( new MainFragment(), MAIN_FRAGMENT );
                navigationView.getMenu().getItem( 0 ).setChecked( true );
                break;
            case ADD_MEMBER_FRAGMENT:
                setFragment( new AdminMemberList(), MEMBER_LIST_FRAGMENT );
                navigationView.getMenu().getItem( 2 ).setChecked( true );
                drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (homeCategoryIconList.size() == 0) {
//            dialog.show();
//            DBquery.getQueryCategoryIcon( dialog );
//        }
    }

    // Fragment Transaction...
    public void setFragment(Fragment fragment, int currentFragment) {
        wCurrentFragment = currentFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
        fragmentTransaction.replace( mainHomeContentFrame.getId(), fragment );
        fragmentTransaction.commit();
    }
    public void setBackFragment(Fragment fragment, int currentFragment) {
        wCurrentFragment = currentFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        fragmentTransaction.replace( mainHomeContentFrame.getId(), fragment );
        fragmentTransaction.commit();
    }


    //---------------------- Navigation Actions..--------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (wCurrentFragment == MAIN_FRAGMENT){
            getMenuInflater().inflate( R.menu.main_nav_options,menu);

            // notification badge...
            MenuItem notificationItem = menu.findItem( R.id.menu_notification );
            notificationItem.setActionView( R.layout.badge_notification_layout );
            badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
            if (currentUser != null){
                // Run user Notification Query to update...
                DBquery.updateNotificationQuery( MainActivity.this,false );
            }
            notificationItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent catIntent = new Intent( MainActivity.this, NotificationsActivity.class);
                    startActivity( catIntent );
                }
            } );

        }

        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer( GravityCompat.START );
        int id = item.getItemId();

        if (id == android.R.id.home) {
            navigationView.getMenu().getItem( 0 ).setChecked( true );
            return true;
        } else if (id == R.id.nav_home) {
            setFragment( new MainFragment(), MAIN_FRAGMENT );
            return true;
        } else if (id == R.id.nav_order) {
            setFragment( new NewOrder(), ORDER_LIST_FRAGMENT );
//            Toast.makeText( this, "Please check update Version.!", Toast.LENGTH_SHORT ).show();
            return true;
        } else if (id == R.id.add_admin) {
//            setFragment( new AddAdminMember() );
            setFragment( new AdminMemberList(), MEMBER_LIST_FRAGMENT );
            return true;
        } else if (id == R.id.add_location) {
            showDialogAddLocation();
            return false;
        }else if (id == R.id.menu_help){
            startActivity( new Intent( MainActivity.this, ImageCompress.class ) );
        }
        if (id == R.id.nav_log_out) {
            // index - 5
            if (currentUser != null) {
                // TODO : Show Dialog to logOut..!
                // Sign Out Dialog...
                AlertDialog.Builder signOutDialog = new AlertDialog.Builder( this );
//                            new DialogsClass().alertDialog( MainActivity.this,
//                            "Sign Out Alert.!","Do you want to sign out.?", null );
                signOutDialog.setTitle( "Sign Out Alert.!" );
                signOutDialog.setMessage( "Do you want to sign out.?" );
                signOutDialog.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                          navigationView.getMenu().getItem( 0 ).setChecked( true );
                        dialog.dismiss();
                        firebaseAuth.signOut();
                        currentUser = null;
                        Intent finishIntent = new Intent( MainActivity.this, RegisterActivity.class );
                        startActivity( finishIntent );
                        Toast.makeText( MainActivity.this, "Sign Out successfully..!", Toast.LENGTH_SHORT ).show();
                        finish();
                    }
                } );
                signOutDialog.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } );
                signOutDialog.show();
                return false;
            }
            return true;
        }

        return super.onOptionsItemSelected( item );

    }

    /// //////////////////////////////////////////////////////////////////////
    private void showDialogAddLocation(){

        // ----  Dialog...
        final Dialog dialogOption = new Dialog( this );
        dialogOption.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialogOption.setContentView( R.layout.dialog_options_layout );
        dialogOption.setCancelable( true );
        dialogOption.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        Button addCityBtn = dialogOption.findViewById( R.id.add_new_city );
        Button addAreaBtn = dialogOption.findViewById( R.id.add_new_area );

        addCityBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request To new dialog...
                dialogOption.dismiss();
                addNewCityDialog();
            }
        } );
        addAreaBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request To new dialog...
                dialogOption.dismiss();
                addNewAreaDialog();

            }
        } );
        dialogOption.show();

    }
    private void addNewCityDialog(){
        if (cityNameList.size() == 0){
            // Query to update list...
            dialog.show();
            MainFragment.getCityNameListQuery(dialog, null);
        }
        final Dialog addCityDialog = new Dialog( this );
        addCityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        addCityDialog.setContentView( R.layout.dialog_single_edit_text );
        addCityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        addCityDialog.setCancelable( false );
        final Button okBtn = addCityDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = addCityDialog.findViewById( R.id.dialog_cancel_btn );
        final TextView dialogTitle = addCityDialog.findViewById( R.id.dialog_title );
        final EditText dialogEditText = addCityDialog.findViewById( R.id.dialog_editText );

        dialogEditText.setHint( "Enter City Name" );
        dialogTitle.setText( "Add New City Name" );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check
                dialog.show();
                if (TextUtils.isEmpty( dialogEditText.getText().toString() )){
                    dialog.dismiss();
                    dialogEditText.setError( "Required field...!" );
                }
                else if(isAlreadyExist(dialogEditText.getText().toString())){
                    dialog.dismiss();
                    Toast.makeText( MainActivity.this, "This city Already Exit.!", Toast.LENGTH_SHORT ).show();
                }
                else{
                    // Query to update data...
                    addCityDialog.dismiss();
                    addNewCityOnDatabase(dialog,dialogEditText.getText().toString() );
                }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss...
                addCityDialog.dismiss();
            }
        } );
        addCityDialog.show();

    }
    private String tempCityName = null;
    private void addNewAreaDialog(){
        final Dialog addCityDialog = new Dialog( this );
        addCityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        addCityDialog.setContentView( R.layout.dialog_add_new_area );
        addCityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        addCityDialog.setCancelable( false );
        final Button okBtn = addCityDialog.findViewById( R.id.area_ok_btn );
        final Button CancelBtn = addCityDialog.findViewById( R.id.area_cancel_btn );
        final EditText newAreaName = addCityDialog.findViewById( R.id.new_area_name );
        Spinner citySpinner = addCityDialog.findViewById( R.id.new_city_spinner );

        // Set city code Spinner
        ArrayAdapter <String> cityAdapter = new ArrayAdapter <String>( this, android.R.layout.simple_spinner_item, cityNameList );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter( cityAdapter );
        citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    tempCityName = cityNameList.get( position );
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

        if (cityNameList.size() == 0){
            // Query to update list...
            dialog.show();
            MainFragment.getCityNameListQuery(dialog, cityAdapter);
        }

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                if (tempCityName != null){
                    if ( TextUtils.isEmpty( newAreaName.getText().toString() )){
                        dialog.dismiss();
                        newAreaName.setError( "Required field...!" );
                    }
                    else{
                        addCityDialog.dismiss();
                        String areaCode = StaticValues.getRandomOrderID();
                        addNewAreaOnDataBase( dialog, tempCityName, areaCode, newAreaName.getText().toString() );
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText( MainActivity.this, "Please Select City Name.!", Toast.LENGTH_SHORT ).show();
                }

            }
        } );
        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAreaName.setText( "" );
                addCityDialog.dismiss();
            }
        } );
        addCityDialog.show();

    }
    // Check is Already Existing....
    private boolean isAlreadyExist(String newCity){
        boolean isExist = false;
        // Check...
        for (int i = 0; i < cityNameList.size(); i++){
            if (cityNameList.get( i ).toUpperCase().equals( newCity.toUpperCase() )){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private void addNewCityOnDatabase(final Dialog dialog, final String newCity){

        Map<String, Object> cityMap = new HashMap <>();
        cityMap.put( "index", cityNameList.size() );
        cityMap.put( "city", newCity );
        firebaseFirestore.collection( "ADMIN_PER" ).document( newCity.toUpperCase() )
                .set( cityMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    cityNameList.add( newCity );
                    Toast.makeText( MainActivity.this, "Added Successfully..!", Toast.LENGTH_SHORT ).show();
                }else{
                    dialog.dismiss();
                }
            }
        } );


    }
    private void addNewAreaOnDataBase(final Dialog dialog, @NonNull final String cityId, @NonNull final String newAreaCode, @NonNull final String newAreaName){
        Map<String, Object> areaMap = new HashMap <>();
        areaMap.put( "location_id", newAreaCode );
        areaMap.put( "location_name", newAreaName );
        firebaseFirestore.collection( "ADMIN_PER" ).document( cityId.toUpperCase() )
                .collection( "SUB_LOCATION" ).document( newAreaCode )
                .set( areaMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    if (cityId.toUpperCase().equals( userCityName.toUpperCase() )){
                        areaCodeAndNameList.add( new AreaCodeAndName( newAreaCode, newAreaName ) );
                    }
                    Toast.makeText( MainActivity.this, "Added Successfully..!", Toast.LENGTH_SHORT ).show();
                }else{
                    dialog.dismiss();
                }
            }
        } );
    }

    /////////////////////////////////////////


}
