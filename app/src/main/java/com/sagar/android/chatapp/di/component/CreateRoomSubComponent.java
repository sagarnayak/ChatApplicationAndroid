package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.CreateRoomModule;
import com.sagar.android.chatapp.di.scope.CreateRoomScope;
import com.sagar.android.chatapp.ui.create_room.CreateRoom;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@CreateRoomScope
@Subcomponent(
        modules = {
                CreateRoomModule.class
        }
)
public interface CreateRoomSubComponent extends AndroidInjector<CreateRoom> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<CreateRoom> {
    }
}
