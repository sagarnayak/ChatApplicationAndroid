package com.sagar.android.chatapp.ui.login;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.LoginRequest;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.repository.Repository;

public class LoginViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataLoginResult;

    public LoginViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLoginResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataLoginResult.addSource(
                repository.mutableLiveDataLoginResult,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        mediatorLiveDataLoginResult.postValue(result);
                    }
                }
        );
    }

    public void login(LoginRequest loginRequest) {
        repository.login(loginRequest);
    }
}
