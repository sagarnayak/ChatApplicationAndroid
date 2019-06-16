package com.sagar.android.chatapp.util.network.interceptor;

import android.content.Context;

import com.sagar.android.chatapp.util.network.Const;
import com.sagar.android.chatapp.util.network.NetworkUtil;
import com.sagar.android.logutilmaster.LogUtil;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;

@SuppressWarnings("unused")
public class OfflineResponseCacheInterceptor implements Interceptor {
    private Context context;
    private LogUtil logUtil;

    public OfflineResponseCacheInterceptor(Context context, LogUtil logUtil) {
        this.context = context;
        this.logUtil = logUtil;
    }

    @Override
    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (Boolean.valueOf(chain.request().header("cacheResponseOffline"))) {
            if (!NetworkUtil.isNetworkAvailable(context)) {
                logUtil.logV(Const.OFFLINE_CACHE_APPLIED);
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + 2419200)
                        .build();
            }
        } else {
            logUtil.logV(Const.OFFLINE_CACHE_NOT_APPLIED);
        }
        return chain.proceed(request);
    }
}