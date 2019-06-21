package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.DashboardScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.dashboard.DashboardViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

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

    @Provides
    Picasso picasso(Repository repository, Dashboard context) {
        return new Picasso.Builder(context)
                .downloader(
                        new OkHttp3Downloader(
                                new OkHttpClient.Builder()
                                        .addInterceptor(
                                                chain -> chain.proceed(
                                                        chain.request()
                                                                .newBuilder()
                                                                .addHeader(
                                                                        "Authorization", repository.getAuthToken()
                                                                )
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}