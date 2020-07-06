package com.ecom.letsshop.admin.update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.catlayouts.BannerItemAdaptor;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;
import com.ecom.letsshop.admin.database.DBquery;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_BANNER_PRODUCTS;
import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_GRID_PRODUCTS;
import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_HORIZONTAL_PRODUCTS;
import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class ViewAllActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private RecyclerView recyclerProductLayout;
    private GridView gridProductLayout;
    public static List <String> totalProductsIdViewAll;
    public static List <HrLayoutItemModel> horizontalItemViewModelListViewAll;
    public static List <HrLayoutItemModel> gridViewModelListViewAll;
    public static List <BannerAndCatModel> bannerSliderListViewAll;
    private List<String> layoutIdList = new ArrayList <>();
    private List<Integer> layoutIndexList = new ArrayList <>();

    int catIndex;
    String catTitle;
    int listIndex;
    int layoutCode;
    String layoutId;
    public static HorizontalViewAllAdaptor horizontalItemViewAdaptor;
    private GridViewAllAdaptor gridViewAllAdaptor;

    DialogsClass dialogsClass = new DialogsClass( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_all );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            String layoutTitle = getIntent().getStringExtra( "TITLE" );
            getSupportActionBar().setTitle( layoutTitle );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        layoutCode = getIntent().getIntExtra( "LAYOUT_CODE", -1 );  // get layout code...
//        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );  // get layout code...
        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 ); // getting category index...
        listIndex = getIntent().getIntExtra( "LIST_INDEX", -1 ); //  getting index of current layout inside of category...
        catTitle = getIntent().getStringExtra( "CAT_TITLE" );

//        viewAllIntent.putExtra( "TITLE", layoutTitle ); // passing category title...

        // -- Recycler View...
        recyclerProductLayout = findViewById( R.id.view_all_layout_recycler );
        // ----------- Grid View....
        gridProductLayout = findViewById( R.id.view_all_layout_grid );


        layoutId = commonCatList.get( catIndex ).get( listIndex ).getLayoutID();
        layoutIndexList.clear();
        for (int i = 0; i < commonCatList.get( catIndex ).size(); i++){
            if (commonCatList.get( catIndex ).get( i ).getLayoutType() == layoutCode && i != listIndex){ // This is only for same layout...
                layoutIndexList.add( i );
                layoutIdList.add( commonCatList.get( catIndex ).get( i ).getLayoutID() );
            }
        }

        //-- condition...
        if (layoutCode == VIEW_ALL_FOR_HORIZONTAL_PRODUCTS){
            gridProductLayout.setVisibility( View.GONE );
            recyclerProductLayout.setVisibility( View.VISIBLE );
            setRecyclerProductLayout();
            if (totalProductsIdViewAll.size() > horizontalItemViewModelListViewAll.size()){
                horizontalItemViewModelListViewAll.clear();
                // Load Again Data...
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadProductDataAgain( totalProductsIdViewAll.get( i ));
                }
            }
        }else
            if (layoutCode == VIEW_ALL_FOR_GRID_PRODUCTS){
            recyclerProductLayout.setVisibility( View.GONE );
            gridProductLayout.setVisibility( View.VISIBLE );
            setGridProductLayout();
            if (totalProductsIdViewAll.size() > gridViewModelListViewAll.size()){
                gridViewModelListViewAll.clear();
                // Load Again Data...
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadProductDataAgain( totalProductsIdViewAll.get( i ));
                }
            }
        }else
            if(layoutCode == VIEW_ALL_FOR_BANNER_PRODUCTS){
            gridProductLayout.setVisibility( View.GONE );
            recyclerProductLayout.setVisibility( View.VISIBLE );
            setBannerSliderProductLayout();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
        if (layoutCode == VIEW_ALL_FOR_HORIZONTAL_PRODUCTS){
            horizontalItemViewAdaptor.notifyDataSetChanged();
        }
        if (layoutCode == VIEW_ALL_FOR_GRID_PRODUCTS){
            gridViewAllAdaptor.notifyDataSetChanged();
        }
//        if (layoutCode == VIEW_ALL_FOR_BANNER_PRODUCTS){
//            // Notify if updated banner...
//        }
    }


    private void setRecyclerProductLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerProductLayout.setLayoutManager( linearLayoutManager );
        horizontalItemViewAdaptor = new HorizontalViewAllAdaptor( horizontalItemViewModelListViewAll, layoutIndexList, layoutId, listIndex, catIndex, catTitle );
        recyclerProductLayout.setAdapter( horizontalItemViewAdaptor );
        horizontalItemViewAdaptor.notifyDataSetChanged();
    }

    private void setGridProductLayout(){
        gridViewAllAdaptor = new GridViewAllAdaptor( gridViewModelListViewAll, listIndex, catIndex, catTitle );
        gridProductLayout.setAdapter( gridViewAllAdaptor );
        gridViewAllAdaptor.notifyDataSetChanged();
    }

    private void setBannerSliderProductLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerProductLayout.setLayoutManager( linearLayoutManager );
        // Set Adaptor...
        BannerItemAdaptor bannerSliderViewAllAdaptor = new BannerItemAdaptor( bannerSliderListViewAll, true, listIndex
                , catIndex, DBquery.homeCategoryIconList.get( catIndex ).getTitleOrBgColor());
        recyclerProductLayout.setAdapter( bannerSliderViewAllAdaptor );
        bannerSliderViewAllAdaptor.notifyDataSetChanged();
    }

    private void loadProductDataAgain( final String productId ){
        firebaseFirestore.collection( "PRODUCTS" ).document( productId )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // access the banners from database...
                    if (layoutCode == VIEW_ALL_FOR_HORIZONTAL_PRODUCTS){
                        horizontalItemViewModelListViewAll.add( new HrLayoutItemModel( productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (long) task.getResult().get( "product_stocks" )
                                , (Boolean) task.getResult().get( "product_cod" ) ) );
                        commonCatList.get( catIndex ).get( listIndex ).setHrAndGridProductsDetailsList( horizontalItemViewModelListViewAll );
                        horizontalItemViewAdaptor.notifyDataSetChanged();
                    }else
                        if(layoutCode == VIEW_ALL_FOR_GRID_PRODUCTS){
                        gridViewModelListViewAll.add( new HrLayoutItemModel( productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (long) task.getResult().get( "product_stocks" )
                                , (Boolean) task.getResult().get( "product_cod" ) ) );
                        commonCatList.get( catIndex ).get( listIndex ).setHrAndGridProductsDetailsList( gridViewModelListViewAll );
                        gridViewAllAdaptor.notifyDataSetChanged();
                    }

                }
                else{
                    String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                }
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate( R.menu.menu_show_in_product_details,menu);
        // Check First whether any item in cart or not...
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else

        return super.onOptionsItemSelected( item );
    }


}

