package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.SignUpModule;
import com.sagar.android.chatapp.di.scope.SignUpScope;
import com.sagar.android.chatapp.ui.sign_up.SignUp;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@SignUpScope
@Subcomponent(
        modules = {
                SignUpModule.class
        }
)
public interface SignUpSubComponent extends AndroidInjector<SignUp> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<SignUp> {
    }
}
