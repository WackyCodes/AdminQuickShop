package com.example.shailendra.admin.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticMethods;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.catlayouts.BannerAndCatModel;
import com.example.shailendra.admin.database.UpdateImages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.shailendra.admin.StaticValues.tempProductAreaCode;
import static com.example.shailendra.admin.StaticValues.userCityName;

public class MainAdaptor extends RecyclerView.Adapter<MainAdaptor.ViewHolder> {

    private List<BannerAndCatModel> mainModelList;

    public MainAdaptor(List <BannerAndCatModel> mainModelList) {
        this.mainModelList = mainModelList;
    }

    @NonNull
    @Override
    public MainAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.main_item_layout, parent, false );
       return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdaptor.ViewHolder holder, int position) {
        if (position < mainModelList.size()){
            String imgLink = mainModelList.get( position ).getImageLink();
            String heading = mainModelList.get( position ).getTitleOrBgColor();
            holder.setData(position, imgLink, heading);
        }
        if (position == mainModelList.size()){
            holder.addNewCatItem();
        }
    }

    @Override
    public int getItemCount() {
        return mainModelList.size()+1 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout categoryLayout;
        private LinearLayout addNewCatLayout;
        private ImageView mainImage;
        private TextView mainHeading;
        private ImageView editCatBtn;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mainImage = itemView.findViewById( R.id.main_home_image );
            mainHeading = itemView.findViewById( R.id.main_home_text );
            categoryLayout = itemView.findViewById( R.id.home_cat_item );
            addNewCatLayout = itemView.findViewById( R.id.add_new_cat_btn_lay );
            editCatBtn = itemView.findViewById( R.id.edit_cat_ImgView );
        }

        private void setData(final int index, String imgLink, final String heading){
            categoryLayout.setVisibility( View.VISIBLE );
            addNewCatLayout.setVisibility( View.GONE );
//            mainImage.setImageResource( R.drawable.ic_home_black_24dp );
            if (heading.toUpperCase().equals( "HOME" )){
                mainImage.setImageResource( R.drawable.ic_home_black_24dp );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mainImage.setImageTintList( ColorStateList.valueOf( itemView.getResources().getColor( R.color.colorPrimary ) ) );
                }
            }else{
                Glide.with( itemView.getContext() ).load( imgLink ).
                        apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( mainImage );
            }
            mainHeading.setText( heading );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( itemView.getContext(), CommonCatActivity.class);
                    intent.putExtra( "TITLE", heading );
                    intent.putExtra( "INDEX", index );
                    itemView.getContext().startActivity( intent );
                }
            } );
            editCatBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code For Edit Category layout...
                    if (heading.toUpperCase().equals( "HOME" )){
                        Toast.makeText( itemView.getContext(), "You Can't Change Home Section!", Toast.LENGTH_SHORT ).show();
                    }else{
                        PopupWindow popupWindowDogs;
                        // format is DogName::DogID
                        List<String> dogsList = new ArrayList <String>();
//                    dogsList.add( "Move::" + StaticValues.ID_MOVE );
//                    dogsList.add( "Copy::" + StaticValues.ID_COPY );
                        dogsList.add( "Delete::" + StaticValues.ID_DELETE );
                        // convert to simple array
                        String[] popUpContents = new String[dogsList.size()];
                        dogsList.toArray(popUpContents);
                        // initialize pop up window
                        popupWindowDogs = popupWindowDogs( itemView.getContext(), false, index, popUpContents);
                        popupWindowDogs.showAsDropDown(v);
                    }
                }
            } );
        }
        private void addNewCatItem(){
            categoryLayout.setVisibility( View.GONE );
            addNewCatLayout.setVisibility( View.VISIBLE );
            addNewCatLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateImages.uploadImageLink = null;
                    MainFragment.addNewCatItemRelativeLayout.setVisibility( View.VISIBLE );
                    MainFragment.mainRecycler.setVisibility( View.INVISIBLE );
                }
            } );
        }

    }

    // More Option CLick...
    public PopupWindow popupWindowDogs(final Context context, final boolean isCopyMove, final int listIndex, String popUpContents[]) {
        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(context);
        final Dialog dialog = new DialogsClass().progressDialog( context );
        // the drop down list is a list view
        ListView listViewDogs = new ListView(context);
        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter( StaticMethods.dogsAdapter( popUpContents, context ));
        listViewDogs.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                dialog.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder( context );
                alertDialog.setTitle( "Do you want to delete this Category.?" );
                alertDialog.setMessage( "It will delete all content inside of this category.!!" );
                alertDialog.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        dialog.show();
                        popupWindow.dismiss();
//                String layCopyMoveID = ((TextView) view).getText().toString();
//                String selectedItemTag = ((TextView) view).getTag().toString();
//                int ind = Integer.parseInt( layCopyMoveID );

                        if ( userCityName != null && tempProductAreaCode != null){
                            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                                    .collection( "CATEGORIES" ).document( mainModelList.get( listIndex ).getTitleOrBgColor().toUpperCase() )
                                    .delete().addOnCompleteListener( new OnCompleteListener <Void>() {
                                @Override
                                public void onComplete(@NonNull Task <Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText( context, "Remove category Success.", Toast.LENGTH_SHORT ).show();
                                        mainModelList.remove( listIndex );
                                        if (MainFragment.homeCatAdaptor != null){
                                            MainFragment.homeCatAdaptor.notifyDataSetChanged();
                                        }
                                    }else{
                                        dialog.dismiss();
                                        Toast.makeText( context, "Failed.. Something Went Wrong.!", Toast.LENGTH_SHORT ).show();
                                    }
                                }
                            } );
                        }else{
                            Toast.makeText( context, "Something Went Wrong..!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    }
                } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        popupWindow.dismiss();
                    }
                } ).show();

            }
        } );

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth( WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight( WindowManager.LayoutParams.WRAP_CONTENT);
        // set the list view as pop up window content...
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }


}
