package com.sagar.android.chatapp.ui.room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivityChatRoomBinding;
import com.sagar.android.chatapp.model.Chat;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.room.adapter.ChatRoomAdapter;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;
import com.sagar.android.logutilmaster.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    private Room room;
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel viewModel;
    private MenuItem menuItemLeave;
    private ArrayList<Chat> chats;

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

        setUpUI();

        bindToViewModel();

        binding.contentChatRoom.recyclerViewChats.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.contentChatRoom.recyclerViewChats.setAdapter(
                new ChatRoomAdapter()
        );

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        viewModel.getChatData(
                                "12",
                                "0",
                                "534grgeg"
                        );
                    }
                },
                5000
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.initialiseSockets();
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
                            if (result!=null)
                                processConnectedToSocketResponse();
                        }
                );
    }

    private void getDataFromIntent() {
        room = new Gson().fromJson(
                getIntent().getStringExtra(DATA),
                Room.class
        );
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
        UserData userData = viewModel.getUserData();
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

    private void processConnectedToSocketResponse(){
        viewModel.joinRoomSocket(room.getId());
    }

    private void sendMessage(String message) {
        viewModel.sendMessage(
                message,
                room.getId()
        );
    }
}
