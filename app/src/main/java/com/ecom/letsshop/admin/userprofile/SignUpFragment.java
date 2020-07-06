package com.ecom.letsshop.admin.userprofile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecom.letsshop.admin.CheckInternetConnection;
import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.R;
import com.ecom.letsshop.admin.StaticValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.ecom.letsshop.admin.database.DBquery.currentUser;


public class SignUpFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    //---------
    private TextView alreadyHaveAccount;
    private ProgressDialog progressDialog;

    private EditText signUpUserName;
    private TextView signUpUserEmail;
    private TextView signUpUserPhone;
    private EditText signUpUserPass1;
    private EditText signUpUserPass2;

    private Button signUpBtn;
    private ImageView closeSignUpFrameBtn;
    //---------
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_sign_up, container, false );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog( getActivity() );
        parentFrameLayout = view.findViewById( R.id.sign_up_frameLayout);

        alreadyHaveAccount = view.findViewById( R.id.sign_up_UserAlready );

        signUpUserName = view.findViewById( R.id.sign_up_UserName_rt );
        signUpUserEmail = view.findViewById( R.id.sign_up_UserEmail_rt );
        signUpUserPhone = view.findViewById( R.id.sign_up_UserPhone_rt );
        signUpUserPass1 = view.findViewById( R.id.sign_up_UserPass1_rt );
        signUpUserPass2 = view.findViewById( R.id.sign_up_UserPass2_rt );
        signUpBtn = view.findViewById( R.id.sign_up_UserRegisterBtn );
        closeSignUpFrameBtn = view.findViewById( R.id.close_sign_up_btn );

        signUpUserEmail.setText( StaticValues.adminData.getAdminEmail() );
        signUpUserPhone.setText( StaticValues.adminData.getAdminMobile() );

//        if (disableCloseSignFormButton){
//            closeSignUpFrameBtn.setVisibility( View.GONE );
//        }else{
//            closeSignUpFrameBtn.setVisibility( View.VISIBLE );
//        }

        closeSignUpFrameBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity( new Intent( getActivity(), MainActivity.class ) );
//                getActivity().finish();
            }
        } );

        // Sign Up Button Click Listner... and Query for Sign Up...
        signUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( isValidDetails( ) && w_isInternetConnect() ){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable( false );
                    progressDialog.show();
                    // : Q01 Call the signUp Process or Method.
                    firebaseAuth.createUserWithEmailAndPassword( signUpUserEmail.getText().toString(),
                            signUpUserPass1.getText().toString() ).addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            // TODO : Check if Task is success...
                            if (task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                StaticValues.adminData.setAdminAuthId( currentUser.getUid() );
                                // Create a Map ... to store our data on firebase...
                                Map<String, Object> userData = new HashMap <>();
                                userData.put( "auth_id", firebaseAuth.getCurrentUser().getUid() );
                                userData.put( "name", signUpUserName.getText().toString().trim() );

                                CollectionReference collectionReference = firebaseFirestore.collection( "ADMIN_PER" )
                                        .document( StaticValues.userCityName.toUpperCase() )
                                        .collection( "PERMISSION" );

                                collectionReference.document( StaticValues.adminData.getAdminMobile() ).update( userData )
                                        .addOnCompleteListener( new OnCompleteListener <Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Void> task) {
                                        // if come from first activity (launching activity...)
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()){
//                                            if(StaticValues.adminData.getAdminType().equals( "C" )){ // For Delivery Boy
//                                                // TODO: User have to show order list... and success delivery Notification...
//
//                                                progressDialog.dismiss();
//                                            }
//                                            startActivity( new Intent( getActivity(), MainActivity.class ) );
//                                            getActivity().finish();
//                                            showToast( "Sign Up Successfully..!" );
                                            progressDialog.dismiss();
                                            new DialogsClass().alertDialog( getContext(), "Please Open Again.!",
                                                    "You have successfully registered. Please Open again App to interact.!",
                                                    null ).setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    getActivity().finish();
                                                }
                                            } ).show();

                                        }else{
                                            showToast( "Something Went wrong..! Error : "+ task.getException().getMessage() );
                                        }
                                    }
                                } );
                                progressDialog.dismiss();
                                // end if ... Task success of Register user...
                            }else{
                                String error = task.getException().getMessage();
                                showToast(error);
                                progressDialog.dismiss();
                            }

                        }
                    } );
                }
            }
        } );

        // If Already have any account...
        alreadyHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignInFragment() );
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
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
//        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    //--- checkValidation
    private boolean isValidDetails(){

        String userName = signUpUserName.getText().toString();
        String userPhone = signUpUserPhone.getText().toString().trim();
        String userPass1 = signUpUserPass1.getText().toString().trim();
        String userPass2 = signUpUserPass2.getText().toString().trim();

        if (TextUtils.isEmpty( userName )){
            signUpUserName.setError( "Please Enter Your Name..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass1 )){
            signUpUserPass1.setError( "Please Enter Password..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass2 )){
            signUpUserPass2.setError( "Please Enter Password Again..!" );
            return false;
        }else
        if (!userPass1.equals( userPass2 )){
            signUpUserPass1.setError( "Password Not Matched..!" );
            signUpUserPass2.setError( "Password Not Matched..!" );
            return false;
        }
        else  if (!TextUtils.isEmpty( userPhone )){
            if (userPhone.length() < 10){
                signUpUserPhone.setError( "Please Enter Correct Mobile..!" );
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

    private boolean w_isInternetConnect() {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( getActivity() )) {
            return false;
        } else {
            return true;
        }

    }
    // Toast message show method...
    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        // or
//        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

    }


}
