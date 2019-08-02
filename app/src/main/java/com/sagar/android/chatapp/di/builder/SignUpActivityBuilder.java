package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.SignUpSubComponent;
import com.sagar.android.chatapp.ui.sign_up.SignUp;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class SignUpActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(SignUp.class)
    abstract AndroidInjector.Factory<?> bind(SignUpSubComponent.Factory builder);
}