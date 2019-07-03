package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.scope.CreateRoomScope;
import com.sagar.android.chatapp.repository.Repository;
import com.sagar.android.chatapp.ui.create_room.CreateRoom;
import com.sagar.android.chatapp.ui.create_room.CreateRoomViewModelProvider;
import com.sagar.android.chatapp.util.ProgressUtil;

import dagger.Module;
import dagger.Provides;

@Module
@CreateRoomScope
public class CreateRoomModule {
    @Provides
    CreateRoomViewModelProvider viewModelProvider(Repository repository) {
        return new CreateRoomViewModelProvider(repository);
    }

    @Provides
    ProgressUtil progressUtil(CreateRoom context) {
        return new ProgressUtil(context);
    }
}