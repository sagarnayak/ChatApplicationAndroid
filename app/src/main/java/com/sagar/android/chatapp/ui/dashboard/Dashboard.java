package com.sagar.android.chatapp.ui.dashboard;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivityDashboardBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.profile.Profile;
import com.sagar.android.chatapp.util.ColorUtil;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Dashboard extends AppCompatActivity {

    @Inject
    public DashboardViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            binding.drawerLayout.openDrawer(GravityCompat.START);
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
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataLogoutResult
                .observe(
                        this,
                        new Observer<Result>() {
                            @Override
                            public void onChanged(Result result) {
                                if (result != null)
                                    processLogoutResult(result);
                            }
                        }
                );
    }

    private void gotoProfile() {
        startActivity(
                new Intent(this, Profile.class)
        );
        finish();
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
}
