package com.sagar.android.chatapp.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sagar.android.chatapp.repository.Repository;

public class LoginViewModelProvider implements ViewModelProvider.Factory {
    private Repository repository;

    public LoginViewModelProvider(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new LoginViewModel(repository);
    }
}
