package com.sagar.android.chatapp.ui.launcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sagar.android.chatapp.repository.Repository;

public class LauncherViewModelProvider implements ViewModelProvider.Factory {
    private Repository repository;

    public LauncherViewModelProvider(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new LauncherViewModel(repository);
    }
}
