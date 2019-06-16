package com.sagar.android.chatapp.ui.forgot_password;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.repository.Repository;

public class ForgotPasswordViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataForgotPasswordResult;

    public ForgotPasswordViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataForgotPasswordResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataForgotPasswordResult.addSource(
                repository.mutableLiveDataForgotPasswordResult,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        mediatorLiveDataForgotPasswordResult.postValue(result);
                    }
                }
        );
    }

    public void sendOtpToEmail(String email) {
        repository.forgotPassword(email);
    }
}
