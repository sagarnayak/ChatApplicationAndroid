package com.sagar.android.chatapp.di.module;

import android.app.Application;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.di.scope.ApplicationScope;
import com.sagar.android.chatapp.repository.retrofit.ApiInterface;
import com.sagar.android.chatapp.util.network.interceptor.OfflineResponseCacheInterceptor;
import com.sagar.android.chatapp.util.network.interceptor.ResponseCacheInterceptor;
import com.sagar.android.logutilmaster.LogUtil;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @ApplicationScope
    ApiInterface androidArchApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    @ApplicationScope
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(URLs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @ApplicationScope
    OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                              ResponseCacheInterceptor responseCacheInterceptor,
                              OfflineResponseCacheInterceptor offlineResponseCacheInterceptor,
                              Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(responseCacheInterceptor)
                .addInterceptor(offlineResponseCacheInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor httpLoggingInterceptor(final LogUtil logUtil) {
        //noinspection NullableProblems
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(@NonNull String message) {
                        logUtil.logV(message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @ApplicationScope
    ResponseCacheInterceptor responseCacheInterceptor(LogUtil logUtil) {
        return new ResponseCacheInterceptor(logUtil);
    }

    @Provides
    @ApplicationScope
    OfflineResponseCacheInterceptor offlineResponseCacheInterceptor(Application application, LogUtil logUtil) {
        return new OfflineResponseCacheInterceptor(application, logUtil);
    }

    @Provides
    @ApplicationScope
    Cache cache(File directory) {
        return new Cache(directory, 5 * 1024 * 1024);
    }

    @Provides
    @ApplicationScope
    File file(Application application) {
        return new File(application.getCacheDir() + "apiResponse");
    }
}
