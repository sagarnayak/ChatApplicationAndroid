package com.sagar.android.chatapp.ui.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivitySignUpBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.UserSignUpRequest;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SignUp extends AppCompatActivity {

    @Inject
    public SignUpViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private SignUpViewModel viewModel;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(SignUpViewModel.class);

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
                new Intent(this, SignUp.class)
        );
        finish();
    }

    public void onClickSignUp(View view) {
        signUp();
    }

    public void onClickAlreadyHaveAccount(View view) {
        backPressed();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataSignUpResult
                .observe(
                        this,
                        new Observer<Result>() {
                            @Override
                            public void onChanged(Result result) {
                                if (result != null)
                                    processSignUpResult(result);
                            }
                        }
                );
    }

    private void signUp() {
        UiUtil.hideSoftKeyboard(this);
        if (
                binding.contentSignUp.editTextUserName.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide name"
            );
            return;
        }
        if (
                binding.contentSignUp.editTextUserEmail.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide email"
            );
            return;
        }
        if (
                !Patterns.EMAIL_ADDRESS.matcher(
                        binding.contentSignUp.editTextUserEmail.getText()
                )
                        .matches()
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide valid email"
            );
            return;
        }
        if (
                binding.contentSignUp.editTextAge.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide age"
            );
            return;
        }
        if (
                binding.contentSignUp.editTextPassword.getText().length() == 0
        ) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide password"
            );
            return;
        }
        progressUtil.show();
        viewModel.signUp(
                new UserSignUpRequest(
                        binding.contentSignUp.editTextUserName.getText().toString().trim(),
                        binding.contentSignUp.editTextUserEmail.getText().toString().trim(),
                        binding.contentSignUp.editTextAge.getText().toString().trim(),
                        binding.contentSignUp.editTextPassword.getText().toString().trim()
                )
        );
    }

    private void processSignUpResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
            return;
        }
        startActivity(
                new Intent(this, Dashboard.class)
        );
        finish();
    }
}
