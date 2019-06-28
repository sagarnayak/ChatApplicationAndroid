package com.sagar.android.chatapp.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.ui.room.ChatRoom;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Launcher extends AppCompatActivity {

    @Inject
    public LauncherViewModelProvider viewModelProvider;

    private LauncherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(LauncherViewModel.class);

        checkAndProceedAccordingly();
    }

    private void checkAndProceedAccordingly() {
        /*if (!viewModel.isLoggedIn())
            startActivity(new Intent(this, Login.class));
        else
            startActivity(new Intent(this, Dashboard.class));*/
        startActivity(new Intent(this, ChatRoom.class));
        finish();
    }
}
