package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.SignUpScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.sign_up.SignUp;
import com.sagar.android.chatapp.ui.sign_up.SignUpViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@SignUpScope
public class SignUpModule {
    @Provides
    SignUpViewModelProvider viewModelProvider(Repository repository) {
        return new SignUpViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(SignUp context) {
        return new ProgressUtil(context);
    }
}