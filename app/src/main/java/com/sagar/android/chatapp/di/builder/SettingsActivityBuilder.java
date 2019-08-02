package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.SettingsSubComponent;
import com.sagar.android.chatapp.ui.settings.Settings;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class SettingsActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Settings.class)
    abstract AndroidInjector.Factory<?> bind(SettingsSubComponent.Factory builder);
}