package com.sagar.android.chatapp.repository.retrofit;

import com.sagar.android.chatapp.model.FcmTokenData;
import com.sagar.android.chatapp.model.LoginRequest;
import com.sagar.android.chatapp.model.ResetPasswordRequest;
import com.sagar.android.chatapp.model.UserSignUpRequest;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("signup")
    Observable<Response<ResponseBody>> signUp(
            @Body UserSignUpRequest userSignUpRequest
    );

    @POST("login")
    Observable<Response<ResponseBody>> login(
            @Body LoginRequest loginRequest
    );

    @GET("forgotPassword/{emailId}")
    Observable<Response<ResponseBody>> forgotPassword(
            @Path("emailId") String emailId
    );

    @POST("resetPassword")
    Observable<Response<ResponseBody>> resetPassword(
            @Body ResetPasswordRequest resetPasswordRequest
    );

    @POST("logout")
    Observable<Response<ResponseBody>> logout(
            @Header("Authorization") String authHeader
    );

    @PATCH("updateAvatar")
    @Multipart
    Observable<Response<ResponseBody>> updateAvatar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part userPicture
    );

    @PATCH("updateFCMToken")
    Observable<Response<ResponseBody>> updateFCMToken(
            @Header("Authorization") String authHeader,
            @Body FcmTokenData fcmToken
    );

    @GET("ping")
    Observable<Response<ResponseBody>> ping(
            @Header("Authorization") String authHeader
    );

    @GET("avatarUpdateTimeStamp")
    Observable<Response<ResponseBody>> avatarUpdateTimeStamp(
            @Header("Authorization") String authHeader
    );
}
