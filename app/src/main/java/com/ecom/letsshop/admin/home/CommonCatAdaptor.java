package com.ecom.letsshop.admin.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.letsshop.admin.DialogsClass;
import com.ecom.letsshop.admin.StaticMethods;
import com.ecom.letsshop.admin.StaticValues;
import com.ecom.letsshop.admin.addnewitem.AddNewLayoutActivity;
import com.ecom.letsshop.admin.addnewitem.AddNewProductActivity;
import com.ecom.letsshop.admin.catlayouts.BannerAndCatModel;
import com.ecom.letsshop.admin.catlayouts.BannerItemAdaptor;
import com.ecom.letsshop.admin.catlayouts.CatItemAdaptor;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemAdaptor;
import com.ecom.letsshop.admin.catlayouts.HrLayoutItemModel;
import com.ecom.letsshop.admin.database.UpdateImages;
import com.ecom.letsshop.admin.productdetails.ProductDetails;
import com.ecom.letsshop.admin.update.ViewAllActivity;
import com.example.shailendra.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.letsshop.admin.StaticValues.BANNER_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.CAT_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_BANNER_PRODUCTS;
import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_GRID_PRODUCTS;
import static com.ecom.letsshop.admin.StaticValues.VIEW_ALL_FOR_HORIZONTAL_PRODUCTS;
import static com.ecom.letsshop.admin.StaticValues.tempProductAreaCode;
import static com.ecom.letsshop.admin.StaticValues.userCityName;
import static com.ecom.letsshop.admin.home.MainFragment.commonCatList;

public class CommonCatAdaptor extends RecyclerView.Adapter {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DialogsClass dialogsClass = new DialogsClass();

    private int catType; // category index
    private String catTitle; // category name or title
    private List <CommonCatModel> commonCatModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public CommonCatAdaptor(List <CommonCatModel> commonCatModelList, int catType, String catTitle ) {
        this.commonCatModelList = commonCatModelList;
        this.catType = catType;
        this.catTitle = catTitle;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (commonCatModelList.get( position ).getLayoutType()) {
            case CAT_ITEM_LAYOUT_CONTAINER:        //-- 0
                return CAT_ITEM_LAYOUT_CONTAINER;
            case BANNER_SLIDER_LAYOUT_CONTAINER:           //-- 1
                return BANNER_SLIDER_LAYOUT_CONTAINER;
            case STRIP_AD_LAYOUT_CONTAINER:         //-- 2
                return STRIP_AD_LAYOUT_CONTAINER;
            case BANNER_AD_LAYOUT_CONTAINER:         //-- 3
                return BANNER_AD_LAYOUT_CONTAINER;
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:     //-- 4
                return HORIZONTAL_ITEM_LAYOUT_CONTAINER;
            case GRID_ITEM_LAYOUT_CONTAINER:            //-- 5
                return GRID_ITEM_LAYOUT_CONTAINER;
                // Add New Items...
//            case ADD_NEW_BANNER_SLIDER_LAYOUT:          //-- 6
//                return ADD_NEW_BANNER_SLIDER_LAYOUT;
//            case ADD_NEW_BANNER_AD_LAYOUT:               //-- 7
//                return ADD_NEW_BANNER_AD_LAYOUT;
//            case ADD_NEW_STRIP_AD_LAYOUT:                //-- 8
//                return ADD_NEW_STRIP_AD_LAYOUT;
//            case ADD_NEW_HORIZONTAL_ITEM_LAYOUT:        //-- 9
//                return ADD_NEW_HORIZONTAL_ITEM_LAYOUT;
//            case ADD_NEW_GRID_ITEM_LAYOUT:              //-- 10
//                return ADD_NEW_GRID_ITEM_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CAT_ITEM_LAYOUT_CONTAINER:
                // TODO : Category Items
                View catView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_view_layout, parent, false );
                return new CatViewHolder( catView );
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                // TODO : banner Items
                View bannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_view_layout, parent, false );
                return new BannerSliderViewHolder( bannerView );
            case STRIP_AD_LAYOUT_CONTAINER:
                // TODO : Strip ad Layout
                View stripAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.strip_ad_item_layout, parent, false );
                return new StripAdViewHolder( stripAdView );
            case BANNER_AD_LAYOUT_CONTAINER:
                // TODO : banner ad Layout
                View bannerAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.banner_ad_item_layout, parent, false );
                return new BannerAdViewHolder( bannerAdView );
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                // TODO : Horizontal viewHolder
                View horizontalLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_view_layout, parent, false );
                return new HorizontalLayoutViewHolder( horizontalLayoutView );
            case GRID_ITEM_LAYOUT_CONTAINER:
                // TODO : GridLayout viewHolder
                View gridLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_layout, parent, false );
                return new GridLayoutViewHolder( gridLayoutView );
