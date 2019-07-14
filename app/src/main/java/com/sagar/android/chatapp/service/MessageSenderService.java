package com.sagar.android.chatapp.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.core.app.RemoteInput;

import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.logutilmaster.LogUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MessageSenderService extends IntentService {

    @Inject
    public Repository repository;
    @Inject
    public LogUtil logUtil;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    public MessageSenderService() {
        super("MessageSenderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            repository.sendMessageToServer(
                    intent.getStringExtra("roomId"),
                    RemoteInput.getResultsFromIntent(intent).getCharSequence(
                            KeyWordsAndConstants.KEY_TEXT_REPLY
                    ).toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
