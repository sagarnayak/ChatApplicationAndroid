package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.LauncherSubComponent;
import com.sagar.android.chatapp.ui.launcher.Launcher;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class LauncherActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Launcher.class)
    abstract AndroidInjector.Factory<?> bind(LauncherSubComponent.Factory builder);
}