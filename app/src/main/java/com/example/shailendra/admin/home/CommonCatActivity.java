package com.example.shailendra.admin.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.shailendra.admin.DialogsClass;
import com.example.shailendra.admin.R;
import com.example.shailendra.admin.addnewitem.AddNewLayoutActivity;
import com.example.shailendra.admin.database.DBquery;

import java.util.ArrayList;

import static com.example.shailendra.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.home.MainFragment.commonCatList;

public class CommonCatActivity extends AppCompatActivity implements View.OnClickListener {
    DialogsClass dialogsClass = new DialogsClass();
    Dialog dialog;

    private RecyclerView commonCatListRecycler;
    public static CommonCatAdaptor commonCatAdaptor;

    private int index = -1;
    private String categoryTitle;

    // New Layout Variable...
    private LinearLayout addNewLayoutBtn;
    private ImageView addNewLayoutCloseBtn;
    private RelativeLayout addNewLayoutFrame;

    private LinearLayout addNewBannerSliderLayBtn;
    private LinearLayout addNewProductsHrLayBtn;
    private LinearLayout addNewProductsGridLayBtn;
    private LinearLayout addNewBannerAdLayBtn;
    private LinearLayout addNewStripAdLayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_common_cat );
        // - Toolbar Menu _---------------------
        Toolbar toolbar = findViewById( R.id.toolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        categoryTitle = getIntent().getStringExtra( "TITLE" );
        index = getIntent().getIntExtra( "INDEX", -1 );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( categoryTitle );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        } catch (NullPointerException e) {
        }
        // - Toolbar Menu _---------------------
        dialog = dialogsClass.progressDialog( this );
        // New Layout Variable Assign..
        addNewLayoutBtn = findViewById( R.id.add_new_layout_LinearLayBtn );
        addNewLayoutCloseBtn = findViewById( R.id.close_add_layout );
        addNewLayoutFrame = findViewById( R.id.add_new_layout_view_RelLayout );

        addNewBannerSliderLayBtn = findViewById( R.id.add_banner_slider_layout_LinearLay );
        addNewProductsHrLayBtn = findViewById( R.id.add_horizontal_layout_LinearLay );
        addNewProductsGridLayBtn = findViewById( R.id.add_grid_layout_LinearLay );
        addNewBannerAdLayBtn = findViewById( R.id.add_banner_ad_layout_LinearLay );
        addNewStripAdLayBtn = findViewById( R.id.add_strip_ad_layout_LinearLay );

        addNewBannerSliderLayBtn.setOnClickListener( this );
        addNewProductsHrLayBtn.setOnClickListener( this );
        addNewProductsGridLayBtn.setOnClickListener( this );
        addNewBannerAdLayBtn.setOnClickListener( this );
        addNewStripAdLayBtn.setOnClickListener( this );

        // New Layout Action....
        addNewLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeUpEnable( false );
                addNewLayoutBtn.setClickable( false );
                addNewLayoutFrame.setVisibility( View.VISIBLE );
            }
        } );
        addNewLayoutCloseBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeUpEnable( true );
                addNewLayoutBtn.setClickable( true );
                addNewLayoutFrame.setVisibility( View.GONE );
            }
        } );
        // New Layout Action....
        // Add Total index in Main commonCatList...
        if (commonCatList.size() == 0) {
            for (int ind = 0; ind < DBquery.homeCategoryIconList.size(); ind++) {
                commonCatList.add( new ArrayList <CommonCatModel>() );
            }
        }

        // ----------- Recycler List View....
        commonCatListRecycler = findViewById( R.id.common_cat_recycler );

        LinearLayoutManager catListLayoutManager = new LinearLayoutManager( this );
        catListLayoutManager.setOrientation( RecyclerView.VERTICAL );
        commonCatListRecycler.setLayoutManager( catListLayoutManager );
        //--------------
        // Logic based on Index...To load data of Category layout
        if (commonCatList.get( index ).isEmpty()) {
            // download data of that category...
            dialog.show();
            DBquery.getQuerySetFragmentData( dialog, index, categoryTitle, this );
        }

//        // Temprary List for checking...
        commonCatAdaptor = new CommonCatAdaptor( commonCatList.get( index ), index, categoryTitle );
        commonCatListRecycler.setAdapter( commonCatAdaptor );
        commonCatAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (commonCatAdaptor != null){
            commonCatAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((id == android.R.id.home)) {
            finish();
            return true;
        } else

            return super.onOptionsItemSelected( item );
    }

    @Override
    public void onClick(View v) {
        if (v == addNewBannerSliderLayBtn) {
            gotoAddNewLayout( BANNER_SLIDER_LAYOUT_CONTAINER );
        }
        else if (v == addNewProductsHrLayBtn) {
            gotoAddNewLayout( HORIZONTAL_ITEM_LAYOUT_CONTAINER );
        }
        else if (v == addNewProductsGridLayBtn) {
            gotoAddNewLayout( GRID_ITEM_LAYOUT_CONTAINER );
        }
        else if (v == addNewBannerAdLayBtn) {
            gotoAddNewLayout( BANNER_AD_LAYOUT_CONTAINER );
        }
        else if (v == addNewStripAdLayBtn) {
            gotoAddNewLayout( STRIP_AD_LAYOUT_CONTAINER );
        }
    }

    private void gotoAddNewLayout(int layType) {
        homeUpEnable( true );
        addNewLayoutBtn.setClickable( true );
        addNewLayoutFrame.setVisibility( View.GONE );
        // start Activity...
        Intent intent = new Intent( this, AddNewLayoutActivity.class );
        intent.putExtra( "CAT_TITLE", categoryTitle );
        intent.putExtra( "CAT_INDEX", index );
        intent.putExtra( "LAY_TYPE", layType );
        intent.putExtra( "LAY_INDEX", -1 );
        intent.putExtra( "TASK_UPDATE", false );
        startActivity( intent );
    }

    private void homeUpEnable(boolean val) {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled( val );
            // For Recycler Layout...
            if (val) {
                commonCatListRecycler.setVisibility( View.VISIBLE );
            } else {
                commonCatListRecycler.setVisibility( View.INVISIBLE );
            }
        } catch (NullPointerException e) {
        }
    }


}


