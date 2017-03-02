package com.snaptiongame.app.data.providers;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

/**
 * @author Tyler Wong
 */

public class FacebookShareProvider {
    public static void shareToFacebook(AppCompatActivity activity, ImageView image) {
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
}
