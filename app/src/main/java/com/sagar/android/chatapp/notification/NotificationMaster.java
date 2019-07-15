package com.sagar.android.chatapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.model.ChatMinified;
import com.sagar.android.chatapp.model.NewChatNotification;
import com.sagar.android.chatapp.model.RoomMinified;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.service.MessageSenderService;
import com.sagar.android.chatapp.ui.room.ChatRoom;

import java.util.ArrayList;

import static com.sagar.android.chatapp.core.KeyWordsAndConstants.KEY_TEXT_REPLY;

public class NotificationMaster {
    private SharedPreferences preferences;
    private Context context;
    private Repository repository;

    public NotificationMaster(SharedPreferences preferences, Context context, Repository repository) {
        this.preferences = preferences;
        this.context = context;
        this.repository = repository;
    }

    public void gotNewChat(NewChatNotification newChatNotification) {
        ArrayList<RoomMinified> roomMinifieds = getAllChatNotifications();
        int indexForRoom = -1;
        for (RoomMinified room :
                roomMinifieds) {
            if (room.getRoomId().equals(newChatNotification.getRoomId())) {
                indexForRoom = roomMinifieds.indexOf(room);
                break;
            }
        }
        ChatMinified chatMinified = new ChatMinified(
                newChatNotification.getAuthorName(),
                newChatNotification.getAuthorId(),
                newChatNotification.getMessage(),
                newChatNotification.getCreatedAt()
        );
        if (indexForRoom != -1) {
            ArrayList<ChatMinified> chatMinifieds = roomMinifieds.get(indexForRoom).getChats();
            int indexForChat = -1;
            for (ChatMinified chat :
                    chatMinifieds) {
                if (
                        chat.getCalendarCreatedAt().getTimeInMillis() >
                                chatMinified.getCalendarCreatedAt().getTimeInMillis()
                ) {
                    indexForChat = chatMinifieds.indexOf(chat);
                    break;
                }
            }
            if (indexForChat != -1) {
                chatMinifieds.add(indexForChat, chatMinified);
            } else {
                chatMinifieds.add(chatMinified);
            }

            postNewOrUpdateNotification(
                    roomMinifieds.get(indexForRoom),
                    indexForRoom
            );
        } else {
            roomMinifieds.add(
                    new RoomMinified(
                            newChatNotification.getRoomId(),
                            newChatNotification.getRoomName(),
                            new ArrayList<ChatMinified>() {{
                                add(
                                        chatMinified
                                );
                            }}
                    )
            );

            postNewOrUpdateNotification(
                    roomMinifieds.get(roomMinifieds.size() - 1),
                    roomMinifieds.size() - 1
            );
        }

        saveChatNotifications(roomMinifieds);
    }

    private ArrayList<RoomMinified> getAllChatNotifications() {
        if (
                preferences.getString(
                        KeyWordsAndConstants.CHAT_NOTIFICATIONS, ""
                ).equals("")
        )
            return new ArrayList<>();

        return new Gson().fromJson(
                preferences.getString(
                        KeyWordsAndConstants.CHAT_NOTIFICATIONS, ""
                ),
                new TypeToken<ArrayList<RoomMinified>>() {
                }.getType()
        );
    }

    private void saveChatNotifications(ArrayList<RoomMinified> roomMinifieds) {
        preferences.edit()
                .putString(
                        KeyWordsAndConstants.CHAT_NOTIFICATIONS,
                        new Gson().toJson(roomMinifieds)
                )
                .apply();
    }

    private CharSequence channelName = "Chat Application Messages";
    private String channelId = "chat_app_channel_messages_channel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Chat application channel for new chat messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.enableLights(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void readAllChatsInRoom(String roomId, boolean shouldPropagateToSerer) {
        ArrayList<RoomMinified> roomMinifieds = getAllChatNotifications();

        int index = -1;
        for (RoomMinified room :
                roomMinifieds) {
            if (room.getRoomId().equals(roomId)) {
                index = roomMinifieds.indexOf(room);
                break;
            }
        }

        if (index != -1) {
            roomMinifieds.get(index).setChats(new ArrayList<>());

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.cancel(index + 1);
        }

        if (shouldPropagateToSerer)
            repository.readAllNotificationForRoom(roomId);
    }

    private void postNewOrUpdateNotification(RoomMinified roomMinified, int index) {
        Intent intent = new Intent(context, ChatRoom.class);
        intent.putExtra("roomId", roomMinified.getRoomId());
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        123,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        createNotificationChannel();

        String replyLabel = "reply now";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent replyIntent;
        PendingIntent replyPendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            replyIntent = new Intent(context, MessageSenderService.class);
            replyIntent.putExtra("roomId", roomMinified.getRoomId());

            replyPendingIntent =
                    PendingIntent.getService(
                            context,
                            123,
                            replyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
        } else {
            replyIntent = new Intent(context, ChatRoom.class);
            replyIntent.putExtra("roomId", roomMinified.getRoomId());

            replyPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            123,
                            replyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
        }

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.reply_notification_icon_below_n,
                "Reply",
                replyPendingIntent
        )
                .addRemoteInput(remoteInput)
                .build();

        Person me = new Person.Builder()
                .setName(
                        repository.getUserData().getUser().getName().split(" ")[0]
                )
                .build();
        NotificationCompat.MessagingStyle messageStyle =
                new NotificationCompat.MessagingStyle(
                        me
                );

        messageStyle.setConversationTitle(roomMinified.getRoomName());

        messageStyle.setGroupConversation(true);

        for (ChatMinified chat :
                roomMinified.getChats()) {
            messageStyle.addMessage(
                    chat.getMessage(),
                    chat.getCalendarCreatedAt().getTimeInMillis(),
                    new Person.Builder()
                            .setName(chat.getAuthor())
                            .build()
            );
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.app_notification_icon)
                .setColor(Color.parseColor("#FFFFFF"))
                .setContentTitle("title")
                .setContentText("content")
                .setContentIntent(pendingIntent)
                .addAction(action)
                .setStyle(messageStyle)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(index + 1, builder.build());
    }
}
