package com.sagar.android.chatapp.ui.dashboard;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataLogoutResult;
    public MediatorLiveData<Result> mediatorLiveDataShouldClearPicassoCacheForAvatar;
    public MediatorLiveData<ArrayList<Room>> mediatorLiveDataAllRooms;
    public MediatorLiveData<Result> mediatorLiveDataAllRoomsError;
    public MediatorLiveData<ArrayList<Room>> mediatorLiveDataRoomSearchResult;

    public DashboardViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLogoutResult = new MediatorLiveData<>();
        mediatorLiveDataShouldClearPicassoCacheForAvatar = new MediatorLiveData<>();
        mediatorLiveDataAllRooms = new MediatorLiveData<>();
        mediatorLiveDataAllRoomsError = new MediatorLiveData<>();
        mediatorLiveDataRoomSearchResult = new MediatorLiveData<>();

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

        mediatorLiveDataAllRooms.addSource(
                repository.mutableLiveDataAllRooms,
                rooms -> mediatorLiveDataAllRooms.postValue(rooms)
        );

        mediatorLiveDataAllRoomsError.addSource(
                repository.mutableLiveDataAllRoomsError,
                result -> mediatorLiveDataAllRoomsError.postValue(result)
        );

        mediatorLiveDataRoomSearchResult.addSource(
                repository.mutableLiveDataRoomSearchResult,
                rooms -> mediatorLiveDataRoomSearchResult.postValue(rooms)
        );
    }

    public void logout() {
        repository.logout();
    }

    public UserData getUserData() {
        return repository.getUserData();
    }

    public void shouldClearCacheForAvatar() {
        repository.shouldClearPicassoCacheForAvatar();
    }

    public void notAuthorised() {
        repository.notAuthorised();
    }

    public void getAllRooms() {
        repository.getAllRooms();
    }

    public void searchRooms(
            String searchString,
            String limit,
            String skip
    ) {
        repository.searchRooms(searchString, limit, skip);
    }
}
