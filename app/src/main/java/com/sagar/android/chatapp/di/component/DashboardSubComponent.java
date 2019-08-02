package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.DashboardModule;
import com.sagar.android.chatapp.di.scope.DashboardScope;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@DashboardScope
@Subcomponent(
        modules = {
                DashboardModule.class
        }
)
public interface DashboardSubComponent extends AndroidInjector<Dashboard> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<Dashboard> {
    }
}
