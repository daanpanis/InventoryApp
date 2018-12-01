package com.daan.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.daan.android.inventoryapp.firebase.LoginService;
import com.daan.android.inventoryapp.utils.Loader;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @BindView(R.id.et_login_username)
    EditText inputUsername;
    @BindView(R.id.et_login_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.btn_register)
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setupLoginButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void setupLoginButton() {
        loginButton.setOnClickListener(v ->
                disposables.add(LoginService.login(inputUsername.getText().toString(), inputPassword.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(exception -> {

                        }).subscribe(authResult -> {

                        })));
    }

}
