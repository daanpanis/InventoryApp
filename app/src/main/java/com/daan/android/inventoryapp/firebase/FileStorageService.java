package com.daan.android.inventoryapp.firebase;

import android.graphics.Bitmap;

import com.daan.android.inventoryapp.utils.ImageUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import rx.Single;

import static com.daan.android.inventoryapp.utils.Constants.ITEM_PICTURES_FOLDER;

public class FileStorageService {

    public static FileStorageService getInstance() {
        return Helper.INSTANCE;
    }

    private static class Helper {
        private static FileStorageService INSTANCE = new FileStorageService();
    }

    private final FirebaseStorage storage;
    private final StorageReference itemPictures;

    private FileStorageService() {
        this.storage = FirebaseStorage.getInstance();
        this.itemPictures = storage.getReference(ITEM_PICTURES_FOLDER);
    }

    public Flowable<UploadTask.TaskSnapshot> uploadItemPictures(Collection<Bitmap> images) {
        return Flowable.fromIterable(images)
                .parallel().runOn(Schedulers.computation())
                .map(ImageUtils::bitmapToBytes)
                .filter(bytes -> bytes.length > 0)
                .map(bytes -> {
                    Single<UploadTask.TaskSnapshot> single = Single.create(subscriber -> {
                        StorageReference reference = itemPictures.child(UUID.randomUUID() + ".jpeg");
                        reference.putBytes(bytes)
                                .addOnSuccessListener(subscriber::onSuccess)
                                .addOnFailureListener(subscriber::onError);
                    });
                    return single.toBlocking().value();
                })
                .sequential();
    }
}
