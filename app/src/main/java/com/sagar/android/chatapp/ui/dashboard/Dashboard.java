package com.sagar.android.chatapp.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.ActivityDashboardBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.profile.Profile;
import com.sagar.android.chatapp.ui.settings.Settings;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.ColorUtil;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Dashboard extends AppCompatActivity {

    @Inject
    public DashboardViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;
    @Inject
    public Picasso picassoAuthenticated;

    private DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;

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
        setContentView(R.layout.activity_dashboard);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(DashboardViewModel.class);

        bindToViewModel();

        setUpUI();

        IntentFilter intentFilter = new IntentFilter("AvatarUpdated");
        registerReceiver(
                receiver,
                intentFilter
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            binding.drawerLayout.openDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.action_filter_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickProfile(View view) {
        gotoProfile();
    }

    public void onClickChangePassword(View view) {
    }

    public void onClickSetting(View view) {
        gotoSettings();
    }

    public void onClickLogout(View view) {
        logout();
    }

    private void setUpUI() {
        binding.navLayout.textViewUserName.setText(
                viewModel.getUserData().getUser().getName()
        );

        Drawable drawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.user_avatar_bg,
                null
        );
        int color = ColorUtil.MATERIAL.getColor(
                viewModel.getUserData().getUser().getName()
        );
        //noinspection ConstantConditions
        drawable.setColorFilter(
                color, PorterDuff.Mode.ADD
        );
        binding.navLayout.appcompatImageViewUserImage.setImageDrawable(
                drawable
        );
        binding.navLayout.textViewUserInitials.setText(
                String.valueOf(
                        viewModel.getUserData().getUser().getName().toCharArray()[0]
                )
                        .toUpperCase()
        );
        binding.navLayout.textViewUserInitials.setTextColor(
                ColorUtil.MATERIAL.getComplementColorInBlackAndWhite(
                        color
                )
        );
        viewModel.shouldClearCacheForAvatar();
    }

    private void setAvatarToUI(boolean shouldRefreshCache) {
        if (shouldRefreshCache)
            picassoAuthenticated.invalidate(URLs.AVATAR_URL);
        picassoAuthenticated
                .load(
                        URLs.AVATAR_URL
                )
                .transform(
                        new CircleTransformation()
                )
                .into(
                        binding.navLayout.appcompatImageViewUserImage,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.navLayout.textViewUserInitials.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                if (e.toString().contains("401"))
                                    viewModel.notAuthorised();
                            }
                        }
                );
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataLogoutResult
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processLogoutResult(result);
                        }
                );

        viewModel.mediatorLiveDataShouldClearPicassoCacheForAvatar
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processShouldClearPicassoCacheForAvatarResult(result);
                        }
                );
    }

    private void gotoProfile() {
        startActivity(
                new Intent(this, Profile.class)
        );
        finish();
    }

    private void gotoSettings() {
        startActivity(
                new Intent(this, Settings.class)
        );
    }

    private void logout() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        progressUtil.show();
        viewModel.logout();
    }

    private void processLogoutResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.SUCCESS) {
            startActivity(
                    new Intent(this, Login.class)
            );
            finish();
            return;
        }
        DialogUtil.showDialogWithMessage(
                this,
                result.getMessage()
        );
    }

    private void processShouldClearPicassoCacheForAvatarResult(Result result) {
        if (result.getResult() == Enums.Result.SUCCESS)
            setAvatarToUI(true);
        else
            setAvatarToUI(false);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
}
