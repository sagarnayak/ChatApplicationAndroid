package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.SignUpSubComponent;
import com.sagar.android.chatapp.ui.sign_up.SignUp;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class SignUpActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(SignUp.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(SignUpSubComponent.Builder builder);
}