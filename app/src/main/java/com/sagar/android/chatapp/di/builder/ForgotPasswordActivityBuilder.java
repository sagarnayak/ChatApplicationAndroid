package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.ForgotPasswordSubComponent;
import com.sagar.android.chatapp.ui.forgot_password.Forgotpassword;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ForgotPasswordActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Forgotpassword.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ForgotPasswordSubComponent.Builder builder);
}