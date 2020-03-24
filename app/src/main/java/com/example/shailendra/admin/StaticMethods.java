package com.example.shailendra.admin;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class StaticMethods {


    public PopupWindow popupWindowDogs(final Context context, String popUpContents[]) {

        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(context);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(dogsAdapter(popUpContents, context));

        // set the item click listener
        // TODO : Option to do anything
        listViewDogs.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                String selectedItemTag = ((TextView) view).getTag().toString();

//                showToast( "Item : " +selectedItemTag , context );
                popupWindow.dismiss();
            }
        } );

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight( WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    // Adapter Where our List will be set...
    public static ArrayAdapter <String> dogsAdapter(String dogsArray[], final Context context) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView( context );

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor( Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }

}
