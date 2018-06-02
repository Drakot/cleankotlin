package es.demokt.kotlindemoproject.utils.message.impl;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import es.demokt.kotlindemoproject.R;
import es.demokt.kotlindemoproject.utils.message.Message;
import es.demokt.kotlindemoproject.utils.message.MessageParams;

public class NotificationMessage implements Message {
    public static final int NOTIFICATION_ID = 1345890101;
    private MessageParams mMessageParams;
    private Activity mActivity;
    private NotificationCompat.Builder mBuilder;

    public NotificationMessage(Activity activity, MessageParams messageParams) {
        mActivity = activity;
        mMessageParams = messageParams;
    }

    @Override public Message init() {
        int iconID = R.mipmap.ic_launcher;
        Intent contentIntent = new Intent(mActivity, mActivity.getClass());

        contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent theappIntent =
            PendingIntent.getActivity(mActivity, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity).setSmallIcon(iconID)
            .setContentTitle(mMessageParams.getTitle())
            .setContentText(mMessageParams.getMessage())
            .setContentIntent(theappIntent)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(mMessageParams.getMessage()));

        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        mBuilder = builder;
        //addActions();
        return this;
    }


    /*
    public void addActions() {
        Intent actionIntent = new Intent(mActivity, ActionReceiver.class);
        for (Button button : mMessageParams.getButtons()) {
            actionIntent.putExtra(EXTRA_BUTTON + button.getIdAppliance(), button);

            actionIntent.setAction(button.getIdAppliance());
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(mActivity, 0, actionIntent, 0);
            mBuilder.addAction(0, button.getConcept(), nextPendingIntent);
        }
    }*/

    @Override public void show() {
        NotificationManager notificationManager =
            (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override public void setButtonListener(ButtonListener buttonListener) {
    }

    @Override public void cancel() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) mActivity.getSystemService(ns);
        nMgr.cancel(NOTIFICATION_ID);
    }
}
