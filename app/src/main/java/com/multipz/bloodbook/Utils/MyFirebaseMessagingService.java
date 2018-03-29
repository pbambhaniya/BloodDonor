package com.multipz.bloodbook.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.multipz.bloodbook.Activity.BirthdayActivity;
import com.multipz.bloodbook.Activity.ReceiveNotificationActivity;
import com.multipz.bloodbook.MainActivity;
import com.multipz.bloodbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.multipz.bloodbook.Utils.AppController.TAG;

/**
 * Created by Admin on 28-06-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, " Message Body: " + remoteMessage.getData());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        String msg = "" + remoteMessage.getData().get("result").toString();

        try {
            JSONObject jsonObject = new JSONObject(msg);

            String body = jsonObject.getString("body");
            String title = jsonObject.getString("title");

            if (jsonObject.has("type")){
                sendNotificationBirthday(body, title, jsonObject.getString("BirthdayImage"));
                return;
            }

            String ContactNo = jsonObject.getString("ContactNo");
            String BloodRequestId = jsonObject.getString("BloodRequestId");
            String UserName = jsonObject.getString("UserName");
            String Address = jsonObject.getString("Address");
            String UserImage = jsonObject.getString("UserImage");
            String UserId = jsonObject.getString("UserId");
            String RequestedUserId = jsonObject.getString("RequestedUserId");
            String BloodType = jsonObject.getString("BloodType");
            sendNotification(ContactNo, UserName, BloodRequestId, RequestedUserId, Address, body, title, UserImage, UserId, BloodType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String ContactNo, String UserName, String BloodRequestId, String RequestId, String Address, String body, String title, String UserImage, String UserId, String BloodType) {

        Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        intent.putExtra("ContactNo", ContactNo);
        intent.putExtra("UserName", UserName);
        intent.putExtra("RequestId", BloodRequestId);
        intent.putExtra("Address", Address);
        intent.putExtra("Message", body);
        intent.putExtra("UserImage", UserImage);
        intent.putExtra("UserId", UserId);
        intent.putExtra("BloodType", BloodType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationBirthday(String body, String title, String image) {

        Intent intent = new Intent(this, BirthdayActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.putExtra("img", image);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
