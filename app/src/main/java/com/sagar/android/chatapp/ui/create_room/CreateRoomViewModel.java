package com.sagar.android.chatapp.ui.create_room;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.repository.Event;
import com.sagar.android.chatapp.repository.Repository;

import java.util.ArrayList;

public class CreateRoomViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Event<ArrayList<User>>> mediatorLiveDataSearchUserResult;
    public MediatorLiveData<Event<Result>> mediatorLiveDataCreateRoomResult;

    public CreateRoomViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataSearchUserResult = new MediatorLiveData<>();
        mediatorLiveDataCreateRoomResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataSearchUserResult.addSource(
                repository.mutableLiveDataUserSearchResult,
                users -> mediatorLiveDataSearchUserResult.postValue(users)
        );

        mediatorLiveDataCreateRoomResult.addSource(
                repository.mutableLiveDataCreateRoomResult,
                result -> mediatorLiveDataCreateRoomResult.postValue(result)
        );
    }

    public void searchUser(
            String containing,
            String limit,
            String skip,
            ArrayList<String> alreadyUsed
    ) {
        repository.searchUser(
                containing, limit, skip, alreadyUsed
        );
    }

    public void createRoom(
            String name,
            ArrayList<String> members
    ) {
        repository.createRoom(
                name,
                members
        );
    }
}
