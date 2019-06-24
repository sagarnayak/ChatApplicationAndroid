package com.sagar.android.chatapp.ui.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private MenuItem mSearchItem;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchItem = menu.findItem(R.id.m_search);

        mSearchItem.setOnActionExpandListener(
                new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Called when SearchView is expanding
                        animateSearchToolbar(1, true, true);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // Called when SearchView is collapsing
                        if (mSearchItem.isActionViewExpanded()) {
                            animateSearchToolbar(1, false, false);
                        }
                        return true;
                    }
                }
        );

        return true;
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        binding.drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.quantum_grey_600));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = binding.toolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(binding.toolbar,
                        isRtl(getResources()) ? binding.toolbar.getWidth() - width : width, binding.toolbar.getHeight() / 2,
                        0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(0.0f, 0.0f, (float) (-binding.toolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                binding.toolbar.clearAnimation();
                binding.toolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = binding.toolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(binding.toolbar,
                        isRtl(getResources()) ? binding.toolbar.getWidth() - width : width, binding.toolbar.getHeight() / 2, (float) width,
                        0.0f);
                createCircularReveal.setDuration(250);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.toolbar.setBackgroundColor(getThemeColor(Dashboard.this, R.attr.colorPrimary));
                        binding.drawerLayout.setStatusBarBackgroundColor(getThemeColor(Dashboard.this, R.attr.colorPrimaryDark));
                    }
                });
                createCircularReveal.start();
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(
                        0.0f, 0.0f, 0.0f,
                        (float) (-binding.toolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.toolbar.setBackgroundColor(getThemeColor(Dashboard.this, R.attr.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                binding.toolbar.startAnimation(animationSet);
            }
            binding.drawerLayout.setStatusBarBackgroundColor(getThemeColor(Dashboard.this, R.attr.colorPrimaryDark));
        }
    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }
}
