package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.ProfileScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.profile.Profile;
import com.sagar.android.chatapp.ui.profile.ProfileViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {
    @Provides
    @ProfileScope
    ProfileViewModelProvider viewModelProvider(Repository repository) {
        return new ProfileViewModelProvider(repository);
    }

    @Provides
    @ProfileScope
    ProgressUtil progressUtil(Profile context) {
        return new ProgressUtil(context);
    }
}