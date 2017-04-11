package com.snaptiongame.app.data.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.snaptiongame.app.SnaptionApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ImageUtils {

    private static final float MAX_WIDTH = 1280.0f;
    private static final float MAX_HEIGHT = 1280.0f;
    private static final int NUM_MB = 16;
    private static final int NUM_BYTES = 1024;
    private static final float MIDDLE_FACTOR = 2.0f;
    private static final int QUALITY = 100;
    private static final String FOLDER = "Pictures/Snaption";
    private static final String JPEG = ".jpg";

    public static Observable<String> getCompressedImage(Uri uri) {
        return Observable.defer(() -> Observable.just(convertImageBase64(uri)));
    }

    private static String convertImageBase64(Uri uri) {
        String picture = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BitmapFactory.decodeFile(compressImage(uri)).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            picture = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            byteArrayOutputStream.close();
        }
        catch (IOException e) {
            Timber.e(e);
        }

        return picture;
    }

    private static String compressImage(Uri imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / actualHeight;
        float maxRatio = MAX_WIDTH / MAX_HEIGHT;

        if (actualHeight > MAX_HEIGHT || actualWidth > MAX_WIDTH) {
            if (imgRatio < maxRatio) {
                imgRatio = MAX_HEIGHT / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) MAX_HEIGHT;
            }
            else if (imgRatio > maxRatio) {
                imgRatio = MAX_WIDTH / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) MAX_WIDTH;
            }
            else {
                actualHeight = (int) MAX_HEIGHT;
                actualWidth = (int) MAX_WIDTH;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[NUM_MB * NUM_BYTES];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        }
        catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        }
        catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / MIDDLE_FACTOR;
        float middleY = actualHeight / MIDDLE_FACTOR;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        FileOutputStream out;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, out);
        }
        catch (FileNotFoundException e) {
            Timber.e("Could not find file");
        }

        return filename;
    }

    private static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + JPEG);
        return uriSting;
    }

    private static String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = SnaptionApplication.getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(index);
            cursor.close();
            return path;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}