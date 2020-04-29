package com.example.shailendra.admin.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.database.DBquery;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_details );

        index = getIntent().getIntExtra( "INDEX", -1 );

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


