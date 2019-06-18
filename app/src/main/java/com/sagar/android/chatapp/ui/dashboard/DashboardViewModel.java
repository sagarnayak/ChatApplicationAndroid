package com.sagar.android.chatapp.ui.dashboard;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

public class DashboardViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataLogoutResult;

    public DashboardViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLogoutResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataLogoutResult.addSource(
                repository.mutableLiveDataLogoutResult,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        mediatorLiveDataLogoutResult.postValue(result);
                    }
                }
        );
    }

    public void logout() {
        repository.logout();
    }

    public UserData getUserData(){
        return repository.getUserData();
    }
}
