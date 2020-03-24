package com.example.shailendra.admin.catlayouts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.addnewitem.AddNewLayoutActivity;

import java.util.List;

import static com.example.shailendra.admin.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;

public class BannerItemAdaptor extends RecyclerView.Adapter<BannerItemAdaptor.ViewHolder> {

    List <BannerAndCatModel> bannerAndCatModelList;
    boolean isViewAll;
    int layoutIndex;
    int catIndex;
    String categoryTitle;

    public BannerItemAdaptor(List <BannerAndCatModel> bannerAndCatModelList, boolean isViewAll, int layoutIndex, int catIndex, String categoryTitle) {
        this.bannerAndCatModelList = bannerAndCatModelList;
        this.isViewAll = isViewAll;
        this.layoutIndex = layoutIndex;
        this.catIndex = catIndex;
        this.categoryTitle = categoryTitle;
    }

    @NonNull
    @Override
    public BannerItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.banner_slider_item_layout, parent, false );
        return new ViewHolder( bannerView );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerItemAdaptor.ViewHolder holder, int position) {
        if (position < bannerAndCatModelList.size() ){
            String imgLink = bannerAndCatModelList.get( position ).getImageLink();
            String bgColor = bannerAndCatModelList.get( position ).getTitleOrBgColor();
            holder.setData( imgLink, bgColor );
        }
        if ( !isViewAll && position == bannerAndCatModelList.size() ){
            holder.setAddNew();
        }
    }

    @Override
    public int getItemCount() {
        if (isViewAll){
            return bannerAndCatModelList.size();
        }else {
            return bannerAndCatModelList.size()+1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bannerImage;
        private LinearLayout addNewItemLayout;
        private ImageView editBannerLayBtn;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerImage = itemView.findViewById( R.id.banner_slider_Image );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_Linearlayout );
            editBannerLayBtn = itemView.findViewById( R.id.update_item_lay_imgBtn );
        }

        private void setData(String imgLink, String bgColor ){
            if (isViewAll){
                editBannerLayBtn.setVisibility( View.VISIBLE );
            }
            bannerImage.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.banner_placeholder ) ).into( bannerImage );
            // Set Backgrgound color...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bannerImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( bgColor ) ));
            }
        }
        private void setAddNew(){
            bannerImage.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start Activity...
                    Intent intent = new Intent( itemView.getContext(), AddNewLayoutActivity.class );
                    intent.putExtra( "CAT_TITLE", categoryTitle );
                    intent.putExtra( "CAT_INDEX", catIndex );
                    intent.putExtra( "LAY_TYPE", BANNER_SLIDER_CONTAINER_ITEM );
                    intent.putExtra( "LAY_INDEX", layoutIndex );
                    intent.putExtra( "TASK_UPDATE", false );
                    itemView.getContext().startActivity( intent );
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();
                }
            } );
        }

    }

}
