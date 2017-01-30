package com.snaptiongame.snaptionapp.data.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rx.Observable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ImageConverter {
   public static Observable<String> convertImage(Drawable drawable) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
      bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
      String picture = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

      try {
         byteArrayOutputStream.close();
      }
      catch (IOException e) {
         Timber.e(e);
      }

      return Observable.just(picture);
   }
}
