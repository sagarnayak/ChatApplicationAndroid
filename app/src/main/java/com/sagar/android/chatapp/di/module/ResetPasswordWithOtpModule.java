package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.ResetPasswordWithOtpScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtp;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtpViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@ResetPasswordWithOtpScope
public class ResetPasswordWithOtpModule {
    @Provides
    ResetPasswordWithOtpViewModelProvider viewModelProvider(Repository repository) {
        return new ResetPasswordWithOtpViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(ResetPasswordWithOtp context) {
        return new ProgressUtil(context);
    }
}