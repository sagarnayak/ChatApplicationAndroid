package com.sagar.android.chatapp.ui.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

import java.lang.reflect.Field;

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
    private Menu search_menu;
    private MenuItem item_search;

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

        setSearchtollbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            binding.drawerLayout.openDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.action_filter_search) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.searchToolbar, 1, true, true);
            else
                binding.searchToolbar.setVisibility(View.VISIBLE);

            item_search.expandActionView();
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

    public void setSearchtollbar() {
        if (binding.searchToolbar != null) {
            ((Toolbar) binding.searchToolbar).inflateMenu(R.menu.search_menu);
            search_menu = ((Toolbar) binding.searchToolbar).getMenu();

            ((Toolbar) binding.searchToolbar).setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchToolbar, 1, true, false);
                    else
                        binding.searchToolbar.setVisibility(View.INVISIBLE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            item_search.setOnActionExpandListener(
                    new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem menuItem) {
                            // Do something when expanded
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                            // Do something when collapsed
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                circleReveal(R.id.searchToolbar, 1, true, false);
                            } else
                                binding.searchToolbar.setVisibility(View.INVISIBLE);
                            return true;
                        }
                    }
            );

            initSearchView();
        }
    }

    public void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_back);


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById((androidx.appcompat.R.id.search_src_text));
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 500);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }
}
