package com.sagar.android.chatapp.ui.verify_otp_and_reset_password;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.ResetPasswordRequest;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.repository.Repository;

public class ResetPasswordWithOtpViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataResetPasswordResult;

    public ResetPasswordWithOtpViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataResetPasswordResult = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataResetPasswordResult.addSource(
                repository.mutableLiveDataResetPasswordResult,
                result -> mediatorLiveDataResetPasswordResult.postValue(result)
        );
    }

    public void resetPassword(String otp, String password) {
        repository.resetPassword(
                new ResetPasswordRequest(
                        otp, password
                )
        );
    }
}
