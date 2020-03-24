package com.example.shailendra.admin.database;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shailendra.admin.catlayouts.BannerAndCatModel;
import com.example.shailendra.admin.catlayouts.HrLayoutItemModel;
import com.example.shailendra.admin.home.CommonCatModel;
import com.example.shailendra.admin.home.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shailendra.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.example.shailendra.admin.home.CommonCatActivity.commonCatAdaptor;
import static com.example.shailendra.admin.home.MainFragment.commonCatList;

public class DBquery {

    // Private Temp Variables...
    private static int tempIndex = 0;
    private static String dbTempListItem;
    private static String dbTempListItem2;

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser =  firebaseAuth.getCurrentUser();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // Home screen Category ...
    public static List<BannerAndCatModel> homeCategoryIconList = new ArrayList <>();
    // Home Fragment model List...
//    public static List<HomeFragmentModel> homeFragmentModelList = new ArrayList <>();

//    public static List <String> homeCategoryListName = new ArrayList <>();
//    public static List <List <HomeFragmentModel>> homeCategoryList = new ArrayList <>();

    // Banner Slidder List...
    private static List<BannerAndCatModel> bannerSliderList;
    private static List<HrLayoutItemModel> hrAndGridProductList;


    // Main List...
    //         commonCatList.get( indexNo ).add( new CommonCatModel( BANNER_SLIDER_LAYOUT_CONTAINER, bannerList ) );
    // Query to Load category Icons....
    public static void getQueryCategoryIcon( final Dialog dialog ) {
        homeCategoryIconList.clear();

        firebaseFirestore.collection( "CATEGORIES" ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        homeCategoryIconList.add( new BannerAndCatModel( documentSnapshot.get( "icon" ).toString(), documentSnapshot.get( "categoryName" ).toString() ) );
                        commonCatList.add( new ArrayList <CommonCatModel>() );
                    }
                    if (MainFragment.homeCatAdaptor != null){
                        MainFragment.homeCatAdaptor.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                } else {
//                    showToast( task.getException().getMessage() );
                    dialog.dismiss();
                }
            }
        } );
    }


    // Query to Load Fragment Data like homepage items etc...
    public static void getQuerySetFragmentData(final Dialog dialog, final int index, String catName, Context context) {

        // Set Home Data.... Loading Category Data...!!
//        String categoryName = catName.toUpperCase();
        String categoryName = "MOBILES";

        firebaseFirestore.collection( "CATEGORIES" ).document( categoryName.toUpperCase() )
                .collection( "LAYOUTS" ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((long) documentSnapshot.get( "view_type" ) == BANNER_SLIDER_LAYOUT_CONTAINER) {
                            // TODO : add banners
                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            bannerSliderList = new ArrayList <>();
                            long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );
                            for (long i = 1; i < no_of_banners + 1; i++) {
                                // access the banners from database...
                                bannerSliderList.add( new BannerAndCatModel( documentSnapshot.get( "banner_" + i ).toString(),
                                        documentSnapshot.get( "banner_" + i + "_bg" ).toString() ) );
                            }
                            // add the banners list in the home recycler list...
                            commonCatList.get( index ).add( new CommonCatModel( BANNER_SLIDER_LAYOUT_CONTAINER, layout_id, bannerSliderList ) );
                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == STRIP_AD_LAYOUT_CONTAINER) {
                            // TODO : for strip ad
                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            commonCatList.get( index ).add( new CommonCatModel( STRIP_AD_LAYOUT_CONTAINER, layout_id,
                                    documentSnapshot.get( "strip_ad" ).toString(), documentSnapshot.get( "strip_bg" ).toString() ) );
                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == HORIZONTAL_ITEM_LAYOUT_CONTAINER) {
                            // TODO : for horizontal products
                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            hrAndGridProductList = new ArrayList <>();
                            List<String> hrAndGridProductIdList = new ArrayList <>();
                            long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                            for (long i = 1; i < no_of_products + 1; i++) {
                                // Load Product Data List After set Adaptor... on View Time...
                                // Below we load only product Id...
                                hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                            }
                            // add list in home fragment model
                            // TODO : To User
//                            if (hrAndGridProductIdList.size()>=3){
//                                commonCatList.get( index ).add( new CommonCatModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, documentSnapshot.get( "layout_title" ).toString()
//                                        , hrAndGridProductIdList, hrAndGridProductList ) );
//                            }
                            // TODO : To Admin
                            commonCatList.get( index ).add( new CommonCatModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, layout_id
                                    , documentSnapshot.get( "layout_title" ).toString()
                                    , hrAndGridProductIdList, hrAndGridProductList ) );

                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == GRID_ITEM_LAYOUT_CONTAINER) {
                            // TODO : for grid products
                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            hrAndGridProductList = new ArrayList <>();
                            List<String> hrAndGridProductIdList = new ArrayList <>();
                            long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                            for (long i = 1; i < no_of_products + 1; i++) {
                                // access the banners from database...
                                hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                            }
                            // add list in home fragment model
                            // TODO : To User
//                            if (hrAndGridProductIdList.size()>=4){
//                                commonCatList.get( index ).add( new CommonCatModel( GRID_ITEM_LAYOUT_CONTAINER, documentSnapshot.get( "layout_title" ).toString()
//                                        , hrAndGridProductIdList, hrAndGridProductList ) );
//                            }
                            // TODO : To Admin
                            commonCatList.get( index ).add( new CommonCatModel( GRID_ITEM_LAYOUT_CONTAINER, layout_id
                                    , documentSnapshot.get( "layout_title" ).toString()
                                    , hrAndGridProductIdList, hrAndGridProductList ) );
                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == BANNER_AD_LAYOUT_CONTAINER) {
                            // TODO : for Banner ad
                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            commonCatList.get( index ).add( new CommonCatModel( BANNER_AD_LAYOUT_CONTAINER, layout_id,
                                    documentSnapshot.get( "banner_ad" ).toString(), documentSnapshot.get( "banner_bg" ).toString() ) );
                        }
                    }
                    if (commonCatAdaptor != null){
                        commonCatAdaptor.notifyDataSetChanged();
                    }
//                    HomeFragmentAdaptor homeFragmentAdaptor = new HomeFragmentAdaptor( homeCategoryList.get( index ) );
//                    homeLayoutContainerRecycler.setAdapter( homeFragmentAdaptor );
//                    homeFragmentAdaptor.notifyDataSetChanged();
//                    HomeFragment.homeSwipeRefreshLayout.setRefreshing( false );
                    dialog.dismiss();

                }
                else {
                    // showToast( task.getException().getMessage() );
                    dialog.dismiss();
                }
            }
        } );

        // Set Home Data....

    }

    public static void loadProductDataById(String productID){

        firebaseFirestore.collection( "PRODUCTS" ).document( productID ).get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                }
            }
        } );

    }

    public static boolean addNewProduct(){
        Map <String, Object> layoutMap = new HashMap <>();
        layoutMap.put( "index", 2 );
        layoutMap.put( "no_of_banners", 0 );
        layoutMap.put( "view_type", 1 );

        firebaseFirestore.collection( "PRODUCTS" ).add( layoutMap )
                .addOnCompleteListener( new OnCompleteListener <DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentReference> task) {
                        if (task.isSuccessful()){
                            // Shift to add product details...
                            String docId = task.getResult().getId();

                        }else{
                            // Show Toast..  to try again...
                        }
                    }
                } );
        return true;
    }

}
