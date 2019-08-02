package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.ForgotPasswordSubComponent;
import com.sagar.android.chatapp.ui.forgot_password.Forgotpassword;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ForgotPasswordActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Forgotpassword.class)
    abstract AndroidInjector.Factory<?> bind(ForgotPasswordSubComponent.Factory builder);
}