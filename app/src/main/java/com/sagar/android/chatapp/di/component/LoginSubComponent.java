package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.LoginModule;
import com.sagar.android.chatapp.di.scope.LoginScope;
import com.sagar.android.chatapp.ui.login.Login;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@LoginScope
@Subcomponent(
        modules = {
                LoginModule.class
        }
)
public interface LoginSubComponent extends AndroidInjector<Login> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<Login> {
    }
}
