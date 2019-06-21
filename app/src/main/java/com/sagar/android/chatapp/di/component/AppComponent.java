package com.sagar.android.chatapp.di.component;

import android.app.Application;

import com.sagar.android.chatapp.application.ApplicationClass;
import com.sagar.android.chatapp.di.builder.DashboardActivityBuilder;
import com.sagar.android.chatapp.di.builder.FirebaseMessagingServiceBuilder;
import com.sagar.android.chatapp.di.builder.ForgotPasswordActivityBuilder;
import com.sagar.android.chatapp.di.builder.LauncherActivityBuilder;
import com.sagar.android.chatapp.di.builder.LoginActivityBuilder;
import com.sagar.android.chatapp.di.builder.ProfileActivityBuilder;
import com.sagar.android.chatapp.di.builder.ResetPasswordWithOtpActivityBuilder;
import com.sagar.android.chatapp.di.builder.SignUpActivityBuilder;
import com.sagar.android.chatapp.di.module.AppModule;
import com.sagar.android.chatapp.di.module.LogUtilModule;
import com.sagar.android.chatapp.di.module.NetworkModule;
import com.sagar.android.chatapp.di.module.RepositoryModule;
import com.sagar.android.chatapp.di.module.SharedPreferenceModule;
import com.sagar.android.chatapp.di.scope.ApplicationScope;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@ApplicationScope
@Component(
        modules = {
                AndroidInjectionModule.class,
                AppModule.class,
                LogUtilModule.class,
                NetworkModule.class,
                RepositoryModule.class,
                SharedPreferenceModule.class,
                LauncherActivityBuilder.class,
                SignUpActivityBuilder.class,
                LoginActivityBuilder.class,
                ForgotPasswordActivityBuilder.class,
                ResetPasswordWithOtpActivityBuilder.class,
                DashboardActivityBuilder.class,
                ProfileActivityBuilder.class,
                FirebaseMessagingServiceBuilder.class
        }
)
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ApplicationClass applicationClass);
}