//                // Add New Item View...
//            case ADD_NEW_BANNER_SLIDER_LAYOUT:          //-- 6
//                View newBannerSliderView = LayoutInflater.from( parent.getContext() ).inflate(
//                        R.layout.add_item_banner_layout, parent, false );
//                return new AddNewLayoutViewHolder( newBannerSliderView );
//            case ADD_NEW_BANNER_AD_LAYOUT:               //-- 7
//                View newBannerAdView = LayoutInflater.from( parent.getContext() ).inflate(
//                        R.layout.add_item_banner_layout, parent, false );
//                return new AddNewLayoutViewHolder( newBannerAdView );
//            case ADD_NEW_STRIP_AD_LAYOUT:                //-- 8
//                View newStipAdView = LayoutInflater.from( parent.getContext() ).inflate(
//                        R.layout.add_item_rectangle_layout, parent, false );
//                return new AddNewLayoutViewHolder( newStipAdView );
//            case ADD_NEW_HORIZONTAL_ITEM_LAYOUT:        //-- 9
//                View newHorizontalLayView = LayoutInflater.from( parent.getContext() ).inflate(
//                        R.layout.grid_view_layout, parent, false );
//                return new AddNewLayoutViewHolder( newHorizontalLayView );
//            case ADD_NEW_GRID_ITEM_LAYOUT:              //-- 10
//                View newGridLayView = LayoutInflater.from( parent.getContext() ).inflate(
//                        R.layout.grid_view_layout, parent, false );
//                return new AddNewLayoutViewHolder( newGridLayView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Layout index num is actually : Position... here...
        switch (commonCatModelList.get( position ).getLayoutType()) {
            case CAT_ITEM_LAYOUT_CONTAINER:
                List<BannerAndCatModel> catItemList = commonCatModelList.get( position ).getBannerAndCatModelList();
                ((CatViewHolder) holder).setCatData( catItemList, "Total Category:" , position);
                break;

            case BANNER_SLIDER_LAYOUT_CONTAINER:
                List <BannerAndCatModel> bannerSliderModelList =
                        commonCatModelList.get( position ).getBannerAndCatModelList();
                ((BannerSliderViewHolder) holder).setBannerData( bannerSliderModelList, "Total Banner:", position );
                break;

            case STRIP_AD_LAYOUT_CONTAINER:
                String stripAdImg = commonCatModelList.get( position ).getStripAndBannerAdImg();
                String stripAdBg = commonCatModelList.get( position ).getStripAndBannerAdBg();
                ((StripAdViewHolder)holder).setStripAdData( stripAdImg, stripAdBg, position );
                break;

            case BANNER_AD_LAYOUT_CONTAINER:
                String bannerAdImg = commonCatModelList.get( position ).getStripAndBannerAdImg();
                String bannerAdBg = commonCatModelList.get( position ).getStripAndBannerAdBg();
                ((BannerAdViewHolder)holder).setBannerAdData( bannerAdImg, bannerAdBg, position );
                break;

            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                List<String> hrProductIdList =
                        commonCatModelList.get( position ).getHrAndGridLayoutProductIdList();
                List<HrLayoutItemModel> hrProductsDetailsList =
                        commonCatModelList.get( position ).getHrAndGridProductsDetailsList();
                String hrLayoutTitle = commonCatModelList.get( position ).getHrAndGridLayoutTitle();
                ((HorizontalLayoutViewHolder)holder).setHrData( hrProductIdList, hrLayoutTitle, hrProductsDetailsList,position );
                break;

            case GRID_ITEM_LAYOUT_CONTAINER:
                List<String> gridProductIdList =
                        commonCatModelList.get( position ).getHrAndGridLayoutProductIdList();
                List<HrLayoutItemModel> gridProductsDetailsList =
                        commonCatModelList.get( position ).getHrAndGridProductsDetailsList();
                String gridLayoutTitle = commonCatModelList.get( position ).getHrAndGridLayoutTitle();
                ((GridLayoutViewHolder)holder).setDataGridLayout( gridProductIdList, gridLayoutTitle, gridProductsDetailsList,position );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return commonCatModelList.size();
    }

    //____________________________ View Holder Class _______________________________________________
    //============  Banner Slider View Holder ============
    public class CatViewHolder extends RecyclerView.ViewHolder {
        private TextView headTitle;
        private TextView indexNo;
        private Button viewAllBtn;
        private RecyclerView catRecycler;
        private TextView warningText;
        private ImageView editLayoutBtn;

        public CatViewHolder(@NonNull View itemView) {
            super( itemView );
            headTitle = itemView.findViewById( R.id.hrViewTotalItemText );
            indexNo = itemView.findViewById( R.id.hrViewIndexText );
            viewAllBtn = itemView.findViewById( R.id.hrViewAllBtn );
            catRecycler = itemView.findViewById( R.id.horizontal_view_recycler );
            warningText = itemView.findViewById( R.id.hr_warning_text );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );

            viewAllBtn.setVisibility( View.GONE );
            indexNo.setVisibility( View.GONE );
            editLayoutBtn.setVisibility( View.GONE );
        }
        private void setCatData(final List<BannerAndCatModel> bannerAndCatModelList, final String layoutTitle, int index){
            headTitle.setText( layoutTitle + " (" + bannerAndCatModelList.size() + ")" );
            warningText.setText( "Go back to edit category position or name, icon changing of category." );
            CatItemAdaptor catItemAdaptor = new CatItemAdaptor( bannerAndCatModelList );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            catRecycler.setLayoutManager( linearLayoutManager );
            catRecycler.setAdapter( catItemAdaptor );
            catItemAdaptor.notifyDataSetChanged();
        }

    }
    //============  Banner Slider View Holder ============

    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private TextView headTitle;
        private TextView indexNo;
        private Button viewAllBtn;
        private RecyclerView bannerRecycler;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        private int layoutPosition;
        private Dialog dialog;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
            headTitle = itemView.findViewById( R.id.hrViewTotalItemText );
            indexNo = itemView.findViewById( R.id.hrViewIndexText );
            viewAllBtn = itemView.findViewById( R.id.hrViewAllBtn );
            bannerRecycler = itemView.findViewById( R.id.horizontal_view_recycler );
            warningText = itemView.findViewById( R.id.hr_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = dialogsClass.progressDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setBannerData(final List<BannerAndCatModel> bannerAndCatModelList, final String layoutTitle, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition);
            headTitle.setText( layoutTitle + " (" + bannerAndCatModelList.size() + ")" );
            if (bannerAndCatModelList.size()>=2){
                warningText.setVisibility( View.GONE );
            }else{
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add 2 or more banner to visible this layout to the customers!!" );
            }
            BannerItemAdaptor bannerItemAdaptor = new BannerItemAdaptor( bannerAndCatModelList , false, index, catType, catTitle );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            bannerRecycler.setLayoutManager( linearLayoutManager );
            bannerRecycler.setAdapter( bannerItemAdaptor );
            bannerItemAdaptor.notifyDataSetChanged();

            viewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.bannerSliderListViewAll = bannerAndCatModelList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE", VIEW_ALL_FOR_BANNER_PRODUCTS );
                    viewAllIntent.putExtra( "CAT_INDEX", catType );
                    viewAllIntent.putExtra( "CAT_TITLE", catTitle );
                    viewAllIntent.putExtra( "LIST_INDEX", index );
                    viewAllIntent.putExtra( "TITLE", layoutTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList<>();
//                    dogsList.add( "Update Layout::" + StaticValues.ID_UPDATE );
//                    dogsList.add( "Update Click::" + StaticValues.ID_CLICK );
                    dogsList.add( "Delete Layout::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    String layoutId = commonCatModelList.get( index ).getLayoutID();
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(),BANNER_SLIDER_LAYOUT_CONTAINER, index, layoutId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

    }
    //============  Banner Slider View Holder ============

    //============  Strip ad  View Holder ============
    public class StripAdViewHolder extends RecyclerView.ViewHolder{
        private ImageView stripAdImage;
        private TextView indexNo;
        private ImageView editLayoutBtn;
        private int defaultColor;
        private int layoutPosition;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private Switch visibleBtn;
        private Dialog dialog;

        public StripAdViewHolder(@NonNull View itemView) {
            super( itemView );
            stripAdImage = itemView.findViewById( R.id.strip_ad_image );
            indexNo = itemView.findViewById( R.id.strip_ad_index );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            defaultColor = ContextCompat.getColor( itemView.getContext(), R.color.colorGray);
            dialog = dialogsClass.progressDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setStripAdData(String imgLink, String colorCode, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                stripAdImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( colorCode ) ));
            }
//            stripAdImage.setImageResource( R.drawable.strip_ad_a );
            // set Image Resouice from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.strip_ad_placeholder ) ).into( stripAdImage );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList<String>();
                    dogsList.add( "Update Layout::" + StaticValues.ID_UPDATE );
                    dogsList.add( "Update Click::" + StaticValues.ID_CLICK );
                    dogsList.add( "Delete Layout::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    String layoutId = commonCatModelList.get( index ).getLayoutID();
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(),STRIP_AD_LAYOUT_CONTAINER, index, layoutId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

        private void openColorPicker(){
            new ColorPickerDialog.Builder(itemView.getContext(), AlertDialog.THEME_HOLO_DARK )
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setCancelable( false )
                    .setPositiveButton(("Confirm"),
                            new ColorEnvelopeListener() {
                                @Override
                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                    String color =  "#" + envelope.getHexCode();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        stripAdImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( color ) ));
                                    }
                                    showToast( color, itemView.getContext() );
                                }
                            })
                    .setNegativeButton(("Cancel"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                    .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                    .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                    .show();
        }

    }
    //============  Strip ad  View Holder ============

    //============  Strip ad  View Holder ============
    public class BannerAdViewHolder extends RecyclerView.ViewHolder{
        private ImageView bannerAdImage;
        private TextView indexNo;
        private ImageView editLayoutBtn;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private Switch visibleBtn;
        private int layoutPosition;
        private Dialog dialog;

        public BannerAdViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerAdImage = itemView.findViewById( R.id.banner_ad_image );
            indexNo = itemView.findViewById( R.id.banner_ad_index );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            dialog = dialogsClass.progressDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setBannerAdData(String imgLink, String colorCode, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bannerAdImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( colorCode ) ));
            }
