package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.MessageSenderServiceModule;
import com.sagar.android.chatapp.di.scope.MessageSenderServiceScope;
import com.sagar.android.chatapp.service.MessageSenderService;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@MessageSenderServiceScope
@Subcomponent(
        modules = {
                MessageSenderServiceModule.class
        }
)
public interface MessageSenderServiceSubComponent extends AndroidInjector<MessageSenderService> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MessageSenderService> {
    }
}
