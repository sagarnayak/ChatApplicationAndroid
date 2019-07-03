package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.CreateRoomSubComponent;
import com.sagar.android.chatapp.ui.create_room.CreateRoom;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class CreateRoomActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(CreateRoom.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(CreateRoomSubComponent.Builder builder);
}