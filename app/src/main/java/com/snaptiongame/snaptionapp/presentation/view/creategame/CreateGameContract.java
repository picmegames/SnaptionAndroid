package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.ContentResolver;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class CreateGameContract {
   interface View extends BaseView<Presenter> {
      void createGame();
      void onBackPressed();
      void showCreateSuccess();
      void showCreateFailure();
   }

   interface Presenter extends BasePresenter {
      void convertImage(ContentResolver resolver, Uri uri, Drawable drawable, boolean isPublic);
      void createGame(Drawable drawable, String type, boolean isPublic);
   }
}
