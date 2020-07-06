package com.ecom.letsshop.admin.database;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.MainActivity;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;
import com.ecom.letsshop.admin.home.CommonCatModel;
import com.ecom.letsshop.admin.home.MainAdaptor;
import com.ecom.letsshop.admin.home.MainFragment;
import com.ecom.letsshop.admin.notifications.NotificationsActivity;
import com.ecom.letsshop.admin.notifications.NotificationsModel;
import com.ecom.letsshop.admin.order.NewOrder;
import com.ecom.letsshop.admin.order.OrderDetailsActivity;
import com.ecom.letsshop.admin.order.OrderModel;
import com.ecom.letsshop.admin.order.OrderProductModel;
import com.ecom.letsshop.admin.userprofile.AdminData;
import com.ecom.letsshop.admin.userprofile.AdminMemberList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.DialogsClass.isInternetConnect;
import static com.ecom.letsshop.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.NOTIFY_ORDER_REQUEST;
import static com.ecom.letsshop.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.tempProductAreaCode;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.home.CommonCatActivity.commonCatAdaptor;
import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class DBquery {

    // Private Temp Variables...
    private static int tempIndex = 0;
    private static String dbTempListItem;
    private static String dbTempListItem2;

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser =  firebaseAuth.getCurrentUser();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public static List<NotificationsModel> notificationModelList = new ArrayList <>();
    private static ListenerRegistration notificationLR;

    // Home screen Category ...
    public static List<BannerAndCatModel> homeCategoryIconList = new ArrayList <>();
    // Home Fragment model List...
//    public static List<HomeFragmentModel> homeFragmentModelList = new ArrayList <>();

//    public static List <String> homeCategoryListName = new ArrayList <>();
//    public static List <List <HomeFragmentModel>> homeCategoryList = new ArrayList <>();

    // Banner Slider List...
    private static List<BannerAndCatModel> bannerSliderList;
    private static List<HrLayoutItemModel> hrAndGridProductList;

    public static List<OrderModel> orderModelList = new ArrayList <>();

    public static List<AdminData> adminMemberList = new ArrayList <>();

    // Main List...
    //         commonCatList.get( indexNo ).add( new CommonCatModel( BANNER_SLIDER_LAYOUT_CONTAINER, bannerList ) );
    // Query to Load category Icons....
    public static void getQueryCategoryIcon(final Dialog dialog, final RecyclerView recyclerView ) {
        homeCategoryIconList.clear();
        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task <QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            homeCategoryIconList.add( new BannerAndCatModel( documentSnapshot.get( "icon" ).toString(), documentSnapshot.get( "categoryName" ).toString() ) );
                            commonCatList.add( new ArrayList <CommonCatModel>() );

//                            if (homeCatAdaptor != null){
//                                homeCatAdaptor.notifyDataSetChanged();
//                            }
                        }

                        // Adaptor...
                        if (recyclerView != null){
                            MainFragment.homeCatAdaptor = new MainAdaptor( homeCategoryIconList );
                            recyclerView.setAdapter( MainFragment.homeCatAdaptor );
                            MainFragment.homeCatAdaptor.notifyDataSetChanged();
                        }
                        if (MainFragment.homeSwipeRefreshLayout != null){
                            MainFragment.homeSwipeRefreshLayout.setRefreshing( false );
                        }

                        dialog.dismiss();
                    } else {
//                    showToast( task.getException().getMessage() );
                        dialog.dismiss();
                    }
                }
            } );

        }else{
            dialog.dismiss();
        }
    }


    // Query to Load Fragment Data like homepage items etc...
    public static void getQuerySetFragmentData(final Dialog dialog, final int index, String catName, Context context) {

        // Set Home Data.... Loading Category Data...!!
        String categoryName = catName.toUpperCase();
//        String categoryName = "MOBILES";

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( categoryName.toUpperCase() )
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

        }else{
            dialog.dismiss();
        }
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

    public static void getMemberListQuery(final String cityName, final Dialog dialog){
        adminMemberList.clear();

        firebaseFirestore.collection( "ADMIN_PER" ).document( cityName.toUpperCase() )
                .collection( "PERMISSION" )
                .orderBy( "id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        Boolean adminPermission = documentSnapshot.getBoolean( "permission" );
                        String name = documentSnapshot.get( "name" ).toString();
                        String photo = documentSnapshot.get( "photo" ).toString();
                        String type = documentSnapshot.get( "type" ).toString();
                        String email = documentSnapshot.get( "email" ).toString();
                        String mobile = documentSnapshot.get( "mobile" ).toString();
                        long id = (long)documentSnapshot.get( "id" );
                        String authId = documentSnapshot.get( "auth_id" ).toString();

                        String address = documentSnapshot.get( "address" ).toString();
                        String addPin = documentSnapshot.get( "add_pin" ).toString();
                        String areaCode = documentSnapshot.get( "area_code" ).toString();
//                        String ciyName = documentSnapshot.get( "area_city" ).toString();
                        String ciyName = cityName;
//                    String areaCode = areaCode;

//                    (String adminName, String adminMobile, String adminEmail, boolean adminPermission, String adminType, String adminPhoto,
//                            String adminAddress, String adminAddPin, String adminId, String adminAuthId,
//                            String adminCityName, String adminAreaCode) {

                        adminMemberList.add( new AdminData( name, mobile, email, adminPermission, type,
                                photo, address, addPin, String.valueOf( id ),
                                authId, ciyName, areaCode  ) );

                        if (AdminMemberList.adminMemberAdaptor != null){
                            AdminMemberList.adminMemberAdaptor.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        } );


    }

    //////////////////// ---- Order Query ----///////////////////////

    /*
     *          (1) WAITING : when order is not accepted by admin...
     *          (2) ACCEPTED : when order has been accepted...
     *          (3) PROCESS : When order is in process to delivery...
     *          (4) CANCELLED : When Order has been cancelled by user...
     *          (5) SUCCESS : when order has been delivered successfully...
     *          (6) FAILED : when PayMode Online and payment has been failed...
     *          (7) PENDING : when Payment is Pending
     */

    public static void getOrderListQuery( ){

        firebaseFirestore.collection( "COM_ORDERS" ).orderBy( "order_id" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Create a product List...

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        List<OrderProductModel> productModelList = new ArrayList <>();

                        long no_of_product = (long)documentSnapshot.get( "no_of_products" );

                        for (long i = 0; i < no_of_product; i++){
                            productModelList.add( new OrderProductModel(
                                    documentSnapshot.get( "product_id_"+i ).toString(),
                                    documentSnapshot.get( "product_img_"+i ).toString(),
                                    documentSnapshot.get( "product_name_"+i ).toString(),
                                    documentSnapshot.get( "product_price_"+i ).toString(),
                                    documentSnapshot.get( "product_qty_"+i ).toString()
                            ) );
                            if (i == no_of_product - 1){
                                if (documentSnapshot.get( "delivery_status" ).toString().equals( "ACCEPTED" )){
                                    orderModelList.add( 0, new OrderModel(
                                            documentSnapshot.get( "order_id" ).toString(),
                                            documentSnapshot.get( "user_phone" ).toString(),
                                            documentSnapshot.get( "order_by" ).toString(),
                                            documentSnapshot.get( "bill_amount" ).toString(),
                                            documentSnapshot.get( "delivery_charge" ).toString(),
                                            documentSnapshot.get( "pay_mode" ).toString(),
                                            documentSnapshot.get( "order_delivered_name" ).toString(),
                                            documentSnapshot.get( "order_delivery_address" ).toString(),
                                            documentSnapshot.get( "order_delivery_pin" ).toString(),
                                            documentSnapshot.get( "delivery_status" ).toString(),
                                            documentSnapshot.get( "order_date_day" ).toString(),
                                            documentSnapshot.get( "order_time" ).toString(),
                                            no_of_product,
                                            productModelList
                                    ) );
                                }
                                else{
                                    orderModelList.add( new OrderModel(
                                            documentSnapshot.get( "order_id" ).toString(),
                                            documentSnapshot.get( "user_phone" ).toString(),
                                            documentSnapshot.get( "order_by" ).toString(),
                                            documentSnapshot.get( "bill_amount" ).toString(),
                                            documentSnapshot.get( "delivery_charge" ).toString(),
                                            documentSnapshot.get( "pay_mode" ).toString(),
                                            documentSnapshot.get( "order_delivered_name" ).toString(),
                                            documentSnapshot.get( "order_delivery_address" ).toString(),
                                            documentSnapshot.get( "order_delivery_pin" ).toString(),
                                            documentSnapshot.get( "delivery_status" ).toString(),
                                            documentSnapshot.get( "order_date_day" ).toString(),
                                            documentSnapshot.get( "order_time" ).toString(),
                                            no_of_product,
                                            productModelList
                                    ) );
                                }

                                if (NewOrder.orderListAdaptor!=null){
                                    NewOrder.orderListAdaptor.notifyDataSetChanged();
                                }

                            }
                        }

                    }

                }else{

                }
            }
        } );

    }

    public static void addOneInOrderListQuery(String orderId, final Context context, final boolean isViewRequest ){
        final Dialog dialog = new DialogsClass().progressDialog( context );
        if (isViewRequest){
            dialog.show();
        }
        firebaseFirestore.collection( "COM_ORDERS" )
                .document( orderId ).get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            List<OrderProductModel> productModelList = new ArrayList <>();
                            long no_of_product = (long)task.getResult().get( "no_of_products" );
                            for (long i = 0; i < no_of_product; i++){
                                productModelList.add( new OrderProductModel(
                                        task.getResult().get( "product_id_"+i ).toString(),
                                        task.getResult().get( "product_img_"+i ).toString(),
                                        task.getResult().get( "product_name_"+i ).toString(),
                                        task.getResult().get( "product_price_"+i ).toString(),
                                        task.getResult().get( "product_qty_"+i ).toString()
                                ) );
                                if (i == no_of_product - 1){

                                    orderModelList.add( 0, new OrderModel(
                                            task.getResult().get( "order_id" ).toString(),
                                            task.getResult().get( "user_phone" ).toString(),
                                            task.getResult().get( "order_by" ).toString(),
                                            task.getResult().get( "bill_amount" ).toString(),
                                            task.getResult().get( "delivery_charge" ).toString(),
                                            task.getResult().get( "pay_mode" ).toString(),
                                            task.getResult().get( "order_delivered_name" ).toString(),
                                            task.getResult().get( "order_delivery_address" ).toString(),
                                            task.getResult().get( "order_delivery_pin" ).toString(),
                                            task.getResult().get( "delivery_status" ).toString(),
                                            task.getResult().get( "order_date_day" ).toString(),
                                            task.getResult().get( "order_time" ).toString(),
                                            no_of_product,
                                            productModelList
                                    ) );
                                    dialog.dismiss();
                                    if (isViewRequest){
                                        Intent intent = new Intent( context, OrderDetailsActivity.class );
                                        intent.putExtra( "INDEX", 0 );
                                        context.startActivity( intent );
                                    }
                                }
                            }

                        }else{
                            dialog.dismiss();
                        }
                    }
                } );
    }

    public static void updateOrderStatusQuery(final Context context, final Dialog dialog, final String orderId,
                                              Map <String, Object> updateDataMap, final String notifyUserId ){

        firebaseFirestore.collection( "COM_ORDERS" )
                .document( orderId ).update( updateDataMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText( context, "Order Delivered Successfully.!", Toast.LENGTH_SHORT ).show();

                    Map <String, Object> userData = new HashMap <>();
                    userData.put( "notify_type", "ORDER_UPDATE" );
                    userData.put( "notify_id", StaticValues.getRandomOrderID() );
                    userData.put( "notify_image", " " );
                    userData.put( "notify_order_id", orderId );
                    userData.put( "notify_heading", "Delivered.!" );
                    userData.put( "notify_text", "Your order has been Delivered.!" );
                    userData.put( "notify_read", false );

                    DBquery.queryToNotifyUser( context, null, notifyUserId, userData );
                    if (NewOrder.orderListAdaptor!=null){
                        NewOrder.orderListAdaptor.notifyDataSetChanged();
                    }
                }else{
                    dialog.dismiss();
                }
            }
        } );
    }

    //////////////////// ---- Order Query ----///////////////////////

    // Query To update Notifications...
    public static void updateNotificationQuery( Context context, boolean remove ){

     /*   delivery Status :
     *          (1) WAITING : when order is not accepted by admin...
     *          (2) ACCEPTED : when order has been accepted...
     *          (3) PROCESS : When order is in process to delivery...
     *          (4) CANCELLED : When Order has been cancelled by user...
     *          (5) SUCCESS : when order has been delivered successfully...
     *          (6) FAILED : when PayMode Online and payment has been failed...
     *          (7) PENDING : when Payment is Pending
     */

        if (remove ){
            if ( notificationLR != null )
                notificationLR.remove();
        }
        else{
            if (isInternetConnect( context )){

                notificationLR = firebaseFirestore.collection("COM_ORDERS").orderBy( "order_id" )
                        .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null ){
                            // TODO: Query to update notification...

                            int notifyCount = 0;
                            notificationModelList.clear();

//                          int notifyType, String notifyImage, String headText, String subHeadingText)
                            for ( int x = 0; x < queryDocumentSnapshots.getDocuments().size(); x++ ){
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get( x );
                                if( documentSnapshot.get( "delivery_status" ).toString().equals( "WAITING" ) ){
                                    notificationModelList.add(
                                            new NotificationsModel( NOTIFY_ORDER_REQUEST,
                                                    documentSnapshot.get( "order_id" ).toString(),
                                                    documentSnapshot.get( "order_by" ).toString())
                                    );
                                    notifyCount = notifyCount + 1;
                                }
                            }

                            if (MainActivity.badgeNotifyCount != null){
                                if (notifyCount > 0){
                                    MainActivity.badgeNotifyCount.setVisibility( View.VISIBLE );
                                    MainActivity.badgeNotifyCount.setText( String.valueOf( notifyCount ) );
                                }else{
                                    MainActivity.badgeNotifyCount.setVisibility( View.INVISIBLE );
                                }
                            }

                            if(NotificationsActivity.notificationsAdaptor!=null){
                                NotificationsActivity.notificationsAdaptor.notifyDataSetChanged();
                            }

                        }
                    }
                } );

