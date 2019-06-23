package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.SettingsModule;
import com.sagar.android.chatapp.di.scope.SettingsScope;
import com.sagar.android.chatapp.ui.settings.Settings;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@SettingsScope
@Subcomponent(
        modules = {
                SettingsModule.class
        }
)
public interface SettingsSubComponent extends AndroidInjector<Settings> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<Settings> {
    }
}
