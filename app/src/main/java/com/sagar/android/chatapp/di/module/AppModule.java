package com.sagar.android.chatapp.di.module;

import com.sagar.android.chatapp.di.component.ForgotPasswordSubComponent;
import com.sagar.android.chatapp.di.component.LauncherSubComponent;
import com.sagar.android.chatapp.di.component.LoginSubComponent;
import com.sagar.android.chatapp.di.component.ResetPasswordWithOtpSubComponent;
import com.sagar.android.chatapp.di.component.SignUpSubComponent;

import dagger.Module;

@Module(
        subcomponents = {
                LauncherSubComponent.class,
                SignUpSubComponent.class,
                LoginSubComponent.class,
                ForgotPasswordSubComponent.class,
                ResetPasswordWithOtpSubComponent.class
        }
)
public class AppModule {
}
