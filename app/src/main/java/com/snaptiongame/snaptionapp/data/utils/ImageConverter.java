package com.snaptiongame.snaptionapp.data.utils;

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
    public static Observable<String> convertImage(ContentResolver resolver, Uri uri) {
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
}