//            bannerAdImage.setImageResource( R.drawable.banner_c );
            // set Image Resource from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.banner_placeholder ) ).into( bannerAdImage );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        defaultColor = stripAdImage.getBackgroundTintList().getDefaultColor();
//                    }
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList<String>();
                    dogsList.add( "Update Layout::" + StaticValues.ID_UPDATE );
                    dogsList.add( "Update Click::" + StaticValues.ID_CLICK );
                    dogsList.add( "Delete Layout::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    String layoutId = commonCatModelList.get( index ).getLayoutID();
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(), BANNER_AD_LAYOUT_CONTAINER, index, layoutId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );
        }
    }
    //============  Strip ad  View Holder ============

    //============  Horizontal Layout View Holder ============
    public class HorizontalLayoutViewHolder extends RecyclerView.ViewHolder{
        private TextView headTitle;
        private TextView indexNo;
        private Button viewAllBtn;
        private RecyclerView hrRecycler;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        List<HrLayoutItemModel> tempHrGridList;
        private HrLayoutItemAdaptor hrLayoutItemAdaptor;
        private Dialog dialog;
        private int layoutPosition;
        public HorizontalLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            headTitle = itemView.findViewById( R.id.hrViewTotalItemText );
            indexNo = itemView.findViewById( R.id.hrViewIndexText );
            viewAllBtn = itemView.findViewById( R.id.hrViewAllBtn );
            hrRecycler = itemView.findViewById( R.id.horizontal_view_recycler );
            warningText = itemView.findViewById( R.id.hr_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = dialogsClass.progressDialog( itemView.getContext() );
            tempHrGridList = new ArrayList <>();
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setHrData(final List<String> hrLayoutProductIdList, final String layoutTitle, final List<HrLayoutItemModel> hrProductDetailsList, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
            headTitle.setText( layoutTitle + " (" + hrLayoutProductIdList.size() + ")" );
//            String layoutId = commonCatModelList.get( index ).getLayoutID();

            if (hrLayoutProductIdList.size() < 3){
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add min 3 products to make visible this layout to the customers.!" );
            }else{
                warningText.setVisibility( View.GONE );
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            hrRecycler.setLayoutManager( linearLayoutManager );

            if (hrProductDetailsList.size() == 0){
                hrLayoutItemAdaptor = new HrLayoutItemAdaptor( tempHrGridList, catTitle, catType, index );
                hrRecycler.setAdapter( hrLayoutItemAdaptor );
                hrLayoutItemAdaptor.notifyDataSetChanged();
                if (hrLayoutProductIdList.size() > 6){
                    for (int id_no = 0; id_no < 6; id_no++){
                        loadProductDetailsData(index, hrLayoutProductIdList.get( id_no ));
                    }
                }else{
                    for (int id_no = 0; id_no < hrLayoutProductIdList.size(); id_no++){
                        loadProductDetailsData(index, hrLayoutProductIdList.get( id_no ));
                    }
                }
            }
            else{
                hrLayoutItemAdaptor = new HrLayoutItemAdaptor( hrProductDetailsList, catTitle, catType, index );
                hrRecycler.setAdapter( hrLayoutItemAdaptor );
                hrLayoutItemAdaptor.notifyDataSetChanged();
            }

            // View All btn Click...
            viewAllBtn.setVisibility( View.VISIBLE );
            viewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        horizontalItemViewModelListViewAll = new ArrayList <>();
//                        horizontalItemViewModelListViewAll = hrLayoutItemModelList;
                    if (hrProductDetailsList.size() == 0){
                        ViewAllActivity.horizontalItemViewModelListViewAll = tempHrGridList;
                    }else{
                        ViewAllActivity.horizontalItemViewModelListViewAll = hrProductDetailsList;
                    }

                        ViewAllActivity.totalProductsIdViewAll = hrLayoutProductIdList;
                        Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra( "LAYOUT_CODE", VIEW_ALL_FOR_HORIZONTAL_PRODUCTS );
                        viewAllIntent.putExtra( "CAT_INDEX", catType ); // passing category index...
                        viewAllIntent.putExtra( "CAT_TITLE", catTitle );
                        viewAllIntent.putExtra( "LIST_INDEX", index ); //  passing index of current layout inside of category...
                        viewAllIntent.putExtra( "TITLE", layoutTitle ); // passing category title...
                        itemView.getContext().startActivity( viewAllIntent );

                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList<String>();
                    dogsList.add( "Change Title::" + StaticValues.ID_UPDATE );
//                    dogsList.add( "Update Click::" + StaticValues.ID_CLICK );
                    dogsList.add( "Delete Layout::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    String layoutId = commonCatModelList.get( index ).getLayoutID();
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(), HORIZONTAL_ITEM_LAYOUT_CONTAINER, index, layoutId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );


            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

        private void loadProductDetailsData(final int index, final String productId ){
            firebaseFirestore.collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        tempHrGridList.add( new HrLayoutItemModel( productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (long) task.getResult().get( "product_stocks" )
                                , (Boolean) task.getResult().get( "product_cod" ) ) );

                        commonCatModelList.get( index ).setHrAndGridProductsDetailsList( tempHrGridList );
                        hrLayoutItemAdaptor.notifyDataSetChanged();
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );
        }

    }
    //============  Horizontal Layout View Holder ============

    //==============  GridProduct Grid Layout View Holder =================
    public class GridLayoutViewHolder extends  RecyclerView.ViewHolder{
        private GridLayout gridLayout;
        private TextView gridLayoutTitle;
        private TextView indexNo;
        private Button gridLayoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        List<HrLayoutItemModel> tempHrGridList;
        private int layoutPosition;
        // Layout///
        public GridLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.product_grid_layout );
            gridLayoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            gridLayoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = dialogsClass.progressDialog( itemView.getContext() );
            tempHrGridList = new ArrayList <>();
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setDataGridLayout(final List<String> gridLayoutProductIdList, final String gridTitle, final List<HrLayoutItemModel> gridProductDetailsList
                ,final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
            gridLayoutTitle.setText( gridTitle + " (" + gridLayoutProductIdList.size() + ")" );

            int gridRange;
            if (gridLayoutProductIdList.size()<3){
                gridRange = gridLayoutProductIdList.size();
                warningText.setVisibility( View.VISIBLE );
            }
            else{
                gridRange = 3;
                if (gridLayoutProductIdList.size()<4){
                    warningText.setVisibility( View.VISIBLE );
                    warningText.setText( "Add min 4 products to make visible this layout to the customers.!" );
                }else{
                    warningText.setVisibility( View.GONE );
                }
            }

            if (gridProductDetailsList.size() == 0){
                for (int i = 0; i < gridRange; i++ ){
                    setProductData(i, index, gridLayoutProductIdList.get( i ) );
                }
            }

            for (int i = 0; i < gridRange; i++ ){
                if (gridProductDetailsList.size()!= 0){
                    ConstraintLayout itemLayout = gridLayout.getChildAt( i ).findViewById( R.id.product_layout );
                    gridLayout.getChildAt( i ).findViewById( R.id.add_new_item_Linearlayout ).setVisibility( View.GONE );
                    itemLayout.setVisibility( View.VISIBLE );

                    ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_image );
                    TextView name = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_name );
                    TextView price = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_price );
                    TextView cutPrice = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_cut_price );
                    TextView perOffText = gridLayout.getChildAt( i ).findViewById( R.id.hr_off_percentage );

                    name.setText( gridProductDetailsList.get( i ).getHrProductName() );
                    price.setText("Rs." + gridProductDetailsList.get( i ).getHrProductPrice() +"/-" );
                    cutPrice.setText( "Rs."+ gridProductDetailsList.get( i ).getHrProductCutPrice() +"/-" );
                    // Set img resource
                    Glide.with( itemView.getContext() ).load( gridProductDetailsList.get( i ).getHrProductImage()  )
                            .apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( img );

                    int mrp =  Integer.parseInt( gridProductDetailsList.get( i ).getHrProductCutPrice());
                    int showPrice = Integer.parseInt(  gridProductDetailsList.get( i ).getHrProductPrice());
                    int perOff = (( mrp - showPrice )*100)/showPrice;
                    perOffText.setText( perOff +"% Off" );
                }
                ConstraintLayout itemLayout = gridLayout.getChildAt( i ).findViewById( R.id.product_layout );
                // ClickListener...
                itemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.product_layout )){
//                                commonCatList.get( catType ).get( index ).getHrAndGridProductsDetailsList()
                                addOnProductClick(  gridLayoutProductIdList.get( 0 ), itemView.getContext(), index );
                            } else
                                if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.product_layout )){
                                    addOnProductClick(  gridLayoutProductIdList.get( 1 ), itemView.getContext(), index );
                            } else
                                if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.product_layout )){
                                    addOnProductClick(  gridLayoutProductIdList.get( 2 ), itemView.getContext(), index );
                            }
                    }
                } );

            }
            // Add new Product in Grid Layout...
            for (int k = 0; k < 4; k++ ) {
                ConstraintLayout itemLayout = gridLayout.getChildAt( k ).findViewById( R.id.product_layout );
                LinearLayout addNewItemLayout = gridLayout.getChildAt( k ).findViewById( R.id.add_new_item_Linearlayout );
                if ( k < gridRange ){
                    itemLayout.setVisibility( View.VISIBLE );
                    addNewItemLayout.setVisibility( View.GONE );
                }else{
                    itemLayout.setVisibility( View.GONE );
                    addNewItemLayout.setVisibility( View.VISIBLE );
                }

                addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.add_new_item_Linearlayout )){
                            addNewItem( index, 0 );
                        } else
                        if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.add_new_item_Linearlayout )){
                            addNewItem( index, 1 );
                        } else
                        if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.add_new_item_Linearlayout )){
                            addNewItem( index, 2 );
                        } else
                        if ( v == gridLayout.getChildAt( 3 ).findViewById( R.id.add_new_item_Linearlayout )){
                            addNewItem( index, 3 );
                        }
                    }
                } );

            }

            gridLayoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    gridViewModelListViewAll = new ArrayList <>();
