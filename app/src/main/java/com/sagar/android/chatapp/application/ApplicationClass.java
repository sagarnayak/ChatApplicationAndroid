package com.sagar.android.chatapp.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.sagar.android.chatapp.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class ApplicationClass extends Application
        implements HasActivityInjector,
        HasServiceInjector {

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidInjectorService;

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidInjectorService;
    }

    /*@Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjectorFrag;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjectorFrag;
    }*/
}
