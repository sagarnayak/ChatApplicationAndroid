package com.sagar.android.chatapp.ui.settings;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.repository.Event;
import com.sagar.android.chatapp.repository.Repository;

public class SettingsViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Event<Result>> mediatorLiveDataLogoutAllResult;

    public SettingsViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLogoutAllResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataLogoutAllResult.addSource(
                repository.mutableLiveDataLogoutAllResult,
                result -> mediatorLiveDataLogoutAllResult.postValue(result)
        );
    }

    public void logoutAll() {
        repository.logoutAll();
    }
}
