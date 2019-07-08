package com.sagar.android.chatapp.ui.login;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
            binding.contentLogin.editTextUserName.setText("one@gmail.com");
            binding.contentLogin.editTextPassword.setText("qwerty");
        }

        Intent intent = new Intent(this, SignUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chat_app_channel";
            String description = "chat_app_channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chat_app_chan_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(
                    this,
                    "chat_app_chan_id"
            ).setSmallIcon(R.drawable.bubble)
                    .setContentTitle("title")
                    .setContentText("content")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true).build();

            notificationManager.notify(123, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.drawable.bubble)
                    .setContentTitle("title")
                    .setContentText("content")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(123, builder.build());
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
