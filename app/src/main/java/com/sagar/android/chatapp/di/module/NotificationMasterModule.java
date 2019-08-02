package com.sagar.android.chatapp.di.module;

import android.app.Application;
import android.content.SharedPreferences;

import com.sagar.android.chatapp.di.scope.ApplicationScope;
import com.sagar.android.chatapp.notification.NotificationMaster;
import com.sagar.android.chatapp.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationMasterModule {

    @Provides
    @ApplicationScope
    NotificationMaster notificationMaster(
            SharedPreferences preferences,
            Application application,
            Repository repository
    ) {
        return new NotificationMaster(
                preferences,
                application,
                repository
        );
    }
}
