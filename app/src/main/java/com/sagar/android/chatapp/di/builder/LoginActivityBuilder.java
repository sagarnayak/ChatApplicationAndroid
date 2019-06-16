package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.LoginSubComponent;
import com.sagar.android.chatapp.ui.login.Login;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Login.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(LoginSubComponent.Builder builder);
}