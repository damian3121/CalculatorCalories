package com.mscisz.damian.calculator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

public class Notification_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService( Context.NOTIFICATION_SERVICE );

        Intent repeating_intent = new Intent( context, Repeating_activity.class );
        repeating_intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        PendingIntent pendingIntent = PendingIntent.getActivity( context, 100,
                repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT );

        String channelId = "default_channel_id";
        String channelDescription = "Default Channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder( context, channelId )
                .setContentIntent( pendingIntent )
                .setSmallIcon( android.R.drawable.arrow_up_float )
                .setContentTitle( "FitApp" )
                .setContentText( "Pamiętaj o wprowadzeniu posiłków i zaktualizowaniu bieżącej wagi!" )
                .setAutoCancel( true );

        notificationManager.notify( 100, builder.build() );
    }
}
