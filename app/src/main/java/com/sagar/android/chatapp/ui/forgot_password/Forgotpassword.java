package com.sagar.android.chatapp.ui.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivityForgotpasswordBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.ui.login.Login;
import com.sagar.android.chatapp.ui.verify_otp_and_reset_password.ResetPasswordWithOtp;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Forgotpassword extends AppCompatActivity {

    @Inject
    public ForgotPasswordViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private ForgotPasswordViewModel viewModel;
    private ActivityForgotpasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgotpassword);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Forgot Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(ForgotPasswordViewModel.class);

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

    public void onClickSendOtp(View view) {
        sendOtp();
    }

    public void onClickAlreadyHaveOtp(View view) {
        startActivity(
                new Intent(this, ResetPasswordWithOtp.class)
        );
        finish();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataForgotPasswordResult
                .observe(
                        this,
                        result -> {
                            if (result.shouldReadContent())
                                processForgotPasswordResult(result.getContent());
                        }
                );
    }

    private void sendOtp() {
        UiUtil.hideSoftKeyboard(this);
        if (
                binding.contentForgotpassword.editTextEmailId.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please enter email"
            );
            return;
        }
        if (
                !Patterns.EMAIL_ADDRESS.matcher(
                        binding.contentForgotpassword.editTextEmailId.getText().toString().trim()
                )
                        .matches()
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please enter valid email"
            );
            return;
        }
        progressUtil.show();
        viewModel.sendOtpToEmail(
                binding.contentForgotpassword.editTextEmailId.getText().toString().trim()
        );
    }

    private void processForgotPasswordResult(Result result) {
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
                    }

                    @Override
                    public void buttonClicked() {
                        startActivity(
                                new Intent(
                                        Forgotpassword.this,
                                        ResetPasswordWithOtp.class
                                )
                        );
                        finish();
                    }
                }
        );
    }
}
