package com.example.shailendra.admin.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.MenuItem;

import com.example.shailendra.admin.R;
import com.example.shailendra.admin.database.DBquery;

import java.util.ArrayList;
import java.util.List;

import static com.example.shailendra.admin.StaticValues.NOTIFY_ORDER_REQUEST;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationRecycler;
    public static NotificationsAdaptor notificationsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notifications );

        Toolbar toolbar = findViewById( R.id.toolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( "Notifications" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        // Set Recycler Layout...
        notificationRecycler = findViewById( R.id.notification_recycler );
        // Layout..
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        notificationRecycler.setLayoutManager( layoutManager );
        // Adaptor...

        notificationsAdaptor = new NotificationsAdaptor( DBquery.notificationModelList );
        notificationRecycler.setAdapter( notificationsAdaptor );
        notificationsAdaptor.notifyDataSetChanged();

    }
    // End Of onCreate...


    @Override
    protected void onResume() {
        super.onResume();
        if(notificationsAdaptor!=null){
            notificationsAdaptor.notifyDataSetChanged();
        }
    }

    // Tool Bar Item Selected....
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

}
