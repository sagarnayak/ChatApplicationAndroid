package com.sagar.android.chatapp.firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.model.NewChatNotification;
import com.sagar.android.chatapp.notification.NotificationMaster;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.logutilmaster.LogUtil;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PushNotificationHandler extends FirebaseMessagingService {

    @Inject
    LogUtil logUtil;
    @Inject
    Repository repository;
    @Inject
    NotificationMaster notificationMaster;
    @Inject
    Picasso picassoAuthenticated;

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
                case "avatarUpdatedForUser":
                    avatarUpdatedForUser(remoteMessage.getData().get("userId"));
                    break;
                case "newMessage":
                    newMessageReceived(
                            new NewChatNotification(
                                    remoteMessage.getData().get("roomName"),
                                    remoteMessage.getData().get("roomId"),
                                    remoteMessage.getData().get("message"),
                                    remoteMessage.getData().get("authorId"),
                                    remoteMessage.getData().get("authorName"),
                                    remoteMessage.getData().get("createdAt")
                            )
                    );
                    break;
                case "messageReadForRoom":
                    allMessageReadForRoom(
                            remoteMessage.getData().get("roomId")
                    );
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void avatarUpdated() {
        Intent intent = new Intent(
                KeyWordsAndConstants.AVATAR_UPDATED_BROADCAST_ACTION
        );
        sendBroadcast(intent);
    }

    private void pingBack() {
        repository.ping();
    }

    private void avatarUpdatedForUser(String userId) {
        picassoAuthenticated.invalidate(URLs.PROFILE_PICTURE_URL + userId);
        Intent intent = new Intent();
        intent.putExtra(
                "userId",
                userId
        );
        intent.setAction(
                KeyWordsAndConstants.AVATAR_UPDATED_FOR_USER_BROADCAST_ACTION
        );
        sendBroadcast(
                intent
        );
    }

    private void newMessageReceived(NewChatNotification chatNotification) {
        if (!repository.isLoggedIn())
            return;
        notificationMaster.gotNewChat(chatNotification);
    }

    private void allMessageReadForRoom(String roomId) {
        notificationMaster.readAllChatsInRoom(roomId, false);
    }
}
