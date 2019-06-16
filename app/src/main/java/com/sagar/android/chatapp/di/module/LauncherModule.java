package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.LauncherScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.launcher.LauncherViewModelProvider;

import dagger.Module;
import dagger.Provides;

@Module
@LauncherScope
public class LauncherModule {
    @Provides
    LauncherViewModelProvider viewModelProvider(Repository repository) {
        return new LauncherViewModelProvider(repository);
    }
}