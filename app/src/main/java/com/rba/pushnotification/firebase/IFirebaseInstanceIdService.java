package com.rba.pushnotification.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rba.pushnotification.storage.SessionManager;

/**
 * Created by Ricardo Bravo on 23/08/16.
 */

public class IFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = IFirebaseInstanceIdService.class.getSimpleName();

    public IFirebaseInstanceIdService() {
    }

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + fcmToken);
        Log.i("x- token", ""+SessionManager.isToken(this));

        if(!SessionManager.isToken(this)){
            SessionManager.addDeviceToken(this, fcmToken);
        }
    }

}
