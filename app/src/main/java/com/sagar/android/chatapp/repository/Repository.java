package com.sagar.android.chatapp.repository;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.model.FcmTokenData;
import com.sagar.android.chatapp.model.LoginRequest;
import com.sagar.android.chatapp.model.ResetPasswordRequest;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.model.UserSignUpRequest;
import com.sagar.android.chatapp.repository.retrofit.ApiInterface;
import com.sagar.android.chatapp.ui.launcher.Launcher;
import com.sagar.android.logutilmaster.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class Repository {
    private ApiInterface apiInterface;
    private SharedPreferences preferences;
    private Application application;
    private LogUtil logUtil;

    public MutableLiveData<Result> mutableLiveDataSignUpResult;
    public MutableLiveData<Result> mutableLiveDataLoginResult;
    public MutableLiveData<Result> mutableLiveDataForgotPasswordResult;
    public MutableLiveData<Result> mutableLiveDataResetPasswordResult;
    public MutableLiveData<Result> mutableLiveDataLogoutResult;
    public MutableLiveData<Result> mutableLiveDataUpdateAvatarResult;
    public MutableLiveData<Result> mutableLiveDataShouldClearPicassoCacheForAvatar;
    public MutableLiveData<Result> mutableLiveDataLogoutAllResult;

    public Repository(
            ApiInterface apiInterface,
            SharedPreferences preferences,
            Application application,
            LogUtil logUtil
    ) {
        this.apiInterface = apiInterface;
        this.preferences = preferences;
        this.application = application;
        this.logUtil = logUtil;

        mutableLiveDataSignUpResult = new MutableLiveData<>();
        mutableLiveDataLoginResult = new MutableLiveData<>();
        mutableLiveDataForgotPasswordResult = new MutableLiveData<>();
        mutableLiveDataResetPasswordResult = new MutableLiveData<>();
        mutableLiveDataLogoutResult = new MutableLiveData<>();
        mutableLiveDataUpdateAvatarResult = new MutableLiveData<>();
        mutableLiveDataShouldClearPicassoCacheForAvatar = new MutableLiveData<>();
        mutableLiveDataLogoutAllResult = new MutableLiveData<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Api Calls
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void signUp(UserSignUpRequest userSignUpRequest) {
        apiInterface.signUp(
                userSignUpRequest
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 201) {
                                    try {
                                        //noinspection ConstantConditions
                                        saveUserData(
                                                new Gson().fromJson(
                                                        responseBodyResponse.body().string(),
                                                        UserData.class
                                                )
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mutableLiveDataSignUpResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS, ""
                                            )
                                    );
                                } else {
                                    mutableLiveDataSignUpResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );
                                }

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            mutableLiveDataSignUpResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataSignUpResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataSignUpResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void login(LoginRequest loginRequest) {
        apiInterface.login(
                loginRequest
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 200) {
                                    try {
                                        //noinspection ConstantConditions
                                        saveUserData(
                                                new Gson().fromJson(
                                                        responseBodyResponse.body().string(),
                                                        UserData.class
                                                )
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mutableLiveDataLoginResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS, ""
                                            )
                                    );
                                } else {
                                    mutableLiveDataLoginResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );
                                }

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            mutableLiveDataLoginResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataLoginResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataLoginResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void forgotPassword(String emailId) {
        apiInterface.forgotPassword(
                emailId
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 200) {
                                    mutableLiveDataForgotPasswordResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    "Otp is sent to your email"
                                            )
                                    );
                                } else {
                                    mutableLiveDataForgotPasswordResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );
                                }

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            mutableLiveDataForgotPasswordResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataForgotPasswordResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataForgotPasswordResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        apiInterface.resetPassword(
                resetPasswordRequest
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 200) {
                                    mutableLiveDataResetPasswordResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    "Your password has been reset successfully"
                                            )
                                    );
                                } else {
                                    mutableLiveDataResetPasswordResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );
                                }

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            mutableLiveDataResetPasswordResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataResetPasswordResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataResetPasswordResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void logout() {
        apiInterface.logout(
                getAuthToken()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    clearAllData();
                                    mutableLiveDataLogoutResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    ""
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mutableLiveDataLogoutResult.postValue(null);
                                            }
                                    ).start();
                                } else {
                                    mutableLiveDataLogoutResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mutableLiveDataLogoutResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataLogoutResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataLogoutResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void updateAvatar(MultipartBody.Part picture) {
        apiInterface.updateAvatar(
                getAuthToken(),
                picture
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    mutableLiveDataUpdateAvatarResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    ""
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mutableLiveDataUpdateAvatarResult.postValue(null);
                                            }
                                    ).start();
                                } else {
                                    mutableLiveDataUpdateAvatarResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(
                                                            responseBodyResponse.errorBody()
                                                    )
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mutableLiveDataUpdateAvatarResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataUpdateAvatarResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(
                                                        e
                                                )
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataUpdateAvatarResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void tryUpdatingFCMToken(String token) {
        if (!isLoggedIn())
            return;

        apiInterface.updateFCMToken(
                getAuthToken(),
                new FcmTokenData(token)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200)
                                    logUtil.logV("updated fcm token");
                                else
                                    logUtil.logV("failed to update fcm token");
                            }

                            @Override
                            public void onError(Throwable e) {
                                logUtil.logV("failed to update fcm token : " + e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void ping() {
        apiInterface.ping(
                getAuthToken()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401)
                                    notAuthorised();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void shouldClearPicassoCacheForAvatar() {
        apiInterface.avatarUpdateTimeStamp(
                getAuthToken()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    try {
                                        //noinspection ConstantConditions
                                        JSONObject jsonObject = new JSONObject(responseBodyResponse.body().string());

                                        //noinspection ConstantConditions
                                        if (
                                                !preferences.getString(
                                                        KeyWordsAndConstants.AVATAR_UPDATE_TIMESTAMP,
                                                        ""
                                                ).equalsIgnoreCase(
                                                        jsonObject.getString("timeStamp")
                                                )
                                        ) {
                                            mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(
                                                    new Result(
                                                            Enums.Result.SUCCESS, ""
                                                    )
                                            );
                                        } else {
                                            mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(
                                                    new Result(
                                                            Enums.Result.FAIL, ""
                                                    )
                                            );
                                        }

                                        preferences.edit()
                                                .putString(
                                                        KeyWordsAndConstants.AVATAR_UPDATE_TIMESTAMP,
                                                        jsonObject.getString("timeStamp")
                                                ).apply();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(
                                                new Result(
                                                        Enums.Result.FAIL, ""
                                                )
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(
                                                new Result(
                                                        Enums.Result.FAIL, ""
                                                )
                                        );
                                    }
                                } else //noinspection StatementWithEmptyBody
                                    if (responseBodyResponse.code() == 404) {
                                        //the picture is not present
                                    } else {
                                        mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(
                                                new Result(
                                                        Enums.Result.FAIL, ""
                                                )
                                        );
                                    }

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataShouldClearPicassoCacheForAvatar.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void logoutAll() {
        apiInterface.logoutAll(
                getAuthToken()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 401 || responseBodyResponse.code() == 200)
                                    notAuthorised();
                                else {
                                    mutableLiveDataLogoutAllResult.postValue(
                                            new Result(
                                                    Enums.Result.FAIL,
                                                    getErrorMessage(responseBodyResponse.errorBody())
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataLogoutAllResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mutableLiveDataLogoutAllResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(e)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataLogoutAllResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Public Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isLoggedIn() {
        return preferences.getBoolean(
                KeyWordsAndConstants.IS_LOGGED_IN,
                false
        );
    }

    public void notAuthorised() {
        clearAllData();

        new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    restartApp();
                }
        ).start();
    }

    public UserData getUserData() {
        try {
            return new Gson().fromJson(
                    preferences.getString(
                            KeyWordsAndConstants.USER_DATA,
                            ""
                    ),
                    UserData.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new UserData();
        }
    }

    public String getAuthToken() {
        return "Bearer " + getToken();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Private Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void restartApp() {
        Intent intent = new Intent(application, Launcher.class);
        int mPendingIntentId = 123;
        PendingIntent mPendingIntent =
                PendingIntent.getActivity(
                        application,
                        mPendingIntentId,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        if (mgr != null) {
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        }
        System.exit(0);
    }

    private void saveUserData(UserData userData) {
        preferences.edit()
                .putString(
                        KeyWordsAndConstants.USER_DATA,
                        new Gson().toJson(userData)
                )
                .apply();
        loggedIn();
    }

    private void loggedIn() {
        preferences.edit()
                .putBoolean(
                        KeyWordsAndConstants.IS_LOGGED_IN,
                        true
                )
                .apply();
    }

    private void clearAllData() {
        preferences.edit().clear().apply();
    }

    private String getToken() {
        return getUserData().getToken();
    }

    private String getErrorMessage(Throwable throwable) {
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
            try {
                //noinspection ConstantConditions
                JSONObject jsonObject = new JSONObject(responseBody.string());
                return jsonObject.getString("error");
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (throwable instanceof SocketTimeoutException) {
            return "Timeout occurred";
        } else if (throwable instanceof IOException) {
            return "network error";
        } else {
            return throwable.getMessage();
        }
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("error");
        } catch (Exception e) {
            return "Something went wrong.";
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
