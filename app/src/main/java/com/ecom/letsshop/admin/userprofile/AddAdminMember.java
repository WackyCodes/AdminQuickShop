package com.ecom.letsshop.admin.userprofile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.MainActivity;
import com.ecom.letsshop.admin.R;
import com.ecom.letsshop.admin.home.AreaCodeAndName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.ecom.letsshop.admin.StaticValues.MEMBER_LIST_FRAGMENT;
import static com.ecom.letsshop.admin.userprofile.RegisterActivity.cityList;

public class AddAdminMember extends Fragment {

    private FrameLayout parentFrameLayout;

    public AddAdminMember() {
        // Required empty public constructor
    }

    private Spinner memberCitySpinner;
    private Spinner perSpinner;
    private Spinner areaSpinner;
    private LinearLayout areaSelectLayout;
    private EditText memberName;
    private EditText memberMobile;
    private EditText memberEmail;
    private Button memberDoneBtn;
    private TextView backBtn;

    private String newMemberCityName = null;
    private String newMemberAreaCode = null;
    private String newMemberPermission = null;
    private ArrayAdapter <String> dataAdapter;
    private ArrayAdapter <String> areaAdaptor;
    private Dialog dialog;

    private static List <String> areaList = new ArrayList <>();
    private List<AreaCodeAndName> areaCodeAndNameList = new ArrayList <>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_add_admin_member, container, false );

        parentFrameLayout = view.findViewById( R.id.addMemberFrame );
        dialog = new DialogsClass().progressDialog( view.getContext() );

        memberName = view.findViewById( R.id.add_member_name );
        memberMobile = view.findViewById( R.id.add_member_mobile );
        memberEmail = view.findViewById( R.id.add_member_email );
        memberDoneBtn = view.findViewById( R.id.add_member_done_btn );
        backBtn = view.findViewById( R.id.add_member_back_txt );
        memberCitySpinner = view.findViewById( R.id.add_member_city_spinner );
        perSpinner = view.findViewById( R.id.type_spinner );
        areaSelectLayout = view.findViewById( R.id.area_spinner_layout );
        areaSpinner = view.findViewById( R.id.area_spinner );

        areaSelectLayout.setVisibility( View.GONE );

        if (cityList.size() <= 1){
            getCityNameList();
        }
        //---------------- City Name Adaptor and Spinner...
        dataAdapter = new ArrayAdapter <String>(view.getContext(),
                android.R.layout.simple_spinner_item, cityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberCitySpinner.setAdapter(dataAdapter);
        memberCitySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    newMemberCityName = cityList.get( position );
                    if (!newMemberPermission.equals( "A" ) && areaList.size() == 0){
                        //  : Get List....
                        dialog.show();
                        getAreaListQuery(dialog, newMemberCityName );
                    }
                }else
                    newMemberCityName = null;
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );
        // --------  Permission Type... Spinner...
        ArrayAdapter<CharSequence> adminTypeAdaptor = ArrayAdapter.createFromResource( view.getContext(),
                R.array.admin_permission, android.R.layout.simple_spinner_item);
        adminTypeAdaptor.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        perSpinner.setAdapter( adminTypeAdaptor );
        perSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        newMemberPermission = null;
                        areaSelectLayout.setVisibility( View.GONE );
                        break;
                    case 1:
                        newMemberPermission = "A";
                        areaSelectLayout.setVisibility( View.GONE );
                        break;
                    case 2:
                        newMemberPermission = "B";
                        areaSelectLayout.setVisibility( View.VISIBLE );
                        break;
                    case 3:
                        newMemberPermission = "C";
                        areaSelectLayout.setVisibility( View.VISIBLE );
                        break;
                    default:
                        newMemberPermission = null;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        } );
        // --------  Area Select ... Spinner...
        areaAdaptor = new ArrayAdapter <String>(view.getContext(),
                android.R.layout.simple_spinner_item, areaList);
        areaAdaptor.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        areaSpinner.setAdapter( areaAdaptor );
        areaSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if (position != 0 && areaCodeAndNameList.size()>1){
                    newMemberAreaCode = areaCodeAndNameList.get( position ).getAreaCode();
                }else{
                    newMemberAreaCode = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        } );

        // --------  Permission Type... Spinner...
        memberDoneBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidDetails() && isEmailValid( memberEmail ) && newMemberCityName != null && newMemberPermission != null){
                    // update data on database...
                    if (!newMemberPermission.equals( "A" )){
                        if (newMemberAreaCode != null){
                            dialog.show();
                            addMemberOnDatabase( memberName.getText().toString(), memberMobile.getText().toString(), memberEmail.getText().toString() );
                        }else{
                            Toast.makeText( getContext(), "Select Area Name", Toast.LENGTH_SHORT ).show();
                        }
                    }
                    else{
                        dialog.show();
                        addMemberOnDatabase( memberName.getText().toString(), memberMobile.getText().toString(), memberEmail.getText().toString() );
                    }
                }else{
                    Toast.makeText( getContext(), "Select City and permission.!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new AdminMemberList(), MEMBER_LIST_FRAGMENT );
                MainActivity.navigationView.getMenu().getItem( 0 ).setChecked( true );
                MainActivity.drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
            }
        } );
        return view;

    }

    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment(Fragment fragment, int currentFragment){
        MainActivity.wCurrentFragment = currentFragment;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private void addMemberOnDatabase( String name, String mobile, String email ){

        // Create a Map ... to store our data on firebase...
        long regId = Long.valueOf( mobile );
        Map <String, Object> userData = new HashMap <>();
        userData.put( "name", name );
        userData.put( "index", 1 );
        userData.put( "mobile", mobile );
        userData.put( "email", email );
        userData.put( "type", newMemberPermission );
        userData.put( "id", regId );
        userData.put( "auth_id", "" );
        userData.put( "add_pin", "" );
        userData.put( "address", "" );
        userData.put( "permission", true );
        userData.put( "photo", "" );
        userData.put( "area_city", newMemberCityName );
        if ( !newMemberPermission.equals( "A" )){
            userData.put( "area_code", newMemberAreaCode );
        }else{
            userData.put( "area_code", "" );
        }

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection( "ADMIN_PER" )
                .document( newMemberCityName.toUpperCase() )
                .collection( "PERMISSION" );

        collectionReference.document( mobile ).set( userData ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                    newMemberCityName = null;
                    newMemberPermission = null;
                    memberEmail.setText( "" );
                    memberName.setText( "" );
                    memberMobile.setText( "" );
                    Toast.makeText( getContext(), "Added Member Successfully.!", Toast.LENGTH_SHORT ).show();
                }else{
                    Toast.makeText( getContext(), "Failed.! Something went wrong..!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }

    public void getCityNameList( ) {
        cityList.clear();
        cityList.add( "Your City" );

        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).
                orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        cityList.add( documentSnapshot.get( "city" ).toString() );
                        dataAdapter.notifyDataSetChanged();
                    }

                } else {
                    //showToast( task.getException().getMessage() );
                }
            }
        } );
    }
    public void getAreaListQuery(final Dialog dialog, final String cityName ){
//        // Area Request...
        areaList.clear();
        areaList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                areaList.add( documentSnapshot.get( "location_name" ).toString() );
                                areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                        , documentSnapshot.get( "location_name" ).toString()  ) );
                                areaAdaptor.notifyDataSetChanged();
                            }
                            dialog.dismiss();

                        }else{
                            dialog.dismiss();

                        }
                    }
                } );

    }

    //--- checkValidation
    private boolean isValidDetails(){

        String userName = memberName.getText().toString();
        String userPhone = memberMobile.getText().toString().trim();

        if (TextUtils.isEmpty( userName )) {
            memberName.setError( "Please Enter Your Name..!" );
            return false;
        }else if(TextUtils.isEmpty( userPhone )){
            memberMobile.setError( "Required..!" );
            return false;
        }
        else  if (!TextUtils.isEmpty( userPhone )){
            if (userPhone.length() < 10){
                memberMobile.setError( "Please Enter Correct Mobile..!" );
                return false;
            }
        }

        return true;
    }
    private boolean isEmailValid( EditText wReference){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }
        return true;
    }


}
