package com.sagar.android.chatapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sagar.android.chatapp.BuildConfig;
import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivityLoginBinding;
import com.sagar.android.chatapp.model.LoginRequest;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.ui.dashboard.Dashboard;
import com.sagar.android.chatapp.ui.forgot_password.Forgotpassword;
import com.sagar.android.chatapp.ui.sign_up.SignUp;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;
import com.sagar.android.chatapp.util.UiUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Login extends AppCompatActivity {

    @Inject
    public LoginViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        UiUtil.hideSoftKeyboardAtStart(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setContext(this);

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(LoginViewModel.class);

        bindToViewModel();

        if (BuildConfig.DEBUG) {
            binding.contentLogin.editTextUserName.setText("snkumar.nayak@gmail.com");
            binding.contentLogin.editTextPassword.setText("qwerty");
        }
    }

    public void onClickLogin(View view) {
        login();
    }

    public void onClickForgotPassword(View view) {
        forgotPassword();
    }

    public void onClickSignUp(View view) {
        signUp();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataLoginResult
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processLoginResult(result);
                        }
                );
    }

    public void login() {
        UiUtil.hideSoftKeyboard(this);
        if (binding.contentLogin.editTextUserName.getText().length() == 0) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide email id"
            );
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(
                binding.contentLogin.editTextUserName.getText().toString()
        ).matches()) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide valid email id"
            );
            return;
        }
        if (binding.contentLogin.editTextPassword.getText().length() == 0) {
            DialogUtil.showDialogWithMessage(
                    this,
                    "Please provide password"
            );
            return;
        }
        progressUtil.show();
        viewModel.login(
                new LoginRequest(
                        binding.contentLogin.editTextPassword.getText().toString(),
                        binding.contentLogin.editTextUserName.getText().toString()
                )
        );
    }

    private void processLoginResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
            return;
        }
        getFcmTokenAndTryUpdating();
    }

    private void getFcmTokenAndTryUpdating() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                //noinspection ConstantConditions
                                viewModel.tryUpdatingFcmToken(
                                        task.getResult().getToken()
                                );
                            }

                            gotoDashboard();
                        }
                );
    }

    private void gotoDashboard() {
        startActivity(
                new Intent(
                        this,
                        Dashboard.class
                )
        );
        finish();
    }

    private void forgotPassword() {
        startActivity(
                new Intent(this, Forgotpassword.class)
        );
        finish();
    }

    private void signUp() {
        startActivity(
                new Intent(this, SignUp.class)
        );
        finish();
    }
}
