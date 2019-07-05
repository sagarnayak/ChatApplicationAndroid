package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.ChatRoomModule;
import com.sagar.android.chatapp.di.scope.ChatRoomScope;
import com.sagar.android.chatapp.ui.room.ChatRoom;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ChatRoomScope
@Subcomponent(
        modules = {
                ChatRoomModule.class
        }
)
public interface ChatRoomSubComponent extends AndroidInjector<ChatRoom> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ChatRoom> {
    }
}
