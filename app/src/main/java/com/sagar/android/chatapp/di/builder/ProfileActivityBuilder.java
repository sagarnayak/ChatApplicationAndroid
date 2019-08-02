package com.sagar.android.chatapp.di.builder;

import com.sagar.android.chatapp.di.component.ProfileSubComponent;
import com.sagar.android.chatapp.ui.profile.Profile;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ClassKey(Profile.class)
    abstract AndroidInjector.Factory<?> bind(ProfileSubComponent.Factory builder);
}