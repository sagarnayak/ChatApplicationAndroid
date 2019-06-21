package com.sagar.android.chatapp.ui.dashboard;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

public class DashboardViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataLogoutResult;
    public MediatorLiveData<Result> mediatorLiveDataShouldClearPicassoCacheForAvatar;

    public DashboardViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLogoutResult = new MediatorLiveData<>();
        mediatorLiveDataShouldClearPicassoCacheForAvatar = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataLogoutResult.addSource(
                repository.mutableLiveDataLogoutResult,
                result -> mediatorLiveDataLogoutResult.postValue(result)
        );

        mediatorLiveDataShouldClearPicassoCacheForAvatar.addSource(
                repository.mutableLiveDataShouldClearPicassoCacheForAvatar,
                result -> mediatorLiveDataShouldClearPicassoCacheForAvatar.postValue(result)
        );
    }

    public void logout() {
        repository.logout();
    }

    public UserData getUserData() {
        return repository.getUserData();
    }

    public void ping() {
        repository.ping();
    }

    public void shouldClearCacheForAvatar() {
        repository.shouldClearPicassoCacheForAvatar();
    }

    public void notAuthorised() {
        repository.notAuthorised();
    }
}
