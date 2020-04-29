package com.example.shailendra.admin.userprofile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.admin.CheckInternetConnection;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.MainActivity;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

import static com.example.shailendra.admin.StaticValues.SIGN_IN_FRAGMENT;
import static com.example.shailendra.admin.StaticValues.SIGN_UP_FRAGMENT;
import static com.example.shailendra.admin.database.DBquery.currentUser;


public class SignInFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    //---------
    private TextView dontHaveAccount;
    private TextView signInForgetPassword;
    private TextView signInEmail;
    private EditText signInPassword;
    private Button signInBtn;
    private ImageView closeSignInFrom;
    //---------

//    public static boolean disableCloseSignFormButton = false;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false );
        firebaseAuth = FirebaseAuth.getInstance();

        parentFrameLayout = view.findViewById( R.id.sign_in_frameLayout);
        progressDialog = new ProgressDialog( getActivity() );

        dontHaveAccount = view.findViewById( R.id.sign_in_dont_have_account );
        signInForgetPassword = view.findViewById( R.id.sign_in_forget_password );
        signInEmail = view.findViewById( R.id.sign_in_email );
        signInPassword = view.findViewById( R.id.sign_in_password );
        signInBtn = view.findViewById( R.id.sign_in_btn );
        closeSignInFrom = view.findViewById( R.id.close_sign_in_btn );

        // Disable Close button
//        if (disableCloseSignFormButton){
//            closeSignInFrom.setVisibility( View.GONE );
//        }else{
//            closeSignInFrom.setVisibility( View.VISIBLE );
//        }

        closeSignInFrom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity( new Intent( getActivity(), MainActivity.class ) );
//                getActivity().finish();
            }
        } );

        dontHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignUpFragment() );
            }
        } );

        signInEmail.setText( StaticValues.adminData.getAdminEmail() );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_Mobile = StaticValues.adminData.getAdminEmail();
                final String password = signInPassword.getText().toString().trim();
                userLogIn(email_Mobile, password);
            }
        } );

        signInForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setFragment( new ForgetPasswordFragment() );
            }
        } );

        return view;

    }

    @Override
    public void onDestroyView() {
//        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
//        mContainer.removeAllViews();
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private void userLogIn(String email_Mobile, String password){
        if ( isEmailValid( null, password ) && w_isInternetConnect()){
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable( false );
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword( email_Mobile, password )
                    .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            if (task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                StaticValues.adminData.setAdminAuthId( currentUser.getUid() );
                                // if come from first activity (launching activity...)
                                startActivity( new Intent( getActivity(), MainActivity.class ) );
                                getActivity().finish();
                                progressDialog.dismiss();
//                                disableCloseSignFormButton = false;
                            }else{
//                                Toast.makeText(  getActivity(),"Error : " + task.getException().getMessage(),Toast.LENGTH_SHORT ).show();
                                 task.getException().getCause();
                                progressDialog.dismiss();
                                 new DialogsClass().alertDialog( getActivity(), "Login Failed.!",
                                        "Your Password not matched or account is not created yet.! \n"
                                         + "Please Sign Up if you haven't sign up yet..!", null).show();
                            }
                        }
                    } );
            //-----------
        }
    }

    private boolean isEmailValid(@Nullable EditText wReference, String password){
//        String wEmail = wReference.getText().toString().trim();
//        String emailRegex =
//                "^[a-zA-Z0-9_+&*-]+(?:\\."+
//                        "[a-zA-Z0-9_+&*-]+)*@" +
//                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
//                        "A-Z]{2,7}$";
//        Pattern pat = Pattern.compile(emailRegex);
//        boolean bool = pat.matcher(wEmail).matches();
//
//        if (TextUtils.isEmpty( wEmail )) {
//            wReference.setError( "Please Enter Email! " );
//            return false;
//        } else if (!bool){
//            wReference.setError( "Please Enter Valid Email! " );
//            return false;
//        }
        if(TextUtils.isEmpty( password )){
            signInPassword.setError( "Required..!" );
            Toast.makeText(  getActivity(),"Please Enter Password",Toast.LENGTH_SHORT ).show();
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



}
