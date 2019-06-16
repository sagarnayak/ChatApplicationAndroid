package com.sagar.android.chatapp.ui.verify_otp_and_reset_password;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sagar.android.chatapp.repository.Repository;

public class ResetPasswordWithOtpViewModelProvider implements ViewModelProvider.Factory {
    private Repository repository;

    public ResetPasswordWithOtpViewModelProvider(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ResetPasswordWithOtpViewModel(repository);
    }
}
