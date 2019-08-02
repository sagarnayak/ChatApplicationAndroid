package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.ChatRoomScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.room.ChatRoom;
import com.sagar.android.chatapp.ui.room.ChatRoomViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatRoomModule {
    @Provides
    @ChatRoomScope
    ChatRoomViewModelProvider viewModelProvider(Repository repository) {
        return new ChatRoomViewModelProvider(repository);
    }

    @Provides
    @ChatRoomScope
    ProgressUtil progressUtil(ChatRoom context) {
        return new ProgressUtil(context);
    }
}