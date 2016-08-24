package com.rba.pushnotification;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rba.pushnotification.storage.SessionManager;
import com.rba.pushnotification.util.Constant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NOTIFICATION = "NOTIFICATION";
    private TextView lblNotification;
    private FirebaseMessaging firebaseMessaging;
    private BroadcastReceiver mNotificationsReceiver;
    private String title;
    private String message;
    private String discount;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblNotification = (TextView) findViewById(R.id.lblNotification);
        TextView lblToken = (TextView) findViewById(R.id.lblToken);
        ImageView imgCopy = (ImageView) findViewById(R.id.imgCopy);


        if (getIntent().getExtras() != null) {

            discount = getIntent().getExtras().getString(Constant.PUSH_DISCOUNT);
            title = getIntent().getExtras().getString(Constant.PUSH_TITLE);
            message = getIntent().getExtras().getString(Constant.PUSH_MESSAGE);
            date = getIntent().getExtras().getString(Constant.PUSH_DATE);

            showData();

        }

        firebaseMessaging = FirebaseMessaging.getInstance();

        Log.i("x- token1", ""+SessionManager.isToken(this));


        Log.i("x- not1", NOTIFICATION);

        if(SessionManager.isToken(this)){
            lblToken.setText(SessionManager.getDeviceToken(this));
        }

        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                title = intent.getStringExtra(Constant.NOT_TITLE);
                message = intent.getStringExtra(Constant.NOT_MESSAGE);
                discount = intent.getStringExtra(Constant.NOT_DISCOUNT);
                date = intent.getStringExtra(Constant.NOT_DATE);

                showData();
            }
        };

        imgCopy.setOnClickListener(this);

    }

    private void showData(){
        Log.i("x- info", "data: "+title+" - "+message);

        lblNotification.setText("Title: "+title+" - Message: "+message+" - Discount: "
                +discount+" % - Date: "+date);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imgCopy){
            copyText();
        }
    }

    private void copyText(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("DeviceToken",SessionManager.getDeviceToken(this));
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, getString(R.string.message_device), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mNotificationsReceiver, new IntentFilter(NOTIFICATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationsReceiver);
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseMessaging.subscribeToTopic("Aptitus");
    }
}
