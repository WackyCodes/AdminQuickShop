package com.example.shailendra.admin.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticMethods;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.database.DBquery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.database.DBquery.orderModelList;

public class OrderDetailsActivity extends AppCompatActivity {

    private int index;
    private TextView orderIdTxt;
    private TextView deliveryStatus; // delivery_status
    private TextView orderDateDay; // order_date_day
    private TextView orderTime; // order_time
    private TextView userId; // user_id
    private TextView recieverName; // reciever_name
    private TextView recieverMobile; // reciever_mobile
    private TextView deliveryAddress; // delivery_address
    private TextView deliveryAddPin; // delivery_pin
    private TextView billAmount; // bill_amount
    private TextView deliveryCharge; // delivery_charge
    private TextView payMode; // pay_mode
    private TextView noOfProducts; // no_of_products
    private RecyclerView  orderProductsRecycler; // order_product_det_recycler

    // Conform Activity...
    private FloatingActionButton successDeliveryBtn;

    private Dialog dialog;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_details );

        index = getIntent().getIntExtra( "INDEX", -1 );
        dialog = new DialogsClass().progressDialog( this );

        orderIdTxt = findViewById( R.id.orderId );
        deliveryStatus = findViewById( R.id.delivery_status );
        orderDateDay = findViewById( R.id.order_date_day );
        orderTime = findViewById( R.id.order_time );
        userId = findViewById( R.id.user_id );
        recieverName = findViewById( R.id.reciever_name );
        recieverMobile = findViewById( R.id.reciever_mobile );
        deliveryAddress = findViewById( R.id.delivery_address );
        deliveryAddPin = findViewById( R.id.delivery_pin );
        billAmount = findViewById( R.id.bill_amount );
        deliveryCharge = findViewById( R.id.delivery_charge );
        payMode = findViewById( R.id.pay_mode );
        noOfProducts = findViewById( R.id.no_of_products );
        orderProductsRecycler = findViewById( R.id.order_product_det_recycler );

        // Set Data....
        orderIdTxt.setText( orderModelList.get( index ).getOrderId() );
        deliveryStatus.setText( orderModelList.get(index).getOrderDeliveryStatus() );
        orderDateDay.setText( orderModelList.get(index).getOrderDateAndDay() );
        orderTime.setText( orderModelList.get(index).getOrderTime() );
        userId.setText( orderModelList.get(index).getOrderByUserId() );
        recieverName.setText( orderModelList.get(index).getOrderRecieverName() );
        recieverMobile.setText( orderModelList.get(index).getOrderByUserPhone() );
        deliveryAddress.setText( orderModelList.get(index).getOrderDeliveryAdd() );
        deliveryAddPin.setText( orderModelList.get(index).getOrderDeliveryPin() );
        billAmount.setText( orderModelList.get(index).getOrderBillAmount() );
        deliveryCharge.setText( orderModelList.get(index).getOrderDeliveryCharge() );
        payMode.setText( orderModelList.get(index).getOrderPayMode() );
//        noOfProducts.setText( orderModelList.get(index).getNoOfProducts());

//        orderProductsRecycler

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderProductsRecycler.setLayoutManager( layoutManager );
        OrderProductAdaptor orderProductAdaptor = new OrderProductAdaptor(orderModelList.get( index ).getOrderProductModelList());
        orderProductsRecycler.setAdapter( orderProductAdaptor );
        orderProductAdaptor.notifyDataSetChanged();


        // Phone Call Action...
        recieverMobile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make a phone Call...
                makeAPhoneCall();
            }
        } );

        // Order Success Button....
        successDeliveryBtn = findViewById( R.id.floating_confirmDelivery );
        if (StaticValues.adminData.getAdminType().equals( StaticValues.TYPE_DELIVERY_BOY )
                && isShowSuccessOrderBtn() ){
            successDeliveryBtn.setVisibility( View.VISIBLE );
            successDeliveryBtn.setEnabled( true );
        }else{
            successDeliveryBtn.setVisibility( View.GONE );
            successDeliveryBtn.setEnabled( false );
        }

        successDeliveryBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                successDeliveryBtn.setVisibility( View.GONE );
                // Conform Order Delivered Code...
                // 1. Update On Order...
                // 2. Notify to User...
                Map <String, Object> updateOrderMap = new HashMap <>();
                // Put Delivery Info and Address..
                updateOrderMap.put( "delivery_status", "SUCCESS" );
                updateOrderMap.put( "delivered_by", StaticValues.adminData.getAdminName() );
                updateOrderMap.put( "delivery_date_day", StaticMethods.getCurrentDateDay() );
                updateOrderMap.put( "delivery_time", StaticMethods.getCurrentTime() );

                DBquery.updateOrderStatusQuery( OrderDetailsActivity.this, dialog,
                        orderModelList.get( index ).getOrderId(), updateOrderMap,
                        orderModelList.get(index).getOrderByUserId() );

                orderModelList.get(index).setOrderDeliveryStatus( "SUCCESS" );
                deliveryStatus.setText( "SUCCESS" );
            }
        } );

    }

    @SuppressLint("MissingPermission")
    private void makeAPhoneCall(){
        Intent callIntent = new Intent( Intent.ACTION_CALL );
        callIntent.setData( Uri.parse( "tel:"+orderModelList.get(index).getOrderByUserPhone() ) );
        if (ActivityCompat.checkSelfPermission(
                OrderDetailsActivity.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText( this, "Permission Denied.! You have to give permission to make call.", Toast.LENGTH_SHORT ).show();
            return;
        }else{
            startActivity( callIntent );
        }
    }

    private boolean isShowSuccessOrderBtn(){

        if (orderModelList.get(index).getOrderDeliveryStatus().equals( "ACCEPTED" )){
            return true;
        }else{
            return false;
        }
    }


    private class OrderProductAdaptor extends RecyclerView.Adapter <OrderProductAdaptor.ViewHolder>{

        List <OrderProductModel> orderProductModelList;

        public OrderProductAdaptor(List <OrderProductModel> orderProductModelList) {
            this.orderProductModelList = orderProductModelList;
        }

        @NonNull
        @Override
        public OrderProductAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.order_product_item_layout, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull OrderProductAdaptor.ViewHolder holder, int position) {
            String imgLink = orderProductModelList.get( position ).getProductImage();
            String name = orderProductModelList.get( position ).getProductName();
            String price = orderProductModelList.get( position ).getProductPrice();
            String qty = orderProductModelList.get( position ).getProductQty();
            holder.setData( imgLink, name, price, qty );
        }

        @Override
        public int getItemCount() {
            return orderProductModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

//            private String productId;
            private ImageView imageView;
            private TextView productName;
            private TextView productPrice;
            private TextView productQty;

            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                imageView = itemView.findViewById( R.id.order_product_img );
                productName = itemView.findViewById( R.id.order_product_name );
                productPrice = itemView.findViewById( R.id.order_price );
                productQty = itemView.findViewById( R.id.order_qty );
            }

            public void setData( String productImage, String name, String price, String Qty){

                Glide.with( itemView.getContext() ).load( productImage )
                        .apply( new RequestOptions().placeholder( R.drawable.square_placeholder) ).into( imageView );
                productName.setText( name );
                productPrice.setText( "Rs."+ price + "/-" );
                productQty.setText( Qty );
            }

        }
    }




}


