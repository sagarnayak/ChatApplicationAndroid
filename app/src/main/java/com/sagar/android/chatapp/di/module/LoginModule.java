package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.LoginScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.login.LoginViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    @Provides
    @LoginScope
    LoginViewModelProvider viewModelProvider(Repository repository) {
        return new LoginViewModelProvider(repository);
    }

    @Provides
    @LoginScope
    ProgressUtil progressUtil(Login context) {
        return new ProgressUtil(context);
    }
}