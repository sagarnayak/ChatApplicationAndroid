package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.ResetPasswordWithOtpModule;
import com.sagar.android.chatapp.di.scope.ResetPasswordWithOtpScope;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtp;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ResetPasswordWithOtpScope
@Subcomponent(
        modules = {
                ResetPasswordWithOtpModule.class
        }
)
public interface ResetPasswordWithOtpSubComponent extends AndroidInjector<ResetPasswordWithOtp> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<ResetPasswordWithOtp> {
    }
}
