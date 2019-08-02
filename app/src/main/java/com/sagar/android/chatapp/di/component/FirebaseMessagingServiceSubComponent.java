package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.FirebaseMessagingServiceModule;
import com.sagar.android.chatapp.di.scope.FirebaseMessagingServiceScope;
import com.sagar.android.chatapp.firebase.PushNotificationHandler;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@FirebaseMessagingServiceScope
@Subcomponent(
        modules = {
                FirebaseMessagingServiceModule.class
        }
)
public interface FirebaseMessagingServiceSubComponent extends AndroidInjector<PushNotificationHandler> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<PushNotificationHandler> {
    }
}
