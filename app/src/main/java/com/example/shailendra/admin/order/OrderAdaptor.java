package com.example.shailendra.admin.order;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.admin.R;
import com.example.shailendra.admin.update.HorizontalViewAllAdaptor;

import java.util.List;

public class OrderAdaptor extends RecyclerView.Adapter <OrderAdaptor.ViewHolder> {

    private List<OrderModel> orderModelList;

    public OrderAdaptor(List <OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.new_order_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdaptor.ViewHolder holder, int position) {
        OrderModel orderModel = orderModelList.get( position );

        holder.setData( position, orderModel.getOrderId(), orderModel.getOrderDateAndDay(),
                orderModel.getOrderTime(), orderModel.getOrderDeliveryStatus() );

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIdTxt;
        private TextView orderDateDayTxt;
        private TextView orderTimeTxt;
        private TextView orderStatus;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            orderIdTxt = itemView.findViewById( R.id.orderID );
            orderDateDayTxt = itemView.findViewById( R.id.orderDateDay );
            orderTimeTxt = itemView.findViewById( R.id.orderTime );
            orderStatus = itemView.findViewById( R.id.order_status );

        }

        public void setData(final int index, String orderId, String orderDateDay, String orderTime, String status){
            orderIdTxt.setText( "Order ID : "+ orderId );
            orderDateDayTxt.setText(  orderDateDay );
            orderTimeTxt.setText(  orderTime );
            orderStatus.setText(  status );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( itemView.getContext(), OrderDetailsActivity.class );
                    intent.putExtra( "INDEX", index );
                    itemView.getContext().startActivity( intent );
                }
            } );

        }

    }


}
