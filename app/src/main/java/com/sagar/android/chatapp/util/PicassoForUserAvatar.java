package com.sagar.android.chatapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.widget.AppCompatImageView;

import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.core.URLs;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoForUserAvatar {
    private Context context;
    private Picasso picasso;
    private Transformation transformation;
    private AppCompatImageView target;
    private String userId;
    private String userName;
    private CallBack callBack;

    public interface CallBack {
        void pictureChanged();
    }

    public PicassoForUserAvatar(Context context, Picasso picasso,
                                Transformation transformation, AppCompatImageView target, String userId,
                                String userName) {
        this.context = context;
        this.picasso = picasso;
        this.transformation = transformation;
        this.target = target;
        this.userId = userId;
        this.userName = userName;

        registerForBroadcast();

        loadImage();
    }

    public PicassoForUserAvatar(Context context, Picasso picasso, Transformation transformation,
                                AppCompatImageView target, String userId, String userName,
                                CallBack callBack) {
        this.context = context;
        this.picasso = picasso;
        this.transformation = transformation;
        this.target = target;
        this.userId = userId;
        this.userName = userName;
        this.callBack = callBack;

        registerForBroadcast();

        loadImage();
    }

    public Picasso getPicasso() {
        return picasso;
    }

    private void registerForBroadcast() {
        IntentFilter intentFilter = new IntentFilter(
                KeyWordsAndConstants.AVATAR_UPDATED_FOR_USER_BROADCAST_ACTION
        );
        context.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getStringExtra("userId").equals(userId)) {
                            if (callBack != null)
                                callBack.pictureChanged();
                            else
                            loadImage();
                        }
                    }
                },
                intentFilter
        );
    }

    private void loadImage() {
        //noinspection ConstantConditions
        picasso
                .load(
                        URLs.PROFILE_PICTURE_URL + userId
                )
                .transform(
                        transformation
                )
                .placeholder(
                        TextDrawableUtil.getPlaceHolder(
                                userName,
                                TextDrawableUtil.Shape.ROUND
                        )
                )
                .into(
                        target
                );
    }
}
