package com.example.shailendra.admin.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shailendra.admin.R;

public class FakeAdaptor extends RecyclerView.Adapter<FakeAdaptor.ViewHolder> {
//    private int placeHolderType;
    public FakeAdaptor( ) {
    }

//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType( position );
//    }

    @NonNull
    @Override
    public FakeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fake_adopter_home, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull FakeAdaptor.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
        }
    }


}
