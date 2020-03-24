package com.example.shailendra.admin.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.admin.R;

import java.util.List;

import static com.example.shailendra.admin.StaticValues.NOTIFY_ORDER_REQUEST;
import static com.example.shailendra.admin.StaticValues.NOTIFY_OUT_OF_STOCK;
import static com.example.shailendra.admin.StaticValues.NOTIFY_PROBLEM_REPORT;
import static com.example.shailendra.admin.StaticValues.NOTIFY_SUCCESS_DELIVERED;

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
                ((NotificationsAdaptor.OrderRequestViewHolder)holder).setData();
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
        public OrderRequestViewHolder(@NonNull View itemView) {
            super( itemView );
        }

        // Set Data...
        private void setData(){

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


}
