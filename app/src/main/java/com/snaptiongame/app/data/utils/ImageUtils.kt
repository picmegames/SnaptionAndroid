package com.snaptiongame.app.data.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64

import com.snaptiongame.app.SnaptionApplication

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

import io.reactivex.Observable
import timber.log.Timber

/**
 * @author Tyler Wong
 */
object ImageUtils {

    private const val MAX_WIDTH = 1280.0f
    private const val MAX_HEIGHT = 1280.0f
    private const val NUM_MB = 16
    private const val NUM_BYTES = 1024
    private const val MIDDLE_FACTOR = 2.0f
    private const val QUALITY = 100
    private const val NINETY_DEGREES = 90
    private const val ONE_EIGHTY_DEGREES = 180
    private const val TWO_SEVENTY_DEGREES = 270
    private const val BUFFER_SIZE = 8192

    fun getCompressedImage(uri: Uri): Observable<String> {
        return Observable.defer { Observable.just(convertImageBase64(uri)) }
    }

    private fun convertImageBase64(uri: Uri): String {
        var picture = ""

        try {
            var bytesRead: Int = 0
            val inputStream = FileInputStream(compressImage(uri))
            val buffer = ByteArray(BUFFER_SIZE)
            val output = ByteArrayOutputStream()

            while ({ bytesRead = inputStream.read(buffer); bytesRead }() != -1) {
                output.write(buffer, 0, bytesRead)
            }

            picture = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)

            inputStream.close()
            output.close()
        }
        catch (e: IOException) {
            Timber.e(e)
        }

        return picture
    }

    fun getBitmapFromURL(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        }
        catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun getCircularBitmapFromUrl(imageUrl: String): Bitmap {
        return convertToCircularBitmap(getBitmapFromURL(imageUrl))
    }

    private fun convertToCircularBitmap(bitmap: Bitmap?): Bitmap {
        val output = Bitmap.createBitmap(bitmap!!.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                (bitmap.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun compressImage(imageUri: Uri): String {
        val resolver = SnaptionApplication.getContext().contentResolver
        val filePath = getImageUrlWithAuthority(resolver, imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        var imgRatio = actualWidth.toFloat() / actualHeight
        val maxRatio = MAX_WIDTH / MAX_HEIGHT

        if (actualHeight > MAX_HEIGHT || actualWidth > MAX_WIDTH) {
            if (imgRatio < maxRatio) {
                imgRatio = MAX_HEIGHT / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = MAX_HEIGHT.toInt()
            }
            else if (imgRatio > maxRatio) {
                imgRatio = MAX_WIDTH / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = MAX_WIDTH.toInt()
            }
            else {
                actualHeight = MAX_HEIGHT.toInt()
                actualWidth = MAX_WIDTH.toInt()
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inTempStorage = ByteArray(NUM_MB * NUM_BYTES)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        }
        catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        }
        catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / MIDDLE_FACTOR
        val middleY = actualHeight / MIDDLE_FACTOR

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()

            if (orientation == 6) {
                matrix.postRotate(NINETY_DEGREES.toFloat())
            }
            else if (orientation == 3) {
                matrix.postRotate(ONE_EIGHTY_DEGREES.toFloat())
            }
            else if (orientation == 8) {
                matrix.postRotate(TWO_SEVENTY_DEGREES.toFloat())
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix, true)
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        val out: FileOutputStream
        try {
            out = FileOutputStream(filePath)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, out)
        }
        catch (e: FileNotFoundException) {
            Timber.e("Could not find file")
        }

        return filePath
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val cursor = SnaptionApplication.getContext().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        }
        else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(index)
            cursor.close()
            return path
        }
    }

    private fun getImageUrlWithAuthority(resolver: ContentResolver, uri: Uri): String {
        val inputStream: InputStream?

        if (uri.authority != null) {
            try {
                inputStream = resolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(inputStream)
                val path = getRealPathFromURI(writeToTempImageAndGetPathUri(resolver, bmp))
                inputStream?.close()
                return path
            }
            catch (e: IOException) {
                Timber.e(e)
            }

        }
        return ""
    }

    private fun writeToTempImageAndGetPathUri(resolver: ContentResolver, inImage: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(resolver, inImage, null, null)
        return Uri.parse(path)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }
}
