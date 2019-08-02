package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.FirebaseMessagingServiceSubComponent;
import com.sagar.android.chatapp.firebase.PushNotificationHandler;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class FirebaseMessagingServiceBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(PushNotificationHandler.class)
    abstract AndroidInjector.Factory<?> bind(FirebaseMessagingServiceSubComponent.Factory builder);
}