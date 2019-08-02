package com.sagar.android.chatapp.di.module;

import android.app.Application;

import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.di.scope.ApplicationScope;
import com.sagar.android.logutilmaster.LogUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class LogUtilModule {

    @Provides
    @ApplicationScope
    LogUtil logUtil(Application application) {
        return new LogUtil.Builder().setCustomLogTag(KeyWordsAndConstants.LOG_TAG).build();
    }
}
