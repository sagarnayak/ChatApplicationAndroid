package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.ForgotPasswordScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.forgot_password.ForgotPasswordViewModelProvider;
import com.sagar.android.chatapp.ui.forgot_password.Forgotpassword;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@ForgotPasswordScope
public class ForgotPasswordModule {
    @Provides
    ForgotPasswordViewModelProvider viewModelProvider(Repository repository) {
        return new ForgotPasswordViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(Forgotpassword context) {
        return new ProgressUtil(context);
    }
}