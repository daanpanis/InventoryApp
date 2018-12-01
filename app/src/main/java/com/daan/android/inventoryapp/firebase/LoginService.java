package com.daan.android.inventoryapp.firebase;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LoginService {

    public static Observable<AuthResult> login(String username, String password) {
        PublishSubject<AuthResult> authResult = PublishSubject.create();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        authResult.onNext(task.getResult());
                    } else if (task.getException() != null) {
                        authResult.onError(task.getException());
                    }
                });
        return authResult;
    }

    private LoginService() {
    }

}
