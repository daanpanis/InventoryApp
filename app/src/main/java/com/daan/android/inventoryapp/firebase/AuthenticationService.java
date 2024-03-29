package com.daan.android.inventoryapp.firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.Preconditions;

import com.daan.android.inventoryapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class AuthenticationService {

    private static final int RC_SIGN_IN = 5632;

    public static AuthenticationService of(Activity activity) {
        return new AuthenticationService(activity);
    }

    public static boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private final Activity activity;
    private final GoogleSignInClient signInClient;
    private final PublishSubject<AuthResult> googleSignInResult = PublishSubject.create();

    private AuthenticationService(Activity activity) {
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.activity.getString(R.string.web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this.activity, gso);
    }

    public Single<AuthResult> login(String username, String password) {
        return Single.create(emitter -> FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        emitter.onSuccess(task.getResult());
                    } else if (task.getException() != null) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onError(new Exception("An unknown error occurred while logging in."));
                    }
                }));
    }

    public void signInGoogle() {
        Preconditions.checkNotNull(activity);
        Intent intent = signInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void handleGoogleSignIn(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = signInTask.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(Objects.requireNonNull(account).getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(activity, task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                googleSignInResult.onNext(task.getResult());
                            } else if (task.getException() != null) {
                                googleSignInResult.onError(task.getException());
                            } else {
                                googleSignInResult.onError(new Exception("An unknown error occurred while logging in."));
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                googleSignInResult.onError(e);
            }
        }
    }

    public Observable<AuthResult> getGoogleSignInObservable() {
        return googleSignInResult;
    }

    public void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return;
        }
        auth.signOut();
    }
}
