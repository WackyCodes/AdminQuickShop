package com.example.shailendra.admin.addnewitem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shailendra.admin.R;

public class NewBannerFragment extends Fragment {



    public NewBannerFragment() {
        // Required empty public constructor
    }

    /**
     * This Fragment for  Task...
     * 1. New Banner in Slider
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_new_banner, container, false );

        // Assign The variables...


        return view;

    }

}
