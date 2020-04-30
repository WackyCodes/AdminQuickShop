package com.example.shailendra.admin;

import androidx.annotation.NonNull;

import com.example.shailendra.admin.home.AreaCodeAndName;
import com.example.shailendra.admin.userprofile.AdminData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StaticValues {

    public static final String APP_VERSION = "a-1-01-1";

    // User Static Variables....

    public static List<String> cityNameList = new ArrayList <>();
    //    public static List<AreaCodeAndName> areaNameList = new ArrayList <>();
    public static List<AreaCodeAndName> areaCodeAndNameList = new ArrayList <>();
    public static List <String> areaNameList = new ArrayList <>();
    public static String userCityName = null;
    public static String userAreaCode = null;
    public static String userAreaName = null;
    public static String tempProductAreaCode = null;

    //Database References...
    public static DocumentReference getFirebaseDocumentReference(@NonNull String cityName, @NonNull String areaCode){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection( "ADMIN_PER" )
                .document( cityName.toUpperCase() ).collection( "SUB_LOCATION" ).document( areaCode );
        return documentReference;
    }

//    public static
    public static final AdminData adminData = new AdminData();
    // Admin Type Static Value...
    public static final String TYPE_FOUNDER = "A";
    public static final String TYPE_PRODUCT_MANAGER = "B";
    public static final String TYPE_DELIVERY_BOY = "C";
    public static final String TYPE_PERMISSION_DENIED = "D";

    public static final int STORAGE_PERM = 1;
    // Final static values...
    public static final int NOTIFY_ORDER_REQUEST = 141;
    public static final int NOTIFY_OUT_OF_STOCK = 142;
    public static final int NOTIFY_PROBLEM_REPORT = 143;
    public static final int NOTIFY_SUCCESS_DELIVERED = 144;

    // Common Main Home Container...
    public static final int BANNER_SLIDER_LAYOUT_CONTAINER = 0;
    public static final int STRIP_AD_LAYOUT_CONTAINER = 1;
    public static final int HORIZONTAL_ITEM_LAYOUT_CONTAINER = 2;
    public static final int GRID_ITEM_LAYOUT_CONTAINER = 3;
    public static final int BANNER_AD_LAYOUT_CONTAINER = 4;
    public static final int CAT_ITEM_LAYOUT_CONTAINER = 5;
    public static final int BANNER_SLIDER_CONTAINER_ITEM = 6;
    public static final int ADD_NEW_PRODUCT_ITEM = 7;
    public static final int ADD_NEW_STRIP_AD_LAYOUT = 8;
    public static final int ADD_NEW_HORIZONTAL_ITEM_LAYOUT = 9;
    public static final int ADD_NEW_GRID_ITEM_LAYOUT = 10;

    // View All Layout...
    public static final int VIEW_ALL_FOR_GRID_PRODUCTS = 11;
    public static final int VIEW_ALL_FOR_HORIZONTAL_PRODUCTS = 12;
    public static final int VIEW_ALL_FOR_BANNER_PRODUCTS = 13;

    public static final int SIGN_IN_FRAGMENT =14;
    public static final int SIGN_UP_FRAGMENT =15;
    public static final int ORDER_LIST_FRAGMENT =16;
    public static final int MEMBER_LIST_FRAGMENT =17;
    public static final int ADD_MEMBER_FRAGMENT =18;
    public static final int MAIN_FRAGMENT =19;

    public static final int GALLERY_CODE = 121;
    public static final int READ_EXTERNAL_MEMORY_CODE = 122;

    // Other Values....
    public static final int ID_UPDATE = 51;
    public static final int ID_DELETE = 52;
    public static final int ID_CLICK = 53;
    public static final int ID_MOVE = 54;
    public static final int ID_COPY = 55;

    // Making A Empty Product List...
    public static List<String> emptyProductIdList = new ArrayList <>();

    // create a random order ID of 10 Digits...
    public static String getRandomOrderID(){

        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(100);
        }while ( rand_int1 <= 0 );

        if (rand_int1 < 99){
            rand_int1 = rand_int1*100;
            randNum = String.valueOf( rand_int1 );
            randNum = randNum.substring( 0, 2 );
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
        String random12 = simpleDateFormat.format(new Date()) + randNum;

        return random12;

    }


    /// ===============

    public static int UPDATE_P_LAY_INDEX;
    public static int UPDATE_P_CAT_INDEX;
    public static String UPDATE_PRODUCT_CAT = null;

}
