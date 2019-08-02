package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.CreateRoomSubComponent;
import com.sagar.android.chatapp.ui.create_room.CreateRoom;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class CreateRoomActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(CreateRoom.class)
    abstract AndroidInjector.Factory<?> bind(CreateRoomSubComponent.Factory builder);
}