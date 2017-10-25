package com.isapanah.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.isapanah.firebase.Attribuites.FCM;
import static com.isapanah.firebase.Attribuites.FCM_ACTION_NEW_NOTIFICATION;
import static com.isapanah.firebase.Attribuites.FCM_TOPIC_GLOBAL;
import static com.isapanah.firebase.Attribuites.FCM_TOPIC_ONSCREEN;
import static com.isapanah.firebase.Attribuites.FCM_TOPIC_VIP_USER;
import static com.isapanah.firebase.Attribuites.VIP_USER;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton VIPUser;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareFloatActionButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("FirebaseToken","refID: "+FirebaseInstanceId.getInstance().getToken());

        RegisterBroadcastService();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCM_ACTION_NEW_NOTIFICATION));

        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC_GLOBAL);

        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC_ONSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FCM_TOPIC_ONSCREEN);
    }

    private void prepareFloatActionButton(){

        VIPUser = (FloatingActionButton) findViewById(R.id.VIPUserAction);
        VIPUser.setImageResource( getUserVIP() ? R.drawable.special : R.drawable.not_special );

        VIPUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserVIP(!getUserVIP(), view);
            }
        });

    }

    private boolean getUserVIP(){
        return getSharedPreferences(FCM, 0).getBoolean(VIP_USER, false);
    }

    private void setUserVIP(boolean isVIP, View view){

        getSharedPreferences(FCM, 0).edit().putBoolean(VIP_USER, isVIP).apply();
        VIPUser.setImageResource( isVIP ? R.drawable.special : R.drawable.not_special );

        if(isVIP){
            Snackbar.make(view, "You are a VIP user.", Snackbar.LENGTH_LONG).show();
            FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC_VIP_USER);
        }else{
            Snackbar.make(view, "You are a regular user", Snackbar.LENGTH_LONG).show();
            FirebaseMessaging.getInstance().unsubscribeFromTopic(FCM_TOPIC_VIP_USER);
        }

    }

    //Firebase Notification broadcast reciever
    private void RegisterBroadcastService(){


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case FCM_ACTION_NEW_NOTIFICATION:
                        showDialog(intent);
                        break;
                }
            }
        };

    }

    private void showDialog(Intent intent){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(intent.getStringExtra("title"));
        builder.setMessage(intent.getStringExtra("message"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
