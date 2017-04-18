package com.snaptiongame.app.data.providers;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

public class FacebookShareProvider {
    public static void shareToFacebook(AppCompatActivity activity, ImageView image) {
        try {
            ApplicationInfo info = activity.getPackageManager().
                    getApplicationInfo(activity.getString(R.string.facebook_app), 0 );

            image.setDrawingCacheEnabled(true);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image.getDrawingCache())
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
        catch( PackageManager.NameNotFoundException e ) {
            Toast.makeText(activity, R.string.no_facebook, Toast.LENGTH_SHORT).show();
        }
    }
}
