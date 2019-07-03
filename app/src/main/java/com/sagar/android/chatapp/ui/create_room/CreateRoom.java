package com.sagar.android.chatapp.ui.create_room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.databinding.ActivityCreateRoomBinding;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.ui.create_room.adapter.FriendsAdapter;
import com.sagar.android.chatapp.ui.create_room.adapter.SearchResultAdapter;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class CreateRoom extends AppCompatActivity {

    @Inject
    public CreateRoomViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private ActivityCreateRoomBinding binding;
    private CreateRoomViewModel viewModel;
    private SearchResultAdapter searchResultAdapter;
    private ArrayList<User> searchResultUser;
    private LinearLayoutManager linearLayoutManagerSearchResultUser;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String searchString;
    private ArrayList<User> selectedFriends;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_room);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Create Room");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(
                this,
                viewModelProvider
        )
                .get(
                        CreateRoomViewModel.class
                );

        setUpSearchToolBar();

        prepareSearchResultList();

        bindToViewModel();

        setUpSelectedFriendsList();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            backPressed();
        if (item.getItemId() == R.id.action_add_friend) {
            showFriendsSearchBar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backPressed() {
        if (item_search.isActionViewExpanded()) {
            item_search.collapseActionView();
            return;
        }
        startActivity(
                new Intent(this, Dashboard.class)
        );
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.create_room_menu, menu);
        return true;
    }

    public void onClickCreateRoom(View view) {
        createRoom();
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
                                searchUsers(charSequence);
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

    private void bindToViewModel() {
        viewModel.mediatorLiveDataSearchUserResult
                .observe(
                        this,
                        users -> {
                            if (users != null)
                                processUserListResponse(users);
                        }
                );
    }

    private void prepareSearchResultList() {
        searchResultUser = new ArrayList<>();
        linearLayoutManagerSearchResultUser = new LinearLayoutManager(this);
        searchResultAdapter = new SearchResultAdapter(
                searchResultUser,
                user -> selectedUser(user)
        );
        binding.contentCreateRoom.recyclerViewSearchResult.setLayoutManager(
                linearLayoutManagerSearchResultUser
        );
        binding.contentCreateRoom.recyclerViewSearchResult.setAdapter(
                searchResultAdapter
        );

        binding.contentCreateRoom.recyclerViewSearchResult.addOnScrollListener(
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
                        int visibleItemCount = linearLayoutManagerSearchResultUser.getChildCount();
                        int totalItemCount = linearLayoutManagerSearchResultUser.getItemCount();
                        int firstVisibleItemPosition = linearLayoutManagerSearchResultUser.findFirstVisibleItemPosition();

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && totalItemCount >= KeyWordsAndConstants.ROOM_SEARCH_LIST_PAGE_SIZE) {
                                searchUsers();
                            }
                        }
                    }
                }
        );
    }

    private void showFriendsSearchBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            circleReveal(R.id.searchToolbar, 1, true, true);
        else
            binding.searchToolBarLayout.searchToolbar.setVisibility(View.VISIBLE);

        item_search.expandActionView();
    }

    private void hideFriendsSearchBar() {
        if (item_search.isActionViewExpanded()) {
            item_search.collapseActionView();
        }
    }

    private void initialiseSearchResult() {
        searchResultUser.clear();
        searchResultAdapter.notifyDataSetChanged();
    }

    private void showSearchResultList() {
        binding.contentCreateRoom.recyclerViewSearchResult.setVisibility(View.VISIBLE);
    }

    private void hideSearchResultList() {
        binding.contentCreateRoom.recyclerViewSearchResult.setVisibility(View.GONE);
    }

    private void searchUsers(CharSequence charSequence) {
        searchString = charSequence.toString();

        isLoading = false;
        isLastPage = false;

        initialiseSearchResult();

        if (searchString.length() == 0) {
            hideSearchResultList();
        } else {
            searchUsers();
        }
    }

    private void searchUsers() {
        isLoading = true;

        viewModel.searchUser(
                searchString,
                String.valueOf(
                        KeyWordsAndConstants.USER_SEARCH_LIST_PAGE_SIZE
                ),
                String.valueOf(
                        searchResultUser.size()
                ),
                friendsAdapter.getAllIds()
        );
    }

    private void processUserListResponse(ArrayList<User> users) {
        showSearchResultList();
        isLoading = false;
        if (users.size() < KeyWordsAndConstants.USER_SEARCH_LIST_PAGE_SIZE)
            isLastPage = true;

        searchResultUser.addAll(users);
        searchResultAdapter.notifyDataSetChanged();
    }

    private void selectedUser(User user) {
        hideSearchResultList();
        UiUtil.hideSoftKeyboard(this);
        hideFriendsSearchBar();
        selectedFriends.add(user);
        friendsAdapter.notifyDataSetChanged();
    }

    private void friendUnSelected(User user) {
        selectedFriends.remove(user);
        friendsAdapter.notifyDataSetChanged();
    }

    private void setUpSelectedFriendsList() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.contentCreateRoom.recyclerViewAddFriends.setLayoutManager(layoutManager);
        selectedFriends = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(
                selectedFriends,
                new FriendsAdapter.CallBack() {
                    @Override
                    public void removeFriend(User friend) {
                        friendUnSelected(friend);
                    }

                    @Override
                    public void addMoreFriends() {
                        showFriendsSearchBar();
                    }
                }
        );
        binding.contentCreateRoom.recyclerViewAddFriends.setNestedScrollingEnabled(false);
        binding.contentCreateRoom.recyclerViewAddFriends.setAdapter(
                friendsAdapter
        );
    }

    private void createRoom() {
    }
}
