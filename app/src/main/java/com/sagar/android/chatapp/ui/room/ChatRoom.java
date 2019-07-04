package com.sagar.android.chatapp.ui.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.databinding.ActivityChatRoomBinding;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.room.adapter.ChatRoomAdapter;
import com.sagar.android.chatapp.util.UiUtil;

public class ChatRoom extends AppCompatActivity {

    public static final String DATA = "DATA";

    private Room room;
    private ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        getDataFromIntent();

        setUpUI();

        binding.contentChatRoom.recyclerViewChats.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.contentChatRoom.recyclerViewChats.setAdapter(
                new ChatRoomAdapter()
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

    private void getDataFromIntent() {
        room = new Gson().fromJson(
                getIntent().getStringExtra(DATA),
                Room.class
        );
    }

    private void setUpUI() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(room.getName());
    }
}
