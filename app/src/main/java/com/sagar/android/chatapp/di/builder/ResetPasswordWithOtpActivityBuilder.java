package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.ResetPasswordWithOtpSubComponent;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtp;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ResetPasswordWithOtpActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(ResetPasswordWithOtp.class)
    abstract AndroidInjector.Factory<?> bind(ResetPasswordWithOtpSubComponent.Factory builder);
}