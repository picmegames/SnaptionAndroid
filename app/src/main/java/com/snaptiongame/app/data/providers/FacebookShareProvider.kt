package com.snaptiongame.app.data.providers

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast

import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.snaptiongame.app.R

/**
 * @author Tyler Wong
 */

object FacebookShareProvider {

    @JvmStatic
    fun shareToFacebook(activity: AppCompatActivity, image: ImageView) {
        try {
            activity.packageManager.getApplicationInfo(activity.getString(R.string.facebook_app), 0)

            image.isDrawingCacheEnabled = true
            val photo = SharePhoto.Builder()
                    .setBitmap(image.drawingCache)
                    .build()
            val content = SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build()

            val shareDialog = ShareDialog(activity)
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC)
        }
        catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(activity, R.string.no_facebook, Toast.LENGTH_SHORT).show()
        }

    }
}
