package com.rba.pushnotification.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.rba.pushnotification.MainActivity;
import com.rba.pushnotification.R;
import com.rba.pushnotification.util.Constant;

/**
 * Created by Ricardo Bravo on 23/08/16.
 */

public class IFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = IFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Mensaje recibido");
        Log.d("x- json1", new Gson().toJson(remoteMessage.getData()));
        Log.d("x- json2", new Gson().toJson(remoteMessage.getNotification()));
        displayNotification(remoteMessage);
        sendNewPromoBroadcast(remoteMessage);
    }

    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(MainActivity.NOTIFICATION);
        intent.putExtra(Constant.NOT_TITLE, remoteMessage.getNotification().getTitle());
        intent.putExtra(Constant.NOT_MESSAGE, remoteMessage.getNotification().getBody());
        intent.putExtra(Constant.NOT_DISCOUNT, remoteMessage.getData().get(Constant.PUSH_DISCOUNT));
        intent.putExtra(Constant.NOT_DATE, remoteMessage.getData().get(Constant.PUSH_DATE));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void displayNotification(RemoteMessage remoteMessage) {
        Log.i("x- not", new Gson().toJson(remoteMessage.getNotification()));
        Intent intent = new Intent(this, MainActivity.class);
        /*
        //intent.putExtra("hola", "hola");
        intent.putExtra(Constant.NOT_TITLE, remoteMessage.getNotification().getTitle());
        intent.putExtra(Constant.NOT_MESSAGE, remoteMessage.getNotification().getBody());
        intent.putExtra(Constant.NOT_DISCOUNT, remoteMessage.getData().get(Constant.PUSH_DISCOUNT));
        intent.putExtra(Constant.NOT_DATE, remoteMessage.getData().get(Constant.PUSH_DATE));
        */
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
