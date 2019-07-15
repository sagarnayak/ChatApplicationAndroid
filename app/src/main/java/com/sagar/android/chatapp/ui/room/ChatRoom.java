package com.sagar.android.chatapp.ui.room;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.core.KeyWordsAndConstants;
import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.ActivityChatRoomBinding;
import com.sagar.android.chatapp.model.Chat;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.notification.NotificationMaster;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.room.adapter.ChatRoomAdapter;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;
import com.sagar.android.logutilmaster.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ChatRoom extends AppCompatActivity {

    public static final String DATA = "DATA";

    @Inject
    public ChatRoomViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;
    @Inject
    public Picasso picasso;
    @Inject
    public LogUtil logUtil;
    @Inject
    public NotificationMaster notificationMaster;

    private Room room;
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel viewModel;
    private MenuItem menuItemLeave;

    private BroadcastReceiver receiverAvatarUpdatedForUser = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            clearCacheForUserPicture(
                    intent.getStringExtra(
                            "userId"
                    )
            );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(ChatRoomViewModel.class);

        getDataFromIntent();

        prepareChatList();

        bindToViewModel();

        IntentFilter intentFilterAvatarUpdatedForUser = new IntentFilter(
                KeyWordsAndConstants.AVATAR_UPDATED_FOR_USER_BROADCAST_ACTION
        );
        registerReceiver(
                receiverAvatarUpdatedForUser,
                intentFilterAvatarUpdatedForUser
        );
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.chat_room_menu, menu);
        menuItemLeave = menu.findItem(R.id.action_leave);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        viewModel.disconnectSocket();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tryInitializingSocketAfterDelay();
    }

    private void tryInitializingSocketAfterDelay() {
        new Handler().postDelayed(
                () -> {
                    if (room != null)
                        viewModel.initialiseSockets();
                    else
                        tryInitializingSocketAfterDelay();
                },
                500
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
        if (item.getItemId() == R.id.action_leave) {
            leaveRoom();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backPressed() {
        viewModel.disconnectSocket();
        startActivity(
                new Intent(this, Dashboard.class)
        );
        finish();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataLeaveRoomResult
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processLeaveRoomResult(result);
                        }
                );

        viewModel.mediatorLiveDataJoinRoomResult
                .observe(
                        this,
                        room -> {
                            if (room != null)
                                processJoinRoomResult(room);
                        }
                );

        viewModel.mediatorLiveDataJoinRoomError
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processJoinRoomError(result);
                        }
                );

        viewModel.mediatorLiveDataConnectedToSocket
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processConnectedToSocketResponse();
                        }
                );

        viewModel.mediatorLiveDataChats
                .observe(
                        this,
                        chats -> {
                            if (chats != null)
                                gotNewChats(chats);
                        }
                );

        viewModel.mediatorLiveDataRoom
                .observe(
                        this,
                        room -> {
                            if (room != null)
                                processRoomData(room);
                        }
                );
    }

    private void getDataFromIntent() {
        userData = viewModel.getUserData();

        try {
            room = new Gson().fromJson(
                    getIntent().getStringExtra(DATA),
                    Room.class
            );

            setUpUI();

            notificationMaster.readAllChatsInRoom(room.getId(), true);
        } catch (Exception e) {
            e.printStackTrace();

            String data = getIntent().getStringExtra("roomId");
            getRoomData(
                    data
            );
        }
    }

    private void setUpUI() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(room.getName());

        if (
                !isUserInRoom()
        ) {
            binding.contentChatRoom.editTextMessage.setVisibility(View.GONE);
            binding.contentChatRoom.appcompatImageViewSend.setVisibility(View.GONE);
        } else {
            binding.contentChatRoom.buttonJoinRoom.setVisibility(View.GONE);
        }

        setUpMenuListing();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isUserInRoom() {
        boolean inRoom = false;
        for (User user :
                room.getUsers()) {
            if (user.getId().equalsIgnoreCase(userData.getUser().getId())) {
                inRoom = true;
                break;
            }
        }
        return inRoom;
    }

    private void setUpMenuListing() {
        try {
            if (
                    !isUserInRoom()
            ) {
                menuItemLeave.setVisible(false);
            } else {
                menuItemLeave.setVisible(true);
            }
        } catch (Exception e) {
            new Handler().postDelayed(
                    this::setUpMenuListing,
                    500
            );
        }
    }

    public void onClickJoinRoom(View view) {
        progressUtil.show();
        viewModel.joinRoom(room.getId());
    }

    public void onClickSendMessage(View view) {
        if (binding.contentChatRoom.editTextMessage.getText().length() == 0)
            return;
        sendMessage(binding.contentChatRoom.editTextMessage.getText().toString().trim());
        binding.contentChatRoom.editTextMessage.setText("");
    }

    private void leaveRoom() {
        progressUtil.show();
        viewModel.leaveRoom(room.getId());
    }

    private void processLeaveRoomResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
            return;
        }

        backPressed();
    }

    private void processJoinRoomResult(Room room) {
        progressUtil.hide();

        startActivity(
                new Intent(
                        this,
                        ChatRoom.class
                )
                        .putExtra(
                                ChatRoom.DATA,
                                new Gson().toJson(room)
                        )
        );
//        viewModel.disconnectSocket();
        finish();
    }

    private void processJoinRoomError(Result result) {
        progressUtil.hide();

        DialogUtil.showDialogWithMessage(
                this,
                result.getMessage()
        );
    }

    private void processConnectedToSocketResponse() {
        viewModel.joinRoomSocket(room.getId());

        if (chats.size() != 0)
            return;
        getChats();
    }

    private void sendMessage(String message) {
        viewModel.sendMessage(
                message,
                room.getId()
        );
    }

    private LinearLayoutManager linearLayoutManager;
    private ChatRoomAdapter adapter;
    private ArrayList<Chat> chats;
    private boolean canScrollToBottom = true;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isThisMyMesssage = false;
    private UserData userData;

    private void prepareChatList() {
        userData = viewModel.getUserData();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setItemPrefetchEnabled(true);

        chats = new ArrayList<>();

        adapter = new ChatRoomAdapter(
                chats,
                picasso,
                userData
        );

        binding.contentChatRoom.recyclerViewChats.setLayoutManager(
                linearLayoutManager
        );
        binding.contentChatRoom.recyclerViewChats.setAdapter(
                adapter
        );
        binding.contentChatRoom.recyclerViewChats.setItemAnimator(
                new DefaultItemAnimator()
        );

        binding.contentChatRoom.recyclerViewChats.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (
                                !recyclerView.canScrollVertically(1) &&
                                        newState == RecyclerView.SCROLL_STATE_IDLE
                        ) {
                            canScrollToBottom = true;
                        } else {
                            canScrollToBottom = false;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && totalItemCount >= KeyWordsAndConstants.CHAT_LIST_PAGE_SIZE) {
                                getChats();
                            }
                        }
                    }
                }
        );
    }

    private void getChats() {
        isLoading = true;

        viewModel.getChatData(
                String.valueOf(
                        KeyWordsAndConstants.CHAT_LIST_PAGE_SIZE
                ),
                String.valueOf(
                        chats.size()
                ),
                room.getId()
        );
    }

    private void gotNewChats(ArrayList<Chat> chats) {
        isLoading = false;

        if (chats.size() < KeyWordsAndConstants.CHAT_LIST_PAGE_SIZE)
            isLastPage = true;

        if (
                chats.size() == 1 &&
                        chats.get(0).getAuthorDetail().getId().equalsIgnoreCase(
                                userData.getUser().getId()
                        )
        )
            isThisMyMesssage = true;

        for (Chat c :
                chats) {
            if (this.chats.size() == 0)
                this.chats.add(c);
            else {
                int indexToInsert = -1;
                for (Chat chat :
                        this.chats) {
                    if (
                            c.getCalendarCreated().getTimeInMillis() >
                                    chat.getCalendarCreated().getTimeInMillis()
                    ) {
                        indexToInsert = this.chats.indexOf(chat);
                        break;
                    }
                }
                if (indexToInsert != -1)
                    this.chats.add(indexToInsert, c);
                else
                    this.chats.add(c);
            }
        }

        adapter.notifyDataSetChanged();

        if (
                isThisMyMesssage
        ) {
            isThisMyMesssage = false;
            new Handler().postDelayed(
                    () -> binding.contentChatRoom.recyclerViewChats.smoothScrollToPosition(
                            0
                    ),
                    500
            );
        } else {
            if (!canScrollToBottom)
                return;
            if (this.chats.size() == 0)
                return;
            new Handler().postDelayed(
                    () -> binding.contentChatRoom.recyclerViewChats.smoothScrollToPosition(
                            0
                    ),
                    500
            );
        }
    }

    private void getRoomData(String roomId) {
        viewModel.getRoom(roomId);
    }

    private void processRoomData(Room room) {
        this.room = room;

        setUpUI();

        notificationMaster.readAllChatsInRoom(room.getId(), true);
    }

    private void clearCacheForUserPicture(String userId) {
        picasso.invalidate(
                URLs.PROFILE_PICTURE_URL + userId
        );
        for (Chat chat :
                chats) {
            if (chat.getAuthorDetail().getId().equalsIgnoreCase(userId)) {
                chat.getAuthorDetail().setAvatarLastUpdated(
                        Calendar.getInstance()
                );
            }
        }
        adapter.notifyDataSetChanged();
    }
}
