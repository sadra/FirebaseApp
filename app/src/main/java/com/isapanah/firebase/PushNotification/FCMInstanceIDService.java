package com.isapanah.firebase.PushNotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.isapanah.firebase.Attribuites.FCM;
import static com.isapanah.firebase.Attribuites.FCM_Registered;
import static com.isapanah.firebase.Attribuites.FCM_Token;

/**
 * Created by sadra on 10/21/17.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(FCM_Registered);
        registrationComplete.putExtra("fcm_token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(FCM, 0);
        pref.edit().putString(FCM_Token, token) .apply();
        Log.d(FCMInstanceIDService.class.getSimpleName(), "Firebase Token: " + token+" | "+pref.getString(FCM_Token, null));
    }

}