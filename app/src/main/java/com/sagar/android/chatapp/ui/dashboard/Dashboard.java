package com.sagar.android.chatapp.ui.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.appcompat.RxSearchView;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.ActivityDashboardBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.ui.dashboard.adapter.RoomListAdapter;
import com.sagar.android.chatapp.ui.dashboard.adapter.RoomSearchListAdapter;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.profile.Profile;
import com.sagar.android.chatapp.ui.settings.Settings;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.ColorUtil;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.logutilmaster.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class Dashboard extends AppCompatActivity {

    @Inject
    public DashboardViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;
    @Inject
    public Picasso picassoAuthenticated;
    @Inject
    public LogUtil logUtil;

    private DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.shouldClearCacheForAvatar();
        }
    };

    private LinearLayoutManager linearLayoutManager;
    private RoomListAdapter roomListAdapter;
    private ArrayList<Room> allRoomsList;

    private LinearLayoutManager linearLayoutManagerRoomSearchList;
    private RoomSearchListAdapter roomSearchListAdapter;
    private ArrayList<Room> roomSearchList;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String searchString;

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

        setUpSearchToolBar();

        setUpRoomList();

        getRoomList();

        prepareSearchResultList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            binding.drawerLayout.openDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.action_search) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.searchToolbar, 1, true, true);
            else
                binding.searchToolBarLayout.searchToolbar.setVisibility(View.VISIBLE);

            item_search.expandActionView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (item_search.isActionViewExpanded()) {
            item_search.collapseActionView();
            return;
        }
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
            picassoAuthenticated.invalidate(URLs.MY_AVATAR_URL);
        picassoAuthenticated
                .load(
                        URLs.MY_AVATAR_URL
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

        viewModel.mediatorLiveDataAllRooms
                .observe(
                        this,
                        rooms -> {
                            if (rooms != null)
                                processAllRooms(rooms);
                        }
                );

        viewModel.mediatorLiveDataAllRoomsError
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processAllRoomError(result);
                        }
                );

        viewModel.mediatorLiveDataRoomSearchResult
                .observe(
                        this,
                        rooms -> {
                            if (rooms != null)
                                processRoomSearchResult(rooms);
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
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Code for the search view
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Menu search_menu;
    private MenuItem item_search;

    public void setUpSearchToolBar() {
        /*
        search view reference taken form
        https://github.com/jaisonfdo/SearchViewSample
         */
        if (binding.searchToolBarLayout.searchToolbar != null) {
            binding.searchToolBarLayout.searchToolbar.inflateMenu(R.menu.menu_search);
            search_menu = binding.searchToolBarLayout.searchToolbar.getMenu();

            binding.searchToolBarLayout.searchToolbar.setNavigationOnClickListener(
                    v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            circleReveal(R.id.searchToolbar, 1, true, false);
                        else
                            binding.searchToolBarLayout.searchToolbar.setVisibility(View.INVISIBLE);
                    });

            item_search = search_menu.findItem(R.id.action_filter_search);

            item_search.setOnActionExpandListener(
                    new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem menuItem) {
                            initialiseSearchResult();
                            // Do something when expanded
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                            hideSearchResultList();
                            // Do something when collapsed
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                circleReveal(R.id.searchToolbar, 1, true, false);
                            } else
                                binding.searchToolBarLayout.searchToolbar.setVisibility(View.INVISIBLE);
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

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);


        // set hint and the text colors

        @SuppressLint("CutPasteId") EditText txtSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTypeface(
                ResourcesCompat.getFont(
                        this,
                        R.font.open_sans
                )
        );
        txtSearch.setTextSize(
                TypedValue.COMPLEX_UNIT_SP, 14
        );
        txtSearch.setTextColor(getResources().getColor(R.color.primary_text));

        // set the cursor

        @SuppressLint("CutPasteId") AutoCompleteTextView searchTextView =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                logUtil.logV("text submit : " + query);
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                logUtil.logV("text change : " + newText);
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });*/

        /*
        this is the rx java implementation for the above function.
         */
        RxSearchView.queryTextChanges(
                searchView
        )
                .map(
                        (Function<CharSequence, CharSequence>) charSequence ->
                                charSequence.toString().trim()
                )
                /*.filter(
                        charSequence -> charSequence.length() != 0
                )*/
                .debounce(
                        1000, TimeUnit.MILLISECONDS
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<CharSequence>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(CharSequence charSequence) {
                                searchRooms(charSequence);
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

    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = binding.searchToolBarLayout.searchToolbar;

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -=
                    (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))
                            - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void getRoomList() {
        viewModel.getAllRooms();
    }

    private void setUpRoomList() {
        linearLayoutManager = new LinearLayoutManager(this);
        allRoomsList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(
                allRoomsList,
                this
        );
        binding.contentDashboard.recyclerViewRoomList.setLayoutManager(
                linearLayoutManager
        );
        binding.contentDashboard.recyclerViewRoomList.setAdapter(
                roomListAdapter
        );
    }

    private void processAllRooms(ArrayList<Room> rooms) {
        allRoomsList.addAll(rooms);
        roomListAdapter.notifyDataSetChanged();
    }

    private void processAllRoomError(Result result) {
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
        }
    }

    private void prepareSearchResultList() {
        roomSearchList = new ArrayList<>();
        linearLayoutManagerRoomSearchList = new LinearLayoutManager(this);
        roomSearchListAdapter = new RoomSearchListAdapter(
                roomSearchList,
                this
        );
        binding.contentDashboard.recyclerViewSearchResult.setLayoutManager(
                linearLayoutManagerRoomSearchList
        );
        binding.contentDashboard.recyclerViewSearchResult.setAdapter(
                roomSearchListAdapter
        );

        binding.contentDashboard.recyclerViewSearchResult.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(
                            @NonNull RecyclerView recyclerView,
                            int newState
                    ) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int visibleItemCount = linearLayoutManagerRoomSearchList.getChildCount();
                        int totalItemCount = linearLayoutManagerRoomSearchList.getItemCount();
                        int firstVisibleItemPosition = linearLayoutManagerRoomSearchList.findFirstVisibleItemPosition();

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && totalItemCount >= KeyWordsAndConstants.ROOM_SEARCH_LIST_PAGE_SIZE) {
                                searchRooms();
                            }
                        }
                    }
                }
        );
    }

    private void initialiseSearchResult() {
        roomSearchList.clear();
        roomSearchListAdapter.notifyDataSetChanged();
    }

    private void showSearchResultList() {
        binding.contentDashboard.recyclerViewSearchResult.setVisibility(View.VISIBLE);
    }

    private void hideSearchResultList() {
        binding.contentDashboard.recyclerViewSearchResult.setVisibility(View.GONE);
    }

    private void searchRooms(CharSequence charSequence) {
        searchString = charSequence.toString();

        isLoading = false;
        isLastPage = false;

        initialiseSearchResult();

        if (searchString.length() == 0) {
            hideSearchResultList();
        } else {
            searchRooms();
        }
    }

    private void searchRooms() {
        isLoading = true;

        viewModel.searchRooms(
                searchString,
                String.valueOf(
                        KeyWordsAndConstants.ROOM_SEARCH_LIST_PAGE_SIZE
                ),
                String.valueOf(
                        roomSearchList.size()
                )
        );
    }

    private void processRoomSearchResult(ArrayList<Room> rooms) {
        showSearchResultList();
        isLoading = false;
        if (rooms.size() < KeyWordsAndConstants.ROOM_SEARCH_LIST_PAGE_SIZE)
            isLastPage = true;

        roomSearchList.addAll(rooms);
        roomSearchListAdapter.notifyDataSetChanged();
    }
}
