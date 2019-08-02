package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.DashboardSubComponent;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class DashboardActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Dashboard.class)
    abstract AndroidInjector.Factory<?> bind(DashboardSubComponent.Factory builder);
}