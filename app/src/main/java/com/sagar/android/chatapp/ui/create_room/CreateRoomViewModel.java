package com.sagar.android.chatapp.ui.create_room;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.repository.Repository;

import java.util.ArrayList;

public class CreateRoomViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<ArrayList<User>> mediatorLiveDataSearchUserResult;

    public CreateRoomViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataSearchUserResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataSearchUserResult.addSource(
                repository.mutableLiveDataUserSearchResult,
                users -> mediatorLiveDataSearchUserResult.postValue(users)
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
}
