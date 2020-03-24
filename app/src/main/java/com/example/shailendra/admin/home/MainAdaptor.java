package com.example.shailendra.admin.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.catlayouts.BannerAndCatModel;
import com.example.shailendra.admin.database.UpdateImages;

import java.util.List;

public class MainAdaptor extends RecyclerView.Adapter<MainAdaptor.ViewHolder> {

    private List<BannerAndCatModel> mainModelList;

    public MainAdaptor(List <BannerAndCatModel> mainModelList) {
        this.mainModelList = mainModelList;
    }

    @NonNull
    @Override
    public MainAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.main_item_layout, parent, false );
       return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdaptor.ViewHolder holder, int position) {
        if (position < mainModelList.size()){
            String imgLink = mainModelList.get( position ).getImageLink();
            String heading = mainModelList.get( position ).getTitleOrBgColor();
            holder.setData(position, imgLink, heading);
        }
        if (position == mainModelList.size()){
            holder.addNewCatItem();
        }
    }

    @Override
    public int getItemCount() {
        return mainModelList.size()+1 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout categoryLayout;
        private LinearLayout addNewCatLayout;
        private ImageView mainImage;
        private TextView mainHeading;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mainImage = itemView.findViewById( R.id.main_home_image );
            mainHeading = itemView.findViewById( R.id.main_home_text );
            categoryLayout = itemView.findViewById( R.id.home_cat_item );
            addNewCatLayout = itemView.findViewById( R.id.add_new_cat_btn_lay );
        }

        private void setData(final int index, String imgLink, final String heading){
            categoryLayout.setVisibility( View.VISIBLE );
            addNewCatLayout.setVisibility( View.GONE );
//            mainImage.setImageResource( R.drawable.ic_home_black_24dp );
            Glide.with( itemView.getContext() ).load( imgLink ).
                    apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( mainImage );
            mainHeading.setText( heading );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( itemView.getContext(), CommonCatActivity.class);
                    intent.putExtra( "TITLE", heading );
                    intent.putExtra( "INDEX", index );
                    itemView.getContext().startActivity( intent );
                }
            } );
        }
        private void addNewCatItem(){
            categoryLayout.setVisibility( View.GONE );
            addNewCatLayout.setVisibility( View.VISIBLE );
            addNewCatLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateImages.uploadImageLink = null;
                    MainFragment.addNewCatItemRelativeLayout.setVisibility( View.VISIBLE );
                    MainFragment.mainRecycler.setVisibility( View.INVISIBLE );
                }
            } );
        }

    }

}
