package com.daan.android.inventoryapp.firebase;

import android.graphics.BitmapFactory;
import android.net.Uri;

import com.daan.android.inventoryapp.models.Item;
import com.daan.android.inventoryapp.viewmodel.CreateItemViewModel;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java8.util.stream.Collectors;

import static com.daan.android.inventoryapp.utils.Constants.DB_ITEMS_COLLECTION;
import static com.daan.android.inventoryapp.utils.Constants.DB_ITEM_BARCODE;
import static com.daan.android.inventoryapp.utils.Constants.DB_ITEM_BARCODE_FORMAT;
import static com.daan.android.inventoryapp.utils.Constants.DB_ITEM_OWNER_ID;
import static java8.util.stream.StreamSupport.stream;

public class ItemStoreService {

    public static ItemStoreService getInstance() {
        return Helper.INSTANCE;
    }

    private static class Helper {
        private static final ItemStoreService INSTANCE = new ItemStoreService();
    }

    private final FirebaseFirestore database;

    private ItemStoreService() {
        this.database = FirebaseFirestore.getInstance();
    }

    @SuppressWarnings("unchecked")
    public Single<DocumentReference> addItem(CreateItemViewModel model) {
        CompositeDisposable disposables = new CompositeDisposable();
        return Single.create(emitter -> {
            if (!AuthenticationService.isSignedIn()) {
                emitter.onError(new Exception("You must be authenticated to do this"));
                return;
            }

            Item item = new Item(AuthenticationService.getUser().getUid());


            disposables.add(Single.create(emitter1 -> {
                if (Objects.requireNonNull(model.getImageUrls().getValue()).size() > 0) {
                    disposables.add(FileStorageService.getInstance()
                            .uploadItemPictures(
                                    stream(model.getImageUrls().getValue())
                                            .map(BitmapFactory::decodeFile)
                                            .collect(Collectors.toList())
                            ).parallel()
                            .runOn(Schedulers.computation())
                            .filter(taskSnapshot -> taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null)
                            .map(taskSnapshot ->
                                    Tasks.await(taskSnapshot.getMetadata().getReference().getDownloadUrl())
                            )
                            .sequential()
                            .subscribeOn(Schedulers.computation())
                            .map(Uri::toString)
                            .toList()
                            .subscribe(emitter1::onSuccess, emitter1::onError));
                } else {
                    emitter1.onSuccess(new ArrayList<String>());
                }
            }).map(o -> (List<String>) o).subscribe(strings -> {
                item.setImageUrls(strings);
                item.setName(model.getItemName().getValue());
                item.setBarcode(model.getItemBarcode().getValue());
                item.setBarcodeFormat(model.getItemBarcodeFormat().getValue());
                item.setDescription(model.getItemDescription().getValue());

                database.collection(DB_ITEMS_COLLECTION)
                        .add(item)
                        .addOnSuccessListener(emitter::onSuccess)
                        .addOnFailureListener(emitter::onError)
                        .addOnCanceledListener(() -> emitter.onError(new Exception("An unknown error occurred")));
            }));
        }).subscribeOn(Schedulers.computation()).cast(DocumentReference.class).doFinally(disposables::dispose);
    }

    public Observable<Collection<DocumentChange>> getUserItems() {
        return Observable.create(emitter -> {
            if (!AuthenticationService.isSignedIn()) {
                emitter.onError(new Exception("You must be authenticatde to do this"));
                return;
            }
            database.collection(DB_ITEMS_COLLECTION)
                    .whereEqualTo(DB_ITEM_OWNER_ID, AuthenticationService.getUser().getUid())
                    .addSnapshotListener((snapshots, e) -> {
                        if (snapshots != null) {
                            emitter.onNext(snapshots.getDocumentChanges());
                        } else {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Observable<Collection<DocumentChange>> getItemsByBarcode(String barcode, String barcodeFormat) {
        return Observable.create(emitter -> {
            if (!AuthenticationService.isSignedIn()) {
                emitter.onError(new Exception("You must be authenticatde to do this"));
                return;
            }
            database.collection(DB_ITEMS_COLLECTION)
                    .whereEqualTo(DB_ITEM_OWNER_ID, AuthenticationService.getUser().getUid())
                    .whereEqualTo(DB_ITEM_BARCODE, barcode)
                    .whereEqualTo(DB_ITEM_BARCODE_FORMAT, barcodeFormat)
                    .addSnapshotListener((snapshots, e) -> {
                        if (snapshots != null) {
                            emitter.onNext(snapshots.getDocumentChanges());
                        } else {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Observable<DocumentSnapshot> getItemById(String itemId) {
        return Observable.create(emitter -> {
            if (!AuthenticationService.isSignedIn()) {
                emitter.onError(new Exception("You must be authenticatde to do this"));
                return;
            }
            database.collection(DB_ITEMS_COLLECTION).document(itemId)
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null) {
                            emitter.onNext(snapshot);
                        } else {
                            emitter.onError(e);
                        }
                    });
        });
    }
}
