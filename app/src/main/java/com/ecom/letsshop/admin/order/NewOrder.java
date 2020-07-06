package com.ecom.letsshop.admin.order;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecom.letsshop.admin.R;
import com.ecom.letsshop.admin.database.DBquery;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrder extends Fragment {

    private RecyclerView orderRecycler;

    public NewOrder() {
        // Required empty public constructor
    }

    public static OrderAdaptor orderListAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_new_order, container, false );

        orderRecycler = view.findViewById( R.id.new_order_recycler );

        LinearLayoutManager layoutManager = new LinearLayoutManager( view.getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderRecycler.setLayoutManager( layoutManager );
        // TODO : Create a adaptor and set that on recycler..
        orderListAdaptor = new OrderAdaptor( DBquery.orderModelList );
        orderRecycler.setAdapter( orderListAdaptor );
        if ( DBquery.orderModelList.size() == 0){
            DBquery.getOrderListQuery( );
        }else{
            orderListAdaptor.notifyDataSetChanged();
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (orderListAdaptor!=null){
            orderListAdaptor.notifyDataSetChanged();
        }
    }


}
