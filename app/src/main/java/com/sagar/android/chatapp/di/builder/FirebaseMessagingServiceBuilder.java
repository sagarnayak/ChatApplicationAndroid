package com.sagar.android.chatapp.di.builder;

import android.app.Service;

import com.sagar.android.chatapp.di.component.FirebaseMessagingServiceSubComponent;
import com.sagar.android.chatapp.firebase.PushNotificationHandler;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class FirebaseMessagingServiceBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ServiceKey(PushNotificationHandler.class)
    abstract AndroidInjector.Factory<? extends Service> bind(FirebaseMessagingServiceSubComponent.Builder builder);
}