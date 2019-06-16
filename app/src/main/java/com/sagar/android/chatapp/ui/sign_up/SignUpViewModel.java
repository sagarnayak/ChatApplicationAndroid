package com.sagar.android.chatapp.ui.sign_up;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserSignUpRequest;
import com.sagar.android.chatapp.repository.Repository;

public class SignUpViewModel extends ViewModel {

    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataSignUpResult;

    public SignUpViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataSignUpResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataSignUpResult.addSource(
                repository.mutableLiveDataSignUpResult,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        mediatorLiveDataSignUpResult.postValue(result);
                    }
                }
        );
    }

    public void signUp(UserSignUpRequest userSignUpRequest) {
        repository.signUp(userSignUpRequest);
    }
}
