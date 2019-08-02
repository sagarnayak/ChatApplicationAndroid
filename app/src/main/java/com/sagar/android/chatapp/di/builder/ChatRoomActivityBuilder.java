package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.ChatRoomSubComponent;
import com.sagar.android.chatapp.ui.room.ChatRoom;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ChatRoomActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(ChatRoom.class)
    abstract AndroidInjector.Factory<?> bind(ChatRoomSubComponent.Factory builder);
}