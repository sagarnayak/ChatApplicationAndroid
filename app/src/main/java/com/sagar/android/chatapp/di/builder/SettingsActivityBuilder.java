package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.SettingsSubComponent;
import com.sagar.android.chatapp.ui.settings.Settings;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class SettingsActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Settings.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(SettingsSubComponent.Builder builder);
}