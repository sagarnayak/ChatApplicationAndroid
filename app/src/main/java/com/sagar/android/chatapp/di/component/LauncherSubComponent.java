package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.LauncherModule;
import com.sagar.android.chatapp.di.scope.LauncherScope;
import com.sagar.android.chatapp.ui.launcher.Launcher;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@LauncherScope
@Subcomponent(
        modules = {
                LauncherModule.class
        }
)
public interface LauncherSubComponent extends AndroidInjector<Launcher> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<Launcher> {
    }
}
