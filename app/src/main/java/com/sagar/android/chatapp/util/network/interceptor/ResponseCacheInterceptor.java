package com.sagar.android.chatapp.util.network.interceptor;
import com.sagar.android.chatapp.util.network.Const;
import com.sagar.android.logutilmaster.LogUtil;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;

@SuppressWarnings("unused")
public class ResponseCacheInterceptor implements Interceptor {
    private LogUtil logUtil;

    public ResponseCacheInterceptor(LogUtil logUtil) {
        this.logUtil = logUtil;
    }

    @Override
    public okhttp3.Response intercept(@SuppressWarnings("NullableProblems") @NonNull Chain chain)
            throws IOException {
        okhttp3.Response originalResponse = chain.proceed(chain.request());
        if (Boolean.valueOf(chain.request().header("cacheResponse"))) {
            logUtil.logV(Const.CACHE_APPLIED);
            originalResponse = originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 600)
                    .build();
        } else {
            logUtil.logV(Const.CACHE_NOT_APPLIED);
        }
        return originalResponse;
    }
}
