package com.example.shailendra.admin.order;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shailendra.admin.R;
import com.example.shailendra.admin.database.DBquery;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrder extends Fragment {

    private RecyclerView orderRecycler;

    public NewOrder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_new_order, container, false );

        orderRecycler = view.findViewById( R.id.new_order_recycler );

        LinearLayoutManager layoutManager = new LinearLayoutManager( view.getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderRecycler.setLayoutManager( layoutManager );
        // TODO : Create a adaptor and set that on recycler..
        if ( DBquery.orderModelList.size() == 0){
            DBquery.getOrderListQuery( orderRecycler );
        }else{
            OrderAdaptor orderAdaptor = new OrderAdaptor( DBquery.orderModelList );
            orderRecycler.setAdapter( orderAdaptor );
            orderAdaptor.notifyDataSetChanged();
        }

        return view;

    }




}
