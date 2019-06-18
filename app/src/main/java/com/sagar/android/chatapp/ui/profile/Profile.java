package com.sagar.android.chatapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
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
import com.sagar.android.chatapp.databinding.ActivityProfileBinding;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.util.ColorUtil;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.logutilmaster.LogUtil;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
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
    public LogUtil logUtil;

    private ProfileViewModel viewModel;
    private ActivityProfileBinding binding;

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
        changeDp();
    }

    private void bindToViewModel() {
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
                Bitmap bitmap = new BitmapDrawable(
                        getResources(),
                        file.getAbsolutePath()
                )
                        .getBitmap();
                binding.contentProfile.appcompatImageViewUserImage.setImageBitmap(bitmap);
            }
        }
    }

    private void setPictureToUI(){}
}