//                        notificationLR = firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
//                        .collection( "USER_DATA" ).document( "MY_NOTIFICATION" )
//                        .addSnapshotListener( new EventListener <DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                                if (documentSnapshot != null && documentSnapshot.exists()){
//                                    notificationModelList.clear();
//                                    int notifyCount = 0;
////                                    int notifyType, String notifyImage, String headText, String subHeadingText)
//                                    for ( long x = 0; x < (long) documentSnapshot.get( "notification_size" ); x++ ){
//                                        notificationModelList.add( 0, new NotificationModel(
//                                                documentSnapshot.get( "noti_img_"+x ).toString(),
//                                                documentSnapshot.get( "noti_text_"+x ).toString(),
//                                                (boolean)documentSnapshot.get( "noti_read_"+x )
//                                        ) );
//                                        if (!documentSnapshot.getBoolean( "noti_read_"+x )){
//                                            notifyCount = notifyCount + 1;
//                                        }
//                                    }
//                                    if (MainActivity.badgeNotifyCount != null){
//                                        if (notifyCount > 0){
//                                            MainActivity.badgeNotifyCount.setVisibility( View.VISIBLE );
//                                            MainActivity.badgeNotifyCount.setText( String.valueOf( notifyCount ) );
//                                        }else{
//                                            MainActivity.badgeNotifyCount.setVisibility( View.INVISIBLE );
//                                        }
//                                    }
//                                    if (NotificationActivity.notificationAdaptor != null ){
//                                        NotificationActivity.notificationAdaptor.notifyDataSetChanged();
//                                    }
//                                }
//                            }
//                        } );

            }
        }
    }


    // Notify User....
    public static void queryToNotifyUser(Context context,@Nullable final Dialog dialog, String notifyUserId, Map <String, Object> notifyMap){

        String notify_id = notifyMap.get( "notify_id" ).toString();

        firebaseFirestore.collection( "USER" ).document( notifyUserId )
                .collection( "USER_NOTIFICATION" )
                .document( notify_id ).set( notifyMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        } );

    }

    public static void queryToUpdateAdminData(@Nullable final Dialog dialog, String adminCityName, String adminMobileID, Map<String, Object> updateMap){

        FirebaseFirestore.getInstance().collection( "ADMIN_PER" )
                .document( adminCityName.toUpperCase() )
                .collection( "PERMISSION" ).document( adminMobileID )
                .update( updateMap ).addOnCompleteListener( new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if (dialog!= null){
                    dialog.dismiss();
                }
                if (task.isSuccessful()){
                    // Successful Updates...
                }else{
                    // Failed To Update...
                }
            }
        } );

    }


}
