package com.sagar.android.chatapp.repository;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.core.SocketEvent;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.model.Chat;
import com.sagar.android.chatapp.model.FcmTokenData;
import com.sagar.android.chatapp.model.GetChatsRequest;
import com.sagar.android.chatapp.model.JoinRoomRequest;
import com.sagar.android.chatapp.model.LoginRequest;
import com.sagar.android.chatapp.model.NewMessageReq;
import com.sagar.android.chatapp.model.ResetPasswordRequest;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.model.UserSignUpRequest;
import com.sagar.android.chatapp.model.createRoomRequest;
import com.sagar.android.chatapp.model.searchUserResuest;
import com.sagar.android.chatapp.repository.retrofit.ApiInterface;
import com.sagar.android.chatapp.ui.launcher.Launcher;
import com.sagar.android.logutilmaster.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

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
    public MutableLiveData<ArrayList<Room>> mutableLiveDataAllRooms;
    public MutableLiveData<Result> mutableLiveDataAllRoomsError;
    public MutableLiveData<ArrayList<Room>> mutableLiveDataRoomSearchResult;
    public MutableLiveData<ArrayList<User>> mutableLiveDataUserSearchResult;
    public MutableLiveData<Result> mutableLiveDataCreateRoomResult;
    public MutableLiveData<Result> mutableLiveDataLeaveRoomResult;
    public MutableLiveData<Room> mutableLiveDataJoinRoomResult;
    public MutableLiveData<Result> mutableLiveDataJoinRoomError;
    public MutableLiveData<Result> mutableLiveDataConnectedToSocket;
    public MutableLiveData<ArrayList<Chat>> mutableLiveDataChats;

    private ArrayList<Disposable> searchRoomDisposablesList;

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
        mutableLiveDataAllRooms = new MutableLiveData<>();
        mutableLiveDataAllRoomsError = new MutableLiveData<>();
        mutableLiveDataRoomSearchResult = new MutableLiveData<>();
        mutableLiveDataUserSearchResult = new MutableLiveData<>();
        mutableLiveDataCreateRoomResult = new MutableLiveData<>();
        mutableLiveDataLeaveRoomResult = new MutableLiveData<>();
        mutableLiveDataJoinRoomResult = new MutableLiveData<>();
        mutableLiveDataJoinRoomError = new MutableLiveData<>();
        mutableLiveDataConnectedToSocket = new MutableLiveData<>();
        mutableLiveDataChats = new MutableLiveData<>();

        searchRoomDisposablesList = new ArrayList<>();
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

    public void getAllRooms() {
        apiInterface.getRooms(
                getAuthToken(),
                "", "", ""
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    try {
                                        //noinspection ConstantConditions
                                        ArrayList<Room> rooms = new Gson().fromJson(
                                                responseBodyResponse.body().string(),
                                                new TypeToken<ArrayList<Room>>() {
                                                }.getType()
                                        );

                                        mutableLiveDataAllRooms.postValue(
                                                rooms
                                        );

                                        new Thread(
                                                () -> {
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mutableLiveDataAllRooms.postValue(null);
                                                }
                                        ).start();
                                    } catch (IOException e) {
                                        e.printStackTrace();

                                        mutableLiveDataAllRoomsError.postValue(
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
                                                    mutableLiveDataAllRoomsError.postValue(null);
                                                }
                                        ).start();
                                    }
                                } else if (responseBodyResponse.code() == 204) {
                                    mutableLiveDataAllRooms.postValue(
                                            new ArrayList<>()
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataAllRooms.postValue(null);
                                            }
                                    ).start();
                                } else {
                                    mutableLiveDataAllRoomsError.postValue(
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
                                                mutableLiveDataAllRoomsError.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                mutableLiveDataAllRoomsError.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(throwable)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataAllRoomsError.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void searchRooms(
            String searchString,
            String limit,
            String skip
    ) {
        disposeAllPendingSearchRoomRequest();
        apiInterface.getRooms(
                getAuthToken(),
                searchString,
                limit,
                skip
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {
                                searchRoomDisposablesList.add(disposable);
                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    try {
                                        //noinspection ConstantConditions
                                        ArrayList<Room> rooms = new Gson().fromJson(
                                                responseBodyResponse.body().string(),
                                                new TypeToken<ArrayList<Room>>() {
                                                }.getType()
                                        );

                                        mutableLiveDataRoomSearchResult.postValue(
                                                rooms
                                        );

                                        new Thread(
                                                () -> {
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mutableLiveDataRoomSearchResult.postValue(null);
                                                }
                                        ).start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (responseBodyResponse.code() == 204) {

                                    mutableLiveDataRoomSearchResult.postValue(
                                            new ArrayList<>()
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataRoomSearchResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    private void disposeAllPendingSearchRoomRequest() {
        for (Disposable d :
                searchRoomDisposablesList) {
            if (!d.isDisposed())
                d.dispose();
        }

        searchRoomDisposablesList.clear();
    }

    public void searchUser(
            String containing,
            String limit,
            String skip,
            ArrayList<String> alreadyUsed
    ) {
        apiInterface.searchUser(
                getAuthToken(),
                new searchUserResuest(
                        containing,
                        limit,
                        skip,
                        alreadyUsed
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    try {
                                        //noinspection ConstantConditions
                                        ArrayList<User> users = new Gson().fromJson(
                                                responseBodyResponse.body().string(),
                                                new TypeToken<ArrayList<User>>() {
                                                }.getType()
                                        );

                                        mutableLiveDataUserSearchResult.postValue(users);

                                        new Thread(
                                                () -> {
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mutableLiveDataUserSearchResult.postValue(null);
                                                }
                                        ).start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (responseBodyResponse.code() == 204) {
                                    mutableLiveDataUserSearchResult.postValue(
                                            new ArrayList<>()
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataUserSearchResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void createRoom(
            String name,
            ArrayList<String> members
    ) {
        apiInterface.createRoom(
                getAuthToken(),
                new createRoomRequest(
                        name,
                        members
                )
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (responseBodyResponse.code() == 200) {
                                    mutableLiveDataCreateRoomResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    ""
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataCreateRoomResult.postValue(null);
                                            }
                                    ).start();
                                } else {
                                    mutableLiveDataCreateRoomResult.postValue(
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
                                                mutableLiveDataCreateRoomResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                mutableLiveDataCreateRoomResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(throwable)
                                        )
                                );

                                new Thread(
                                        () -> {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                            mutableLiveDataCreateRoomResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void leaveRoom(String roomId) {
        apiInterface.leaveRoom(
                getAuthToken(),
                roomId
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (
                                        responseBodyResponse.code() == 200
                                ) {
                                    mutableLiveDataLeaveRoomResult.postValue(
                                            new Result(
                                                    Enums.Result.SUCCESS,
                                                    ""
                                            )
                                    );

                                    new Thread(
                                            () -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataLeaveRoomResult.postValue(null);
                                            }
                                    ).start();
                                } else {
                                    mutableLiveDataLeaveRoomResult.postValue(
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
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataLeaveRoomResult.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                mutableLiveDataLeaveRoomResult.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(
                                                        throwable
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
                                            mutableLiveDataLeaveRoomResult.postValue(null);
                                        }
                                ).start();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public void joinRoom(String roomId) {
        apiInterface.joinRoom(
                getAuthToken(),
                new JoinRoomRequest(roomId)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {
                                if (responseBodyResponse.code() == 404)
                                    notAuthorised();
                                else if (
                                        responseBodyResponse.code() == 200
                                ) {
                                    try {
                                        //noinspection ConstantConditions
                                        mutableLiveDataJoinRoomResult.postValue(
                                                new Gson().fromJson(
                                                        responseBodyResponse.body().string(),
                                                        Room.class
                                                )
                                        );

                                        new Thread(
                                                () -> {
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    mutableLiveDataJoinRoomResult.postValue(null);
                                                }
                                        ).start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        mutableLiveDataJoinRoomError.postValue(
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
                                                    mutableLiveDataJoinRoomError.postValue(null);
                                                }
                                        ).start();
                                    }
                                } else {
                                    mutableLiveDataJoinRoomError.postValue(
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
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                mutableLiveDataJoinRoomError.postValue(null);
                                            }
                                    ).start();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                mutableLiveDataJoinRoomError.postValue(
                                        new Result(
                                                Enums.Result.FAIL,
                                                getErrorMessage(
                                                        throwable
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
                                            mutableLiveDataJoinRoomError.postValue(null);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Sockets
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Socket socket;

    public void prepareSockets() {
        try {
            if (socket == null) {
                IO.Options options = new IO.Options();
                options.forceNew = true;
                options.query = "token=" + getAuthToken();
                socket = IO.socket(URLs.BASE_URL, options);

                socket.on(
                        SocketEvent.CONNECT,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logUtil.logV("socket connected");
                            }
                        }
                );

                socket.on(
                        SocketEvent.DISCONNECT,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logUtil.logV("socket disconnected");
                            }
                        }
                );

                socket.on(
                        SocketEvent.ERROR,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logUtil.logV("socket auth" + Arrays.toString(args));
                                for (Object obj :
                                        args) {
                                    if (String.valueOf(obj).contains("auth error")) {
                                        notAuthorised();
                                        break;
                                    }
                                }
                            }
                        }
                );

                socket.on(
                        SocketEvent.YOU_ARE_CONNECTED,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... objects) {
                                connectedToSocket();
                            }
                        }
                );

                socket.on(
                        SocketEvent.NEW_MESSAGE,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logUtil.logV("got new message : " + Arrays.toString(args));
                                gotNewChat(args);
                            }
                        }
                );

                socket.on(
                        SocketEvent.NEW_MESSAGES,
                        new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logUtil.logV("got new messages : " + Arrays.toString(args));
                                gotNewChats(args);
                            }
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (socket.connected())
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            logUtil.logV("socket connection failed : " + e.toString());
        }
    }

    private void connectedToSocket() {
        mutableLiveDataConnectedToSocket.postValue(
                new Result()
        );

        new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    mutableLiveDataConnectedToSocket.postValue(null);
                }
        ).start();
    }

    public void disconnectSocket() {
        try {
            socket.disconnect();
            socket.off();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getChatData(
            String limit,
            String skip,
            String roomId
    ) {
        if (socket == null || !socket.connected())
            return;
        socket.emit(
                SocketEvent.GET_CHAT_DATA,
                new Gson().toJson(
                        new GetChatsRequest(
                                limit, skip, roomId
                        )
                )
        );
    }

    public void sendMessage(
            String message,
            String roomId
    ) {
        socket.emit(
                SocketEvent.SEND_NEW_MESSAGE,
                new Gson().toJson(
                        new NewMessageReq(
                                message,
                                roomId
                        )
                )
        );
    }

    public void joinRoomSocket(
            String roomId
    ) {
        socket.emit(
                SocketEvent.JOIN_ROOM,
                roomId
        );
    }

    public void gotNewChat(Object... chats) {
        ArrayList<Chat> chatToSend = new Gson().fromJson(
                Arrays.toString(chats),
                new TypeToken<ArrayList<Chat>>() {
                }.getType()
        );

        mutableLiveDataChats.postValue(chatToSend);

        new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    mutableLiveDataChats.postValue(null);
                }
        ).start();
    }

    public void gotNewChats(Object... chats) {
        ArrayList<ArrayList<Chat>> result = new Gson().fromJson(
                Arrays.toString(chats),
                new TypeToken<ArrayList<ArrayList<Chat>>>() {
                }.getType()
        );

        mutableLiveDataChats.postValue(result.get(0));

        new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    mutableLiveDataChats.postValue(null);
                }
        ).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
