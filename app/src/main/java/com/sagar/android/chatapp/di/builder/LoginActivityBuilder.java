package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.LoginSubComponent;
import com.sagar.android.chatapp.ui.login.Login;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Login.class)
    abstract AndroidInjector.Factory<?> bind(LoginSubComponent.Factory builder);
}