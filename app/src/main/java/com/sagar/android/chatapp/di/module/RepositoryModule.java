package com.sagar.android.chatapp.di.module;

import android.app.Application;
import android.content.SharedPreferences;

import com.sagar.android.chatapp.di.scope.ApplicationScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.repository.retrofit.ApiInterface;
import com.sagar.android.logutilmaster.LogUtil;

import dagger.Module;
import dagger.Provides;

@Module
@ApplicationScope
public class RepositoryModule {

    @Provides
    Repository repository(
            ApiInterface apiInterface,
            SharedPreferences preferences,
            Application application,
            LogUtil logUtil
    ) {
        return new Repository(
                apiInterface, preferences, application, logUtil
        );
    }
}
