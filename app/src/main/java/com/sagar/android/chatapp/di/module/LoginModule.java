package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.LoginScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.login.LoginViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@LoginScope
public class LoginModule {
    @Provides
    LoginViewModelProvider viewModelProvider(Repository repository) {
        return new LoginViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(Login context) {
        return new ProgressUtil(context);
    }
}