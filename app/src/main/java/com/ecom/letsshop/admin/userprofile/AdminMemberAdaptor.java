package com.ecom.letsshop.admin.userprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.letsshop.admin.R;

import java.util.List;

public class AdminMemberAdaptor extends RecyclerView.Adapter<AdminMemberAdaptor.ViewHolder> {

    private List<AdminData> adminDataList;

    public AdminMemberAdaptor(List <AdminData> adminDataList) {
        this.adminDataList = adminDataList;
    }

    @NonNull
    @Override
    public AdminMemberAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.member_list_item_layout, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMemberAdaptor.ViewHolder holder, int position) {
        AdminData adminData = adminDataList.get( position );
        String imgLink = adminData.getAdminPhoto();
        String name = adminData.getAdminName();
        String mobile = adminData.getAdminMobile();
        String email = adminData.getAdminEmail();
        String authId = adminData.getAdminAuthId();
        String type = adminData.getAdminType();
        holder.setData( position, imgLink, name, mobile, email, authId, type );

    }

    @Override
    public int getItemCount() {
        return adminDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout contentLayout;
        private ImageView userImage;
        private TextView userName;
        private TextView userMobile;
        private TextView userEmail;
        private TextView userAuth;
        private TextView userPermissionSpinner;
        private ArrayAdapter <CharSequence> adminTypeAdaptor;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            contentLayout = itemView.findViewById( R.id.member_item_layout );
            userImage = itemView.findViewById( R.id.memberImage );
            userName = itemView.findViewById( R.id.memberName );
            userMobile = itemView.findViewById( R.id.memberMobile );
            userEmail = itemView.findViewById( R.id.memberEmail );
            userAuth = itemView.findViewById( R.id.memberAuth );
            userPermissionSpinner = itemView.findViewById( R.id.memberPermissionSpinner );

            adminTypeAdaptor = ArrayAdapter.createFromResource( itemView.getContext(),
                    R.array.admin_permission, android.R.layout.simple_spinner_item);
            adminTypeAdaptor.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        }

        @SuppressLint("ResourceAsColor")
        private void setData(int index, String imgLink, String name, String mobile, String email, String authId, final String permissionId){

            //Set Data...
            if (imgLink != null){
                Glide.with( itemView.getContext() ).load( imgLink ).
                        apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( userImage );
            }

            userName.setText( name );
            userMobile.setText( mobile );
            userEmail.setText( email );

            if (authId != null){
                userAuth.setText( "created" );
                userAuth.setTextColor( ColorStateList.valueOf( R.color.colorGreen ) );
            }else{
                userAuth.setText( "Not auth yet.!" );
                userAuth.setTextColor( ColorStateList.valueOf( R.color.colorRed ) );
            }


            contentLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // OnClick...
//                    Intent showAdminIntent = new Intent(itemView.getContext(), ShowAdminActivity.class);
//
//                    showAdminIntent.putExtra( "ADMIN_ID", "");
//                    showAdminIntent.putExtra( "ADMIN_PIC", "");
//                    showAdminIntent.putExtra( "ADMIN_NAME", "");
//                    showAdminIntent.putExtra( "ADMIN_MOBILE", "");
//                    showAdminIntent.putExtra( "ADMIN_EMAIL", "");
//                    showAdminIntent.putExtra( "ADMIN_TYPE", "");
//                    showAdminIntent.putExtra( "ADMIN_PERMISSION", "");
//                    showAdminIntent.putExtra( "ADMIN_CITY", "");
//
//                    itemView.getContext().startActivity( showAdminIntent );

                }
            } );

            // TODO : Spinner Set..

            userPermissionSpinner.setText( permissionId );

//            userPermissionSpinner.setAdapter( adminTypeAdaptor );
//            if (StaticValues.adminData.getAdminType().equals( "A" )){
//                userPermissionSpinner.setClickable( false );
//            }else{
//                userPermissionSpinner.setClickable( true );
//            }

//            if(permissionId.equals( "A" )){
//                userPermissionSpinner.setSelected( adminTypeAdaptor.isEnabled( 1 ) );
//            }else if (permissionId.equals( "B" )){
//
//            }else if (permissionId.equals( "C" )){
//
//            }else if (permissionId.equals( "D" )){
//
//            }
//
//            userPermissionSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
////                    switch (position){
////                        case 0:
////                            newMemberPermission = null;
////                            break;
////                        case 1:
////                            newMemberPermission = "A";
////                            break;
////                        case 2:
////                            newMemberPermission = "B";
////                            break;
////                        case 3:
////                            newMemberPermission = "C";
////                            break;
////                        default:
////                            break;
////                    }
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView <?> parent) {
//
//                }
//            } );



        }

    }


}