//                    gridViewModelListViewAll = gridLayoutList;
                    if (gridProductDetailsList.size() == 0){
                        ViewAllActivity.gridViewModelListViewAll = tempHrGridList;
                    }else{
                        ViewAllActivity.gridViewModelListViewAll = gridProductDetailsList;
                    }
                    ViewAllActivity.totalProductsIdViewAll = gridLayoutProductIdList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE",VIEW_ALL_FOR_GRID_PRODUCTS );
                    viewAllIntent.putExtra( "CAT_INDEX", catType );
                    viewAllIntent.putExtra( "CAT_TITLE", catTitle );
                    viewAllIntent.putExtra( "LIST_INDEX", index );
                    viewAllIntent.putExtra( "TITLE", gridTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupWindow popupWindowDogs;
                    // format is DogName::DogID
                    List<String> dogsList = new ArrayList<String>();
                    dogsList.add( "Change Title::" + StaticValues.ID_UPDATE );
//                    dogsList.add( "Update Click::" + StaticValues.ID_CLICK );
                    dogsList.add( "Delete Layout::" + StaticValues.ID_DELETE );
                    // convert to simple array
                    String[] popUpContents = new String[dogsList.size()];
                    dogsList.toArray(popUpContents);
                    String layoutId = commonCatModelList.get( index ).getLayoutID();
                    // initialize pop up window
                    popupWindowDogs = popupWindowDogs( itemView.getContext(), GRID_ITEM_LAYOUT_CONTAINER, index, layoutId, popUpContents);
                    popupWindowDogs.showAsDropDown(v);
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = commonCatModelList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "visibility", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }
        private void setProductData(final int pos, final int index, final String productId){
            firebaseFirestore.collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){

                        String pImgLink = task.getResult().get( "product_image_1").toString();
                        String pName = task.getResult().get( "product_full_name" ).toString();
                        String pPrice = task.getResult().get( "product_price" ).toString();
                        String pMrpPrice = task.getResult().get( "product_cut_price" ).toString();
                        long pStock = (long) task.getResult().get( "product_stocks" );
                        Boolean pCod = (Boolean) task.getResult().get( "product_cod" );

                        // access the banners from database...
                        tempHrGridList.add( new HrLayoutItemModel( productId
                                , pImgLink
                                , pName
                                , pPrice
                                , pMrpPrice
                                , pStock
                                , pCod ));

                        commonCatModelList.get( index ).setHrAndGridProductsDetailsList( tempHrGridList );
                        temp = pos;


                        ConstraintLayout itemLayout = gridLayout.getChildAt( pos ).findViewById( R.id.product_layout );
                        itemLayout.setVisibility( View.VISIBLE );
                        gridLayout.getChildAt( pos ).findViewById( R.id.add_new_item_Linearlayout ).setVisibility( View.GONE );

                        ImageView img = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_image );
                        TextView name = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_name );
                        TextView price = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_price );
                        TextView cutPrice = gridLayout.getChildAt( temp ).findViewById( R.id.hr_product_cut_price );
                        TextView perOffText = gridLayout.getChildAt( temp ).findViewById( R.id.hr_off_percentage );
                        TextView stockText = gridLayout.getChildAt( temp ).findViewById( R.id.stock_text );

                        name.setText( pName );
                        price.setText("Rs." + pPrice +"/-" );
                        cutPrice.setText( "Rs."+ pMrpPrice +"/-" );
                        if (pStock > 0){
                            stockText.setText("In Stock (" + pStock + ")");
                        }else{
                            stockText.setText( "Out Of Stock" );
                        }
                        // Set img resource
                        Glide.with( itemView.getContext() ).load( pImgLink )
                                .apply( new RequestOptions().placeholder( R.drawable.square_placeholder ) ).into( img );

                        int mrp =  Integer.parseInt( pMrpPrice );
                        int showPrice = Integer.parseInt( pPrice );
                        int perOff = (( mrp - showPrice )*100)/showPrice;
                        perOffText.setText( perOff +"% Off" );
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );


        }
        private void addOnProductClick(String productId, Context context, int index){

            StaticValues.UPDATE_P_LAY_INDEX = index;
            StaticValues.UPDATE_P_CAT_INDEX = catType;
            StaticValues.UPDATE_PRODUCT_CAT = catTitle;

            Intent productDetailIntent = new Intent( context, ProductDetails.class );
            productDetailIntent.putExtra( "PRODUCT_ID", productId );
            context.startActivity( productDetailIntent );

        }
        private void addNewItem(final int layIndex, int listIndex){
            Map <String, Object> newProductMap = new HashMap <>();
            /** Is_Update :
             1. N - Created Product but Not add details yet.!
             2. Y - Added Details and Update...
             3. V - Visible to search...
             4. I - InVisible : Stop from searching etc...
             */
            newProductMap.put( "a_is_update", "N" );
            newProductMap.put( "a_no_of_uses", 0 );
            newProductMap.put( "a_product_cat", catTitle );

            final Dialog dialog = new DialogsClass().progressDialog( itemView.getContext() );
            dialog.show();
            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).add( newProductMap )
                    .addOnCompleteListener( new OnCompleteListener <DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task <DocumentReference> task) {
                            if (task.isSuccessful()){
                                // Shift to add product details...
                                String docId = task.getResult().getId();
                                Toast.makeText( itemView.getContext(), "Product Id : "+docId, Toast.LENGTH_SHORT ).show();

                                Intent gotoAddProductIntent = new Intent(itemView.getContext(), AddNewProductActivity.class );
                                gotoAddProductIntent.putExtra( "PRODUCT_ID", docId );
                                gotoAddProductIntent.putExtra( "LAY_INDEX", layIndex );
                                gotoAddProductIntent.putExtra( "CAT_INDEX", catType );
                                gotoAddProductIntent.putExtra( "PRODUCT_CAT", catTitle );
                                gotoAddProductIntent.putExtra( "UPDATE", false );
                                itemView.getContext().startActivity( gotoAddProductIntent );
                            }else{
                                // Show Toast..  to try again...
                                Toast.makeText( itemView.getContext(), "Creating New Product Id Failed..! Please Try Again.!", Toast.LENGTH_SHORT ).show();
                            }
                            dialog.dismiss();
                        }
                    } );
        }

    }
    //==============  GridProduct Grid Layout View Holder =================

    //                            _________________________________________
    /*___________________________ Others Methods to Updates the Layouts ____________________________*/

    private void setIndexUpDownVisibility( int index, ImageView indexUpBtn,  ImageView indexDownBtn){
        if (commonCatModelList.size()>1){
            indexUpBtn.setVisibility( View.VISIBLE );
            indexDownBtn.setVisibility( View.VISIBLE );
            if (index == 0){
                indexUpBtn.setVisibility( View.INVISIBLE );
            }else if (index == commonCatModelList.size()-1){
                indexDownBtn.setVisibility( View.INVISIBLE );
            }
        }else{
            indexUpBtn.setVisibility( View.INVISIBLE );
            indexDownBtn.setVisibility( View.INVISIBLE );
        }
    }
    private void updateIndex(boolean isMoveUp, int index, Dialog dialog){
        dialog.show();
       if (isMoveUp){
           // GoTo Up...
           updateIndexOnDatabase(index, (index - 1), dialog );
       }else{
           // Goto Down..
           updateIndexOnDatabase(index, (index + 1), dialog );
       }
    }
    private void updateIndexOnDatabase(final int startInd, final int endInd, final Dialog dialog){
        String[] layoutId = new String[]{ commonCatModelList.get( startInd ).getLayoutID(), commonCatModelList.get( endInd ).getLayoutID() };

        for ( String tempId : layoutId){
            if (!dialog.isShowing()){
                dialog.show();
            }
            Map <String, Object> updateMap = new HashMap <>();
            updateMap.clear();
            if (tempId.equals( layoutId[0] )){
                updateMap.put( "index", ( endInd ) );
            }else{
                updateMap.put( "index", ( startInd ) );
            }
            updateOnDocument(dialog, tempId, updateMap);
        }
        Collections.swap( commonCatModelList, startInd, endInd );
        commonCatList.set( catType, commonCatModelList );
        CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();

    }
    private void updateOnDocument(final Dialog dialog, String layoutId, Map <String, Object> updateMap){

        if ( userCityName != null && tempProductAreaCode != null){
            StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                    .collection( "CATEGORIES" ).document( catTitle.toUpperCase() )
                    .collection( "LAYOUTS" ).document( layoutId ).update( updateMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            dialog.dismiss();
                        }
                    } );
        }else{
            dialog.dismiss();
        }
    }

    private void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    // More Option CLick...
    public PopupWindow popupWindowDogs(final Context context, final int layoutType, final int index, final String layoutId, String popUpContents[]) {
        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(context);
        // the drop down list is a list view
        ListView listViewDogs = new ListView(context);
        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter( StaticMethods.dogsAdapter( popUpContents, context ));
        listViewDogs.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                String selectedItemTag = ((TextView) view).getTag().toString();
                switch ( layoutType ){
                    case BANNER_SLIDER_LAYOUT_CONTAINER: // Only Delete...
                        alertDialog(context, index, layoutId);
                        break;
                    case STRIP_AD_LAYOUT_CONTAINER: // 3 no
                    case BANNER_AD_LAYOUT_CONTAINER: // 3 no
                        switch (Integer.parseInt( selectedItemTag )){
                            case StaticValues.ID_UPDATE:
                                //  : Update Banner Ad and Strip Ad..!
                                UpdateImages.uploadImageLink = commonCatModelList.get( index ).getStripAndBannerAdImg();
                                UpdateImages.uploadImageBgColor = commonCatModelList.get( index ).getStripAndBannerAdBg();

                                Intent intent = new Intent( context, AddNewLayoutActivity.class );
                                intent.putExtra( "CAT_TITLE", catTitle );
                                intent.putExtra( "CAT_INDEX", catType );
                                intent.putExtra( "LAY_TYPE", layoutType );
                                intent.putExtra( "LAY_INDEX", index );
                                intent.putExtra( "TASK_UPDATE", true );
                                context.startActivity( intent );
                                break;
                            case StaticValues.ID_DELETE:
                                alertDialog(context, index, layoutId);
                                break;
                            case StaticValues.ID_CLICK:
                                // TODO: CLICK Update,....
                                showToast( "method is not written yet..!", context );
                                break;
                            default:
                                break;
                        }
                        break;
                    case HORIZONTAL_ITEM_LAYOUT_CONTAINER: // 2 title and delete
                    case GRID_ITEM_LAYOUT_CONTAINER: // 2 title and delete
                        switch (Integer.parseInt( selectedItemTag )){
                            case StaticValues.ID_UPDATE:
                                // Rename Layout... Single Text Dialog..!
                                updateNameOfLayout( context, index, layoutId, null );
                                break;
                            case StaticValues.ID_DELETE:
                                alertDialog(context, index, layoutId);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                popupWindow.dismiss();
            }
        } );
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight( WindowManager.LayoutParams.WRAP_CONTENT);
        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private void alertDialog(final Context context, final int index, final String layoutId){
        AlertDialog.Builder alertD = new AlertDialog.Builder( context );
        alertD.setTitle( "Do You want to delete this Layout.?" );
        alertD.setMessage( "If you delete this layout, you will loose all the inside data of the layout.!" );
        alertD.setCancelable( false );
        alertD.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final Dialog pDialog = dialogsClass.progressDialog( context );
                pDialog.show();
                // Query to delete the Layout...!

                if ( userCityName != null && tempProductAreaCode != null){
                    StaticValues.getFirebaseDocumentReference( userCityName, tempProductAreaCode )
                            .collection( "CATEGORIES" ).document( catTitle.toUpperCase() )
                            .collection( "LAYOUTS" ).document( layoutId ).delete()
                            .addOnCompleteListener( new OnCompleteListener <Void>() {
                                @Override
                                public void onComplete(@NonNull Task <Void> task) {
                                    if (task.isSuccessful()){
                                        showToast( "Deleted Layout Successfully.!", context );
                                        // : Update in local list..!
                                        commonCatModelList.remove( index );
                                        commonCatList.set( catType, commonCatModelList );
                                        CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();
                                    }else {
                                        showToast( "Failed.! Something went wrong.!", context );
                                    }
                                    pDialog.dismiss();
                                }
                            } );
                }else{
                    showToast( "Failed.!", context );
                    pDialog.dismiss();
                }

            }
        } );
        alertD.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        alertD.show();
    }
    private void updateNameOfLayout(final Context context, final int index, final String layoutId, @Nullable String StDetText){
        // dType == 1 : For Stocks...
        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( context );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_single_edit_text );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        final Button okBtn = quantityDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.dialog_cancel_btn );
        final TextView dialogTitle = quantityDialog.findViewById( R.id.dialog_title );
        // 1 & 2 Stocks Or Details Update...
        final EditText dialogEditText = quantityDialog.findViewById( R.id.dialog_editText );

            dialogTitle.setText( "Enter Layout Title :" );
            dialogEditText.setInputType( InputType.TYPE_CLASS_TEXT );
            dialogEditText.setSingleLine();
            dialogEditText.setHint( "Enter title" );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialogEditText.setTextAlignment( EditText.TEXT_ALIGNMENT_TEXT_START );
            }
            if (StDetText != null){
                dialogEditText.setText( StDetText );
            }else dialogEditText.setText( "" );
        quantityDialog.show();
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty( dialogEditText.getText().toString() )){
                    dialogEditText.setError( "Enter Any title Text..!" );
                }
                else{
                    // : Raise a query.. to rename...
                    quantityDialog.dismiss();
                    Dialog dialog = dialogsClass.progressDialog( context );
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.put( "layout_title", dialogEditText.getText().toString() );
                    dialog.show();
                    updateOnDocument(dialog, layoutId, updateMap);
                    // update in local list...
                    commonCatModelList.get( index ).setHrAndGridLayoutTitle( dialogEditText.getText().toString() );
                    commonCatList.set( catType, commonCatModelList );
                    CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();
                }

            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();
            }
        } );

    }


}

// WackyCodes - (Shailendra Lodhi) ... //

