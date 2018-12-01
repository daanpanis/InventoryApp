package com.daan.android.inventoryapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.validate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        validator.validate();
    }

    @OnFocusChange({R.id.et_login_username, R.id.et_login_password})
    public void onFocusChange(View v) {
        Log.d("TESTEST", "Focus change: " + v.getId());
        validator.validateTill(v);
    }

    @OnTextChanged({R.id.et_login_username, R.id.et_login_password})
    public void onTextChange() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        loginButton.setEnabled(true);
        Log.d("TESTEST", "Validation succeeded");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        loginButton.setEnabled(false);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TESTEST", "Restore instance state 2");
        validator.validate();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d("TESTEST", "Restore instance state 1");
    }
}
