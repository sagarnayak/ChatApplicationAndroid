package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.MessageSenderServiceSubComponent;
import com.sagar.android.chatapp.service.MessageSenderService;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class MessageSenderServiceBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(MessageSenderService.class)
    abstract AndroidInjector.Factory<?> bind(MessageSenderServiceSubComponent.Factory builder);
}