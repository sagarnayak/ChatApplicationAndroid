package com.sagar.android.chatapp.di.component;

import com.sagar.android.chatapp.di.module.ForgotPasswordModule;
import com.sagar.android.chatapp.di.scope.ForgotPasswordScope;
import com.sagar.android.chatapp.ui.forgot_password.Forgotpassword;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ForgotPasswordScope
@Subcomponent(
        modules = {
                ForgotPasswordModule.class
        }
)
public interface ForgotPasswordSubComponent extends AndroidInjector<Forgotpassword> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<Forgotpassword> {
    }
}
