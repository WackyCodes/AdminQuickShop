package com.example.shailendra.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shailendra.admin.home.CommonCatActivity;
import com.example.shailendra.admin.home.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.shailendra.admin.database.DBquery.currentUser;
import static com.example.shailendra.admin.database.DBquery.firebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainHomeContentFrame;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mainHomeContentFrame = findViewById( R.id.main_content_FrameLayout );

        if (currentUser == null){
            firebaseAuth.signInWithEmailAndPassword("admin@myshop.com", "123123")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                currentUser = firebaseAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                            }

                        }
                    });
        }

        setFragment( new MainFragment() );
    }


    // Fragment Transaction...
    public void setFragment(Fragment fragment){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
            fragmentTransaction.replace( mainHomeContentFrame.getId(),fragment );
            fragmentTransaction.commit();

    }


    // Sample Query
    private void sampleQuery(){
        firebaseFirestore.collection( "USER" ).document("SJFiZ7YOlSYqNQ8EMj4ycECsjeh1").get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
//                    userName.setText( task.getResult().get( "user_name" ).toString() );
                }
            }
        } );

    }


}
