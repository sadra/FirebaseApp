package com.isapanah.firebase.PushNotification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.isapanah.firebase.Attribuites.FCM_Registered;

/**
 * Created by Sadra Isapanah Amlashi on 10/21/17.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(FCM_Registered);
        registrationComplete.putExtra("fcm_token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}