package com.daan.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daan.android.inventoryapp.firebase.AuthenticationService;
import com.daan.android.inventoryapp.utils.Loader;
import com.daan.android.inventoryapp.utils.ValidationUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Order(1)
    @BindView(R.id.et_login_username)
    @NotEmpty
    @Email(messageResId = R.string.err_email)
    EditText usernameField;

    @Order(2)
    @BindView(R.id.et_login_password)
    @Password(messageResId = R.string.err_password)
    EditText passwordField;

    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.login_error)
    TextView loginError;

    private Validator validator;
    private boolean loginPressed = false;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        authService = AuthenticationService.of(this);

        if (authService.isSignedIn()) {
            startMainScreen();
            return;
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.validate();

        setupGoogleLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    @OnClick(R.id.btn_login)
    public void loginClick() {
        loginPressed = true;
        validator.validate();
    }

    @OnClick(R.id.image_btn_google)
    public void googleLoginClick() {
//        Loader.showSpinner(this);
        authService.signInGoogle();
    }

    @OnFocusChange({R.id.et_login_username, R.id.et_login_password})
    public void onFocusChange(View v) {
        if (validator != null) {
            validator.validateTill(v);
        }
    }

    @OnTextChanged({R.id.et_login_username, R.id.et_login_password})
    public void onTextChange() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        loginButton.setEnabled(true);
        if (loginPressed) {
            login();
        }
        loginPressed = false;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        loginButton.setEnabled(false);
        ValidationUtils.displayErrors(this, errors);
        loginPressed = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        validator.validate();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authService.handleGoogleSignIn(requestCode, resultCode, data);
    }

    private void login() {
        loginError.setVisibility(GONE);
        Loader.showSpinner(this);
        disposables.add(authService.login(usernameField.getText().toString(), passwordField.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResult -> {
                    Loader.removeSpinner(this);
                    startMainScreen();
                }, exception -> {
                    Loader.removeSpinner(this);
                    showError(exception.getMessage());
                }));
    }

    private void setupGoogleLogin() {
        disposables.add(authService.getGoogleSignInObservable().observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Loader.removeSpinner(this);
                    startMainScreen();
                }, exception -> {
                    Loader.removeSpinner(this);
                    showError(exception.getMessage());
                }));
    }

    private void showError(String message) {
        loginError.setText(message);
        loginError.setVisibility(View.VISIBLE);
    }

    private void startMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
