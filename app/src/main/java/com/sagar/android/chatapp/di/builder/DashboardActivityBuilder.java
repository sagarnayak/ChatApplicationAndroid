package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.DashboardSubComponent;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class DashboardActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Dashboard.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(DashboardSubComponent.Builder builder);
}