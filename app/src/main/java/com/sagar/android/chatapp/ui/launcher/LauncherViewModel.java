package com.sagar.android.chatapp.ui.launcher;

import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.repository.Repository;

public class LauncherViewModel extends ViewModel {
    private Repository repository;

    public LauncherViewModel(Repository repository) {
        this.repository = repository;
    }

    public boolean isLoggedIn() {
        return repository.isLoggedIn();
    }
}
