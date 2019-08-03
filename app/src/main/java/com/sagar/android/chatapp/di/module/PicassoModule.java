package com.sagar.android.chatapp.di.module;

import android.app.Application;

import com.sagar.android.chatapp.di.scope.ApplicationScope;
import com.sagar.android.chatapp.repository.Repository;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class PicassoModule {

    @Provides
    @ApplicationScope
    Picasso picasso(Repository repository, Application context) {
        Picasso picasso = new Picasso.Builder(context)
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
                                        ).cache(
                                        new Cache(
                                                context.getCacheDir(),
                                                1024 * 1024 * 40
                                        )
                                )
                                        .build()
                        )
                )
                .build();
//        picasso.setLoggingEnabled(true);
//        picasso.setIndicatorsEnabled(true);

        return picasso;
    }
}
