package com.example.shailendra.admin.userprofile;


import android.app.Dialog;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.MainActivity;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.database.DBquery;
import com.example.shailendra.admin.home.MainFragment;

import static com.example.shailendra.admin.StaticValues.ADD_MEMBER_FRAGMENT;


public class AdminMemberList extends Fragment {

    public AdminMemberList() {
        // Required empty public constructor
    }

    private FrameLayout adminListFrameLayout;
    private RecyclerView adminListRecycler;
    private LinearLayout addNewMemberLayout;

    public static AdminMemberAdaptor adminMemberAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_admin_member_list, container, false );
        Dialog dialog = new DialogsClass().progressDialog( getContext() );

        adminListRecycler = view.findViewById( R.id.member_list_recycler );
        addNewMemberLayout = view.findViewById( R.id.add_new_member_layout );
        adminListFrameLayout = view.findViewById( R.id.admin_member_list_frame );

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        adminListRecycler.setLayoutManager( layoutManager );

        adminMemberAdaptor = new AdminMemberAdaptor( DBquery.adminMemberList );
        adminListRecycler.setAdapter( adminMemberAdaptor );
        adminMemberAdaptor.notifyDataSetChanged();

        if (DBquery.adminMemberList.size() == 0){

            dialog.show();

            if (StaticValues.userCityName != null){

                DBquery.getMemberListQuery( StaticValues.userCityName, dialog);
            }else{
                Toast.makeText( getContext(), "Please select City First.!", Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
            }

        }
        else{
            if (StaticValues.userCityName != null){
                if (!DBquery.adminMemberList.get( 0 ).getAdminCityName().toUpperCase().equals( StaticValues.userCityName.toUpperCase() )){
                    dialog.show();
                    DBquery.getMemberListQuery( StaticValues.userCityName, dialog);
                }
            }else{
                Toast.makeText( getContext(), "Please select City First.!", Toast.LENGTH_SHORT ).show();
            }
        }

        addNewMemberLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add New Member Method.. Call
                setFragment( new AddAdminMember(), ADD_MEMBER_FRAGMENT );
                MainActivity.drawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
            }
        } );

        return view;
    }
    @Override
    public void onDestroyView() {
        adminListFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment(Fragment fragment, int currentFragment) {
        MainActivity.wCurrentFragment = currentFragment;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
        onDestroyView();
        fragmentTransaction.replace( adminListFrameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }

}
