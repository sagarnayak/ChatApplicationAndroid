package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.LauncherSubComponent;
import com.sagar.android.chatapp.ui.launcher.Launcher;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class LauncherActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Launcher.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(LauncherSubComponent.Builder builder);
}