package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.ResetPasswordWithOtpSubComponent;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtp;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ResetPasswordWithOtpActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(ResetPasswordWithOtp.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ResetPasswordWithOtpSubComponent.Builder builder);
}