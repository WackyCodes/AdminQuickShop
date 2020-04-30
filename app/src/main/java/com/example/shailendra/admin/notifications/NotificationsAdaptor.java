package com.example.shailendra.admin.notifications;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.StaticValues;
import com.example.shailendra.admin.database.DBquery;
import com.example.shailendra.admin.order.OrderDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.StaticValues.NOTIFY_ORDER_REQUEST;
import static com.example.shailendra.admin.StaticValues.NOTIFY_OUT_OF_STOCK;
import static com.example.shailendra.admin.StaticValues.NOTIFY_PROBLEM_REPORT;
import static com.example.shailendra.admin.StaticValues.NOTIFY_SUCCESS_DELIVERED;
import static com.example.shailendra.admin.database.DBquery.addOneInOrderListQuery;
import static com.example.shailendra.admin.database.DBquery.firebaseFirestore;

public class NotificationsAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationsModel> notificationsModelList;
    public NotificationsAdaptor(List <NotificationsModel> notificationsModelList) {
        this.notificationsModelList = notificationsModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (notificationsModelList.get( position ).getNotifyType()){
            case NOTIFY_ORDER_REQUEST:
                return NOTIFY_ORDER_REQUEST;
            case NOTIFY_OUT_OF_STOCK:
                return NOTIFY_OUT_OF_STOCK;
            case NOTIFY_PROBLEM_REPORT:
                return NOTIFY_PROBLEM_REPORT;
            case NOTIFY_SUCCESS_DELIVERED:
                return NOTIFY_SUCCESS_DELIVERED;
                default:
                    return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case NOTIFY_ORDER_REQUEST:
                View orderRequestView = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.notify_layout_1, parent, false );
                return new OrderRequestViewHolder( orderRequestView );
            case NOTIFY_OUT_OF_STOCK:
                View outOfStockView = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.notify_layout_1, parent, false );

                return new OutOfStockViewHolder( outOfStockView );
            case NOTIFY_PROBLEM_REPORT:
                View reportProblemView = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.notify_layout_1, parent, false );

                return new ReportProblemViewHolder( reportProblemView );
            case NOTIFY_SUCCESS_DELIVERED:
                View successOrderView = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.notify_layout_1, parent, false );

                return new SuccessDeliverViewHolder( successOrderView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (notificationsModelList.get( position ).getNotifyType()){
            case NOTIFY_ORDER_REQUEST:
                NotificationsModel notificationsModel = notificationsModelList.get( position );
                String orderId = notificationsModel.getNotifyOrderId();
                String userId = notificationsModel.getNotifyUserId();
                ((NotificationsAdaptor.OrderRequestViewHolder)holder).setData( position, orderId, userId );
                break;
            case NOTIFY_OUT_OF_STOCK:
                ((NotificationsAdaptor.OutOfStockViewHolder)holder).setData();
                break;
            case NOTIFY_PROBLEM_REPORT:
                ((NotificationsAdaptor.ReportProblemViewHolder)holder).setData();
                break;
            case NOTIFY_SUCCESS_DELIVERED:
                ((NotificationsAdaptor.SuccessDeliverViewHolder)holder).setData();
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return notificationsModelList.size();
    }


    // RequestOrderViewHolder Class.......
    public class OrderRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView HeadingText;
        private TextView subHeadingText;
        private Button acceptBtn;
        private Button viewOrderBtn;
        private ImageView imageView;
        //int notifyType, String notifyImage, String headText, String subHeadingText)
        private Dialog dialog;
        public OrderRequestViewHolder(@NonNull View itemView) {
            super( itemView );
            HeadingText = itemView.findViewById( R.id.heading );
            subHeadingText = itemView.findViewById( R.id.sub_hading );
            acceptBtn = itemView.findViewById( R.id.acceptBtn );
            viewOrderBtn = itemView.findViewById( R.id.viewBtn );
            imageView = itemView.findViewById( R.id.notify_image );
            dialog = new DialogsClass().progressDialog( itemView.getContext() );
        }

        // Set Data...
        private void setData(final int index, final String orderId, final String userId ){
            HeadingText.setText( "You have a new Order" );
            subHeadingText.setText( "Order ID : "+orderId );
//            Glide.with( itemView.getContext() ).load( orderImage )
//                    .apply( new RequestOptions().placeholder( R.mipmap.logo ) ).into( imageView );
            imageView.setImageResource( R.mipmap.logo );
            if (StaticValues.adminData.getAdminType().equals( StaticValues.TYPE_DELIVERY_BOY )){
                acceptBtn.setEnabled( false );
            }
                acceptBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptBtn.setEnabled( false );
                    dialog.show();
                    // Update in Database...
//                    (2) ACCEPTED : when order has been accepted...
                    firebaseFirestore.collection( "COM_ORDERS" ).document( orderId )
                            .update( "delivery_status", "ACCEPTED" )
                            .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                showToast(itemView.getContext(), "Accepted..!" );
                                // Query...
//                                notificationsModelList.remove( index );
                                if(NotificationsActivity.notificationsAdaptor!=null){
                                    NotificationsActivity.notificationsAdaptor.notifyDataSetChanged();
                                }
                                // Notify to user...
                                queryToNotifyUser(userId, orderId);
                                // Query to update in Local
                                DBquery.addOneInOrderListQuery( orderId, itemView.getContext(), false );
                            }else{
                                dialog.dismiss();
                            }
                        }
                    } );
                }
            } );

            viewOrderBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // View Order ...
                    for (int i = 0; i < DBquery.orderModelList.size(); i++){
                        if (DBquery.orderModelList.get( i ).getOrderId().equals( orderId )){
                            // GOTO : View Order Activity..!
                            Intent intent = new Intent( itemView.getContext(), OrderDetailsActivity.class );
                            intent.putExtra( "INDEX", i );
                            itemView.getContext().startActivity( intent );
                            break;
                        }else if ( i == DBquery.orderModelList.size()){
                            DBquery.addOneInOrderListQuery( orderId, itemView.getContext(), true );
                            break;
                        }
                    }

                }
            } );

        }
        private void queryToNotifyUser(String notifyUserId, final String orderId){
            Map <String, Object> userData = new HashMap <>();
            userData.put( "notify_type", "ORDER_UPDATE" );
            userData.put( "notify_image", " " );
            userData.put( "notify_order_id", orderId );
            userData.put( "notify_heading", "Order Accepted.!" );
            userData.put( "notify_text", "Your order has been accepted.!" );
            userData.put( "notify_read", false );

            DBquery.queryToNotifyUser( itemView.getContext(), null, notifyUserId, userData );

        }

    }

    // OutOfStockNotification Class.......
    public class OutOfStockViewHolder extends RecyclerView.ViewHolder {
        public OutOfStockViewHolder(@NonNull View itemView) {
            super( itemView );
        }
        // Set Data...
        private void setData(){

        }
    }

    // OutOfStockNotification Class.......
    public class ReportProblemViewHolder extends RecyclerView.ViewHolder {
        public ReportProblemViewHolder(@NonNull View itemView) {
            super( itemView );
        }
        // Set Data...
        private void setData(){

        }
    }
    // OutOfStockNotification Class.......
    public class SuccessDeliverViewHolder extends RecyclerView.ViewHolder {
        public SuccessDeliverViewHolder(@NonNull View itemView) {
            super( itemView );
        }
        // Set Data...
        private void setData(){

        }
    }

    private void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

}
