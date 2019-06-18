package com.sagar.android.chatapp.di.builder;

import android.app.Activity;

import com.sagar.android.chatapp.di.component.ProfileSubComponent;
import com.sagar.android.chatapp.ui.profile.Profile;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileActivityBuilder {

    @Binds
    @IntoMap
    @SuppressWarnings("unused")
    @ActivityKey(Profile.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ProfileSubComponent.Builder builder);
}