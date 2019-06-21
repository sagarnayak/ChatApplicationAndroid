package com.sagar.android.chatapp.ui.profile;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

import okhttp3.MultipartBody;

public class ProfileViewModel extends ViewModel {
    public Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataUpdateAvatarResult;
    public MediatorLiveData<Result> mediatorLiveDataShouldClearPicassoCacheForAvatar;

    public ProfileViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataUpdateAvatarResult = new MediatorLiveData<>();
        mediatorLiveDataShouldClearPicassoCacheForAvatar = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataUpdateAvatarResult.addSource(
                repository.mutableLiveDataUpdateAvatarResult,
                result -> mediatorLiveDataUpdateAvatarResult.postValue(result)
        );

        mediatorLiveDataShouldClearPicassoCacheForAvatar.addSource(
                repository.mutableLiveDataShouldClearPicassoCacheForAvatar,
                result -> mediatorLiveDataShouldClearPicassoCacheForAvatar.postValue(result)
        );
    }

    public UserData getUserData() {
        return repository.getUserData();
    }

    public void updateAvatar(MultipartBody.Part picture) {
        repository.updateAvatar(
                picture
        );
    }

    public void notAuthorised() {
        repository.notAuthorised();
    }

    public void shouldClearCacheForAvatar() {
        repository.shouldClearPicassoCacheForAvatar();
    }
}
