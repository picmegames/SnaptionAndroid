package com.snaptiongame.app.presentation.view.creategame;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CreateGameContract {
    interface View extends BaseView<Presenter> {
        void createGame();

        void setFriendNames(String[] friendNames);

        void onBackPressed();

        void showUploadComplete();

        List<String> getTags();

        List<String> getAddedFriends();

        Context getContext();
    }

    interface Presenter extends BasePresenter {
        void createGame(ContentResolver resolver, Uri uri, Drawable drawable, int userId, boolean isPublic);

        int getFriendIdByName(String name);
    }
}
