package com.sagar.android.chatapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.ActivityProfileBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.ColorUtil;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.logutilmaster.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class Profile extends AppCompatActivity {

    @Inject
    public ProfileViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;
    @Inject
    public Picasso picassoAuthenticated;
    @Inject
    public LogUtil logUtil;

    private ProfileViewModel viewModel;
    private ActivityProfileBinding binding;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.shouldClearCacheForAvatar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(ProfileViewModel.class);

        bindToViewModel();

        setUpUI();

        viewModel.shouldClearCacheForAvatar();

        IntentFilter intentFilter = new IntentFilter("AvatarUpdated");
        registerReceiver(
                receiver,
                intentFilter
        );
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            backPressed();
        return super.onOptionsItemSelected(item);
    }

    private void backPressed() {
        startActivity(
                new Intent(this, Dashboard.class)
        );
        finish();
    }

    private void setUpUI() {
        UserData userData = viewModel.getUserData();

        Drawable drawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.user_avatar_bg,
                null
        );
        int color = ColorUtil.MATERIAL.getColor(userData.getUser().getName());
        drawable.setColorFilter(
                color, PorterDuff.Mode.ADD
        );
        binding.contentProfile.appcompatImageViewUserImage.setImageDrawable(
                drawable
        );
        binding.contentProfile.textViewUserInitials.setText(
                String.valueOf(
                        userData.getUser().getName().toCharArray()[0]
                )
                        .toUpperCase()
        );
        binding.contentProfile.textViewUserInitials.setTextColor(
                ColorUtil.MATERIAL.getComplementColorInBlackAndWhite(
                        color
                )
        );
        binding.contentProfile.textViewUserName.setText(
                userData.getUser().getName()
        );
        binding.contentProfile.textViewUserAge.setText(
                String.valueOf(
                        userData.getUser().getAge()
                )
        );
        binding.contentProfile.textViewUserEmail.setText(
                userData.getUser().getEmail()
        );
    }

    public void onClickEditPicture(View view) {
       ProfilePermissionsDispatcher.changeDpWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ProfilePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(
            {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }
    )
    public void changeDp() {
        Options options = Options.init()
                .setRequestCode(100)
                .setCount(1)
                .setFrontfacing(true)
                .setImageQuality(ImageQuality.HIGH)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
                .setPath("/ChatApp/UserPicture");

        Pix.start(this, options);
    }

    @OnShowRationale(
            {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }
    )
    public void rationaleForChangeDp() {
        DialogUtil.showDialogWithMessage(
                this,
                "We need few permission from you to take picture from your camera and help you change your profile picture."
        );
    }

    @OnPermissionDenied(
            {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }
    )
    public void permissionDeniedForChangeDp() {
        DialogUtil.showDialogWithMessage(
                this,
                "We need few permission from you to take picture from your camera and help you change your profile picture. You may not able to change your profile picture as you have denied to the required permission."
        );
    }

    @OnNeverAskAgain(
            {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }
    )
    public void NeverAskAgainForChangeDp() {
        DialogUtil.showDialogWithMessage(
                this,
                "We need few permission from you to take picture from your camera and help you change your profile picture. You may not able to change your profile picture as you have denied to the required permission. You can allow for the permissions by going to application settings."
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (String str :
                    returnValue) {
                logUtil.logV(str);
                File file = new File(str);

                sendPictureToServer(file);
            }
        }
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataUpdateAvatarResult
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processUpdateAvatarResult(result);
                        }
                );

        viewModel.mediatorLiveDataShouldClearPicassoCacheForAvatar
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processShouldClearCacheForAvatarResult(result);
                        }
                );
    }

    private void sendPictureToServer(File file) {
        progressUtil.show();
        okhttp3.RequestBody reqFilePic = RequestBody.create(
                MediaType.parse("image/*"),
                file
        );
        MultipartBody.Part bodyPic = MultipartBody.Part.createFormData("avatar", file.getName(), reqFilePic);

        viewModel.updateAvatar(bodyPic);
    }

    private void processUpdateAvatarResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
            return;
        }
        setPictureToUI(true);
    }

    private void setPictureToUI(boolean shouldRefreshCache) {
        if (shouldRefreshCache)
            picassoAuthenticated.invalidate(URLs.MY_AVATAR_URL);
        picassoAuthenticated
                .load(
                        URLs.MY_AVATAR_URL
                )
                .transform(
                        new CircleTransformation()
                )
                .into(
                        binding.contentProfile.appcompatImageViewUserImage,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.contentProfile.textViewUserInitials.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                if (e.toString().contains("401"))
                                    viewModel.notAuthorised();
                            }
                        }
                );
    }

    private void processShouldClearCacheForAvatarResult(Result result) {
        if (result.getResult() == Enums.Result.SUCCESS) {
            setPictureToUI(true);
        } else {
            setPictureToUI(false);
        }
    }
}
