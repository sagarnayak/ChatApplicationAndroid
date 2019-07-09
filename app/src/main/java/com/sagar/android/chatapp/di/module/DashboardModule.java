package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.DashboardScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.dashboard.DashboardViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@DashboardScope
public class DashboardModule {
    @Provides
    DashboardViewModelProvider viewModelProvider(Repository repository) {
        return new DashboardViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(Dashboard context) {
        return new ProgressUtil(context);
    }
}