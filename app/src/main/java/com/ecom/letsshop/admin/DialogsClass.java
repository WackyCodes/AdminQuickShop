package com.ecom.letsshop.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DialogsClass {

    public DialogsClass() {
    }

    public Dialog progressDialog(Context context){

        // ---- Progress Dialog...
        Dialog progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.dialog_progress_layout );
        progressDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressDialog.getWindow().setBackgroundDrawable( context.getDrawable( R.drawable.back_white_rad_8dp ) );
        }
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        progressDialog.show();
        return progressDialog;
        // ---- Progress Dialog...
    }

    public Dialog progressPerDialog(Context context){

        // ---- Progress Dialog...
        Dialog progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.dialog_per_progress_layout );
        progressDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressDialog.getWindow().setBackgroundDrawable( context.getDrawable( R.drawable.back_white_rad_8dp ) );
        }
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        TextView perText = progressDialog.findViewById( R.id.process_per_complete_text );
        perText.setVisibility( View.GONE );
//        progressDialog.show();
        return progressDialog;
        // ---- Progress Dialog...
    }

    public AlertDialog.Builder alertDialog(Context context,@Nullable String title, String message,@Nullable Drawable icon){
        AlertDialog.Builder alertD = new AlertDialog.Builder( context );
        if (title != null){
            alertD.setTitle( title );
        }
        alertD.setMessage( message );
        if (icon != null){
            alertD.setIcon( icon );
        }
        alertD.setCancelable( false );
        alertD.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );

        return alertD;
    }

    public static boolean isInternetConnect(Context context) {
        CheckInternetConnection checkInternetCON = new CheckInternetConnection();
        if (checkInternetCON.checkInternet( context )) {
            return false;
        } else {
            return true;
        }

    }

}
