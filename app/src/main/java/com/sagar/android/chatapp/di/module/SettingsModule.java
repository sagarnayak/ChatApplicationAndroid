package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.SettingsScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.settings.Settings;
import com.sagar.android.chatapp.ui.settings.SettingsViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    @Provides
    @SettingsScope
    SettingsViewModelProvider viewModelProvider(Repository repository) {
        return new SettingsViewModelProvider(repository);
    }

    @Provides
    @SettingsScope
    ProgressUtil progressUtil(Settings context) {
        return new ProgressUtil(context);
    }
}