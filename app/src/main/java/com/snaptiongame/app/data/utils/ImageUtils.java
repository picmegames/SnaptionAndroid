package com.snaptiongame.app.data.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private static final int NINETY_DEGREES = 90;
    private static final int ONE_EIGHTY_DEGREES = 180;
    private static final int TWO_SEVENTY_DEGREES = 270;
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

    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getCircularBitmapFromUrl(String imageUrl) {
        return convertToCircularBitmap(getBitmapFromURL(imageUrl));
    }

    private static Bitmap convertToCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private static String compressImage(Uri imageUri) {
        ContentResolver resolver = SnaptionApplication.getContext().getContentResolver();
        String filePath = getImageUrlWithAuthority(resolver, imageUri);
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

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();

            if (orientation == 6) {
                matrix.postRotate(NINETY_DEGREES);
            }
            else if (orientation == 3) {
                matrix.postRotate(ONE_EIGHTY_DEGREES);
            }
            else if (orientation == 8) {
                matrix.postRotate(TWO_SEVENTY_DEGREES);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

    private static String getImageUrlWithAuthority(ContentResolver resolver, Uri uri) {
        InputStream is;

        if (uri.getAuthority() != null) {
            try {
                is = resolver.openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                String path = getRealPathFromURI(writeToTempImageAndGetPathUri(resolver, bmp));
                if (is != null) {
                    is.close();
                }
                return path;
            }
            catch (IOException e) {
                Timber.e(e);
            }
        }
        return null;
    }

    private static Uri writeToTempImageAndGetPathUri(ContentResolver resolver, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, QUALITY, bytes);
        String path = MediaStore.Images.Media.insertImage(resolver, inImage, FOLDER, null);
        return Uri.parse(path);
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
