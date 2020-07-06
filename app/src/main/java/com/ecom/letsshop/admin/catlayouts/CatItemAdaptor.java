package com.ecom.letsshop.admin.catlayouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.letsshop.admin.R;

import java.util.List;

public class CatItemAdaptor extends RecyclerView.Adapter<CatItemAdaptor.ViewHolder> {
    List <BannerAndCatModel> bannerAndCatModelList;
    public CatItemAdaptor(List <BannerAndCatModel> bannerAndCatModelList) {
        this.bannerAndCatModelList = bannerAndCatModelList;
    }

    @NonNull
    @Override
    public CatItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View catView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.add_item_round_layout, parent, false );
        return new ViewHolder( catView );
    }

    @Override
    public void onBindViewHolder(@NonNull CatItemAdaptor.ViewHolder holder, int position) {
        String imgLink = bannerAndCatModelList.get( position ).getImageLink();
        String title = bannerAndCatModelList.get( position ).getTitleOrBgColor();
        holder.setData( imgLink, title );
    }

    @Override
    public int getItemCount() {
        return bannerAndCatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView catIcon;
        private TextView catTitle;
        private LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            catIcon = itemView.findViewById( R.id.add_item_image );
            catTitle = itemView.findViewById( R.id.add_item_title );
            layout = itemView.findViewById( R.id.catNewItemLinearLayout );
        }
        private void setData(String imgLink, final String titleText ){
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( catIcon );
            catTitle.setText( titleText );
            layout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText( itemView.getContext(), "Go back to view "+ titleText + " page.!", Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }

}
