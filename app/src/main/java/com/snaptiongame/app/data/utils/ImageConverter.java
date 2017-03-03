package com.snaptiongame.app.data.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ImageConverter {
    public static Observable<String> convertImageBase64(ContentResolver resolver, Uri uri) {
        String picture = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MediaStore.Images.Media.getBitmap(resolver, uri).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            picture = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            byteArrayOutputStream.close();
        }
        catch (IOException e) {
            Timber.e(e);
        }

        return Observable.just(picture);
    }

    public static Observable<byte[]> convertImageByteArray(ContentResolver resolver, Uri uri) {
        byte[] byteArray = new byte[1];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MediaStore.Images.Media.getBitmap(resolver, uri).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        }
        catch (IOException e) {
            Timber.e(e);
        }

        return Observable.just(byteArray);
    }
}