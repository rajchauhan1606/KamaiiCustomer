package com.kamaii.customer;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.TrackingActivity;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        prefrence = SharedPrefrence.getInstance(this);
        context = this;
        if (remoteMessage.getData().size() > 0) {
        }

        if (remoteMessage.getData() != null) {

            if (remoteMessage.getData().containsKey(Consts.TYPE)) {
                if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.CHAT_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.CHAT_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TICKET_COMMENT_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.TICKET_COMMENT_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TICKET_STATUS_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.TICKET_STATUS_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.WALLET_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.WALLET_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION)) {
                    sendDeclineNotificationcopy(getValue(remoteMessage.getData(), "body"), Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION);
                    //  sendNotification(getValue(remoteMessage.getData(), "body"), Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.START_BOOKING_ARTIST_NOTIFICATION)) {
                    sendNotificationcopy(getValue(remoteMessage.getData(), "body"), Consts.START_BOOKING_ARTIST_NOTIFICATION);
                    // sendNotification(getValue(remoteMessage.getData(), "body"), Consts.START_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.END_BOOKING_ARTIST_NOTIFICATION)) {
                    sendNotificationcopy(getValue(remoteMessage.getData(), "body"), Consts.END_BOOKING_ARTIST_NOTIFICATION);
                    // sendNotification(getValue(remoteMessage.getData(), "body"), Consts.END_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ACCEPT_BOOKING_ARTIST_NOTIFICATION)) {
                    sendNotificationcopy(getValue(remoteMessage.getData(), "body"), Consts.ACCEPT_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.JOB_APPLY_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.JOB_APPLY_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.BRODCAST_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.BRODCAST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ADMIN_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.ADMIN_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ARRIVAL_NOTIFICATION)) {
                    sendarrival(getValue(remoteMessage.getData(), "body"), Consts.ARRIVAL_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TIME_CONFIRM_DIALOG_NOTIFICATION)) {
                    sendNotificationstoprring(getValue(remoteMessage.getData(), "body"), Consts.TIME_CONFIRM_DIALOG_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase("9000")) {
                    sendNotificationcopy2(getValue(remoteMessage.getData(), "body"), "9000");
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.SWIFT_REPEAT_NOTIFICATION)) {
                    sendNotificationcrepeat(getValue(remoteMessage.getData(), "body"), Consts.SWIFT_REPEAT_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.RIDER_HAS_PICKUP_ORDER)) {
                    sendarrival(getValue(remoteMessage.getData(), "body"), Consts.RIDER_HAS_PICKUP_ORDER);
                } else {
                    sendNotification(getValue(remoteMessage.getData(), "body"), "");
                }
            }
        }

    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }

    @Override
    public void onNewToken(String token) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Consts.DEVICE_TOKEN, token);
        editor.commit();
    }

    private void sendNotificationstoprring(String messageBody, String tag) {

        Log.e("notification_number","0" + " tag :-- "+tag);

        Intent resultIntent = null;

        resultIntent = new Intent(this, BaseActivity.class);
        resultIntent.putExtra(Consts.SCREEN_TAG, tag);
        resultIntent.putExtra("message", messageBody);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))

                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }

    private void sendNotification(String messageBody, String tag) {

        Log.e("notification_number","1" + " tag :-- "+tag);
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void sendarrival(String messageBody, String tag) {

        Log.e("notification_number","2"+ " tag :-- "+tag);

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        try {
            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.arrival);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
            r.play();
        } catch (Exception e) {

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }


    private void sendNotificationcopy(String messageBody, String tag) {

        Log.e("notification_number","3"+ " tag :-- "+tag);

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        long[] VIBRATE_PATTERN = {0, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());


    }


    private void sendDeclineNotificationcopy(String messageBody, String tag) {

        Log.e("notification_number","4"+ " tag :-- "+tag);

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        long[] VIBRATE_PATTERN = {0, 500};

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.cancel);

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());


    }


    private void sendNotificationcopy2(String messageBody, String tag) {

        Log.e("notification_number","5"+ " tag :-- "+tag);

        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("flag", "0");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        long[] VIBRATE_PATTERN = {0, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void sendNotificationcrepeat(String messageBody, String tag) {

        Log.e("notification_number","6"+ " tag :-- "+tag);

        if (isAppIsInBackground(this)) {
            TrackingActivity viewMapTrackingActivity = new TrackingActivity();
            viewMapTrackingActivity.getLatestBooking();
        }
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        // Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ring);
        long[] VIBRATE_PATTERN = {0, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppIsInBackground(Context context) {

        Log.e("notification_number","7");

        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}

