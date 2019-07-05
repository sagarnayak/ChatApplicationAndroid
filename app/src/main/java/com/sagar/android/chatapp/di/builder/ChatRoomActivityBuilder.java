package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.ChatRoomSubComponent;
import com.sagar.android.chatapp.ui.room.ChatRoom;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ChatRoomActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(ChatRoom.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ChatRoomSubComponent.Builder builder);
}