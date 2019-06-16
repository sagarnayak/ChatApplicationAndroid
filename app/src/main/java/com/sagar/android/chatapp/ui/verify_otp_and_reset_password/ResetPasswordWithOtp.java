package com.sagar.android.chatapp.ui.verify_otp_and_reset_password;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivityResetPasswordAndOtpBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ResetPasswordWithOtp extends AppCompatActivity {

    @Inject
    public ResetPasswordWithOtpViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private ResetPasswordWithOtpViewModel viewModel;
    private ActivityResetPasswordAndOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_and_otp);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password_and_otp);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Forgot Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(ResetPasswordWithOtpViewModel.class);

        bindToViewModel();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            backPressed();
        return super.onOptionsItemSelected(item);
    }

    private void backPressed() {
        startActivity(
                new Intent(this, Login.class)
        );
        finish();
    }

    public void onClickSubmit(View view) {
        submit();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataResetPasswordResult
                .observe(
                        this,
                        new Observer<Result>() {
                            @Override
                            public void onChanged(Result result) {
                                if (result != null)
                                    processResetPasswordResult(result);
                            }
                        }
                );
    }

    private void submit() {
        UiUtil.hideSoftKeyboard(this);
        if (
                binding.contentResetPasswordAndOtp.editTextOtp.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please enter otp"
            );
            return;
        }
        if (
                binding.contentResetPasswordAndOtp.editTextPassword.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please enter new password"
            );
            return;
        }
        progressUtil.show();
        viewModel.resetPassword(
                binding.contentResetPasswordAndOtp.editTextOtp.getText().toString(),
                binding.contentResetPasswordAndOtp.editTextPassword.getText().toString()
        );
    }

    private void processResetPasswordResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
            return;
        }
        DialogUtil.showDialogWithMessage(
                this,
                result.getMessage(),
                false,
                new DialogUtil.DialogWithMessageCallBack() {
                    @Override
                    public void dialogCancelled() {
                        startActivity(
                                new Intent(ResetPasswordWithOtp.this, Login.class)
                        );
                        finish();
                    }
                }
        );
    }
}
