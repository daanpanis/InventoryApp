package com.daan.android.inventoryapp.utils;

import android.content.Context;
import android.support.v4.util.Preconditions;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

public class ValidationUtils {

    public static void displayErrors(Context context, List<ValidationError> errors) {
        Preconditions.checkNotNull(context);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(context);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private ValidationUtils() {
    }

}
