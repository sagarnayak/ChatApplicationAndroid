package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.ProfileScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.profile.Profile;
import com.sagar.android.chatapp.ui.profile.ProfileViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
@ProfileScope
public class ProfileModule {
    @Provides
    ProfileViewModelProvider viewModelProvider(Repository repository) {
        return new ProfileViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(Profile context) {
        return new ProgressUtil(context);
    }

    @Provides
    Picasso picasso(Repository repository, Profile context) {
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