package com.sagar.android.chatapp.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.sagar.android.chatapp.R;
import com.sagar.android.chatapp.core.Enums;
import com.sagar.android.chatapp.databinding.ActivitySettingsBinding;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.util.DialogUtil;
import com.sagar.android.chatapp.util.ProgressUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class Settings extends AppCompatActivity {

    @Inject
    public SettingsViewModelProvider viewModelProvider;
    @Inject
    public ProgressUtil progressUtil;

    private SettingsViewModel viewModel;
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setContext(this);

        setSupportActionBar(binding.toolbar);

        if (
                getSupportActionBar() != null
        ) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
                .get(SettingsViewModel.class);

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
        finish();
    }

    public void onClickLogoutAll(View view) {
        logoutAll();
    }

    private void bindToViewModel() {
        viewModel.mediatorLiveDataLogoutAllResult
                .observe(
                        this,
                        result -> {
                            if (result != null)
                                processLogoutAllResult(result);
                        }
                );
    }

    private void logoutAll() {
        progressUtil.show();
        viewModel.logoutAll();
    }

    private void processLogoutAllResult(Result result) {
        progressUtil.hide();
        if (result.getResult() == Enums.Result.FAIL) {
            DialogUtil.showDialogWithMessage(
                    this,
                    result.getMessage()
            );
        }
    }
}
