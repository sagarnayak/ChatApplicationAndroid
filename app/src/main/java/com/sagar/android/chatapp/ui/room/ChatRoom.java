package com.sagar.android.chatapp.ui.room;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.databinding.ActivityChatRoomBinding;
import com.sagar.android.chatapp.ui.room.adapter.ChatRoomAdapter;
import com.sagar.android.chatapp.util.UiUtil;

public class ChatRoom extends AppCompatActivity {

    private ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        binding.contentChatRoom.recyclerViewChats.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.contentChatRoom.recyclerViewChats.setAdapter(
                new ChatRoomAdapter()
        );
    }
}
