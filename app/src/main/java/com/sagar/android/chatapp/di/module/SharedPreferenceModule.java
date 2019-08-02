package com.sagar.android.chatapp.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {

    @Provides
    @ApplicationScope
    public SharedPreferences sharedPreferences(Application application) {
        return application.getSharedPreferences(
                KeyWordsAndConstants.SHARED_PREF_DB,
                Context.MODE_PRIVATE
        );
    }
}
