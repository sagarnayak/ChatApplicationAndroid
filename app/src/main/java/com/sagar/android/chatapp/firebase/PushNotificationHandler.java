package com.sagar.android.chatapp.firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.logutilmaster.LogUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PushNotificationHandler extends FirebaseMessagingService {

    @Inject
    LogUtil logUtil;
    @Inject
    Repository repository;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        logUtil.logV("new token : " + s);
        repository.tryUpdatingFCMToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            String operationType = remoteMessage.getData().get("operationType");
            //noinspection ConstantConditions
            switch (operationType) {
                case "AvatarUpdated":
                    avatarUpdated();
                    break;
                case "PingBack":
                    pingBack();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void avatarUpdated() {
        Intent intent = new Intent("AvatarUpdated");
        sendBroadcast(intent);
    }

    private void pingBack() {
        repository.ping();
    }
}
