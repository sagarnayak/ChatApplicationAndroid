package com.sagar.android.chatapp.ui.profile;

import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

public class ProfileViewModel extends ViewModel {
    private Repository repository;

    public ProfileViewModel(Repository repository) {
        this.repository = repository;
    }

    public UserData getUserData() {
        return repository.getUserData();
    }
}
