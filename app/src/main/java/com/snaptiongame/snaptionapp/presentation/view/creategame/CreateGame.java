package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Nick Romero
 */

public class CreateGame extends AppCompatActivity {
   @BindView(R.id.newGameImage)
   ImageView mNewGameImage;
   @BindView(R.id.createGameUser)
   TextView mUsernameView;
   @BindView(R.id.contentRatingsSpinner)
   Spinner mContentSpinner;
   @BindView(R.id.categorySpinner)
   Spinner mCategorySpinner;
   @BindView(R.id.public_switch)
   Switch mPublicSwitch;

   private String mEncodedImage;
   private String mType;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_game);
      ButterKnife.bind(this);

      assignSpinnerValues();
   }

   @OnClick(R.id.newGameImage)
   public void getImage() {
      Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
      imagePickerIntent.setType("image/*");
      startActivityForResult(imagePickerIntent, 1);
   }

   @OnClick(R.id.createGameButton)
   public void createGame() {
      if (mNewGameImage.getDrawable() != null) {
         SnaptionProvider.addSnaption(
               new Snaption(!mPublicSwitch.isChecked(), 1, mEncodedImage, mType))
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Subscriber<Snaption>() {
                  @Override
                  public void onCompleted() {
                     Timber.i("Posted successfully");
                     onBackPressed();
                  }

                  @Override
                  public void onError(Throwable e) {
                     Timber.e(e);
                  }

                  @Override
                  public void onNext(Snaption snaption) {

                  }
               });
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
         Uri uri = data.getData();
         mNewGameImage.setImageURI(uri);
         mType = getContentResolver().getType(uri);

         convertImageToBase64()
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Subscriber<String>() {
                  @Override
                  public void onCompleted() {
                     Timber.i("Successfully encoded image.");
                  }

                  @Override
                  public void onError(Throwable e) {
                     Timber.e(e);
                  }

                  @Override
                  public void onNext(String s) {
                     mEncodedImage = s;
                  }
               });
      }
   }

   private void assignSpinnerValues() {
      ArrayAdapter contentAdapter = ArrayAdapter.createFromResource(this,
            R.array.content_ratings_array, android.R.layout.simple_spinner_item);

      ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
            R.array.categories_array, android.R.layout.simple_spinner_item);

      contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      mContentSpinner.setAdapter(contentAdapter);
      mCategorySpinner.setAdapter(categoryAdapter);
   }

   private Observable<String> convertImageToBase64() {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Drawable imageDrawable = mNewGameImage.getDrawable();
      Bitmap bmp = ((BitmapDrawable) imageDrawable).getBitmap();
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
