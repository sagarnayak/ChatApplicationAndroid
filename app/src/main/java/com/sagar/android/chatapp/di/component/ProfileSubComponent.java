package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.ProfileModule;
import com.sagar.android.chatapp.di.scope.ProfileScope;
import com.sagar.android.chatapp.ui.profile.Profile;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ProfileScope
@Subcomponent(
        modules = {
                ProfileModule.class
        }
)
public interface ProfileSubComponent extends AndroidInjector<Profile> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<Profile> {
    }
}
