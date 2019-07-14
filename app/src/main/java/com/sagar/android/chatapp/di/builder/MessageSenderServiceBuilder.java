package com.sagar.android.chatapp.di.builder;

import android.app.Service;

import com.sagar.android.chatapp.di.component.MessageSenderServiceSubComponent;
import com.sagar.android.chatapp.service.MessageSenderService;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class MessageSenderServiceBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ServiceKey(MessageSenderService.class)
    abstract AndroidInjector.Factory<? extends Service> bind(MessageSenderServiceSubComponent.Builder builder);
}