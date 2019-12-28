package com.example.sms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class SmsReceiver extends BroadcastReceiver {
    int messageId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        Bundle bundle = intent.getExtras();

        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
        }
        String smsFromPhone = messages[0].getMessageBody();
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            body.append(messages[i].getMessageBody());
        }
        String bodyText = body.toString();
        MakeNote(context, smsFromPhone, bodyText);
    }

    public void MakeNote(Context context, String addresFrom, String message){
        NotificationCompat.Builder builder =new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(addresFrom)
                .setContentText(message);
        Intent resultIntent = new Intent(context, SmsReceiver.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}
