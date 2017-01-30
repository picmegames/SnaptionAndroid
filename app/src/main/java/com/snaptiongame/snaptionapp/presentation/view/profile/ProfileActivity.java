package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.utils.ImageConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ProfileActivity extends AppCompatActivity
      implements AppBarLayout.OnOffsetChangedListener, ProfileContract.View {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.cover_photo)
   ImageView mCoverPhoto;
   @BindView(R.id.profile_image)
   ImageView mProfileImg;
   @BindView(R.id.name)
   TextView mTitle;
   @BindView(R.id.main_title)
   TextView mMainTitle;
   @BindView(R.id.layout)
   CoordinatorLayout mLayout;
   @BindView(R.id.app_bar)
   AppBarLayout mAppBar;
   @BindView(R.id.collapsing_toolbar)
   CollapsingToolbarLayout mCollapsingLayout;
   @BindView(R.id.info_view)
   ProfileInfoView mInfoView;

   private ActionBar mActionBar;

   private String mEncodedImage;
   private String mType;

   private AuthenticationManager mAuthManager;
   private ProfileContract.Presenter mPresenter;
   private boolean mIsTheTitleVisible = false;
   private boolean mIsTheTitleContainerVisible = true;

   private static final int PROFILE_IMG_ELEVATION = 40;
   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION = 200;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_profile);
      ButterKnife.bind(this);

      mAuthManager = AuthenticationManager.getInstance(this);
      mPresenter = new ProfilePresenter(this);
      mInfoView.setPresenter(mPresenter);

      String coverPhoto = getIntent().getStringExtra(AuthenticationManager.COVER_PHOTO_URL);
      String name = getIntent().getStringExtra(AuthenticationManager.FULL_NAME);

      Glide.with(this)
            .load(getIntent().getStringExtra(AuthenticationManager.PROFILE_IMAGE_URL))
            .into(mProfileImg);

      if (!coverPhoto.isEmpty()) {
         Glide.with(this)
               .load(coverPhoto)
               .centerCrop()
               .into(mCoverPhoto);
      }
      else {
         Glide.with(this)
               .load(R.drawable.snaption_background)
               .centerCrop()
               .into(mCoverPhoto);
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         mProfileImg.setElevation(PROFILE_IMG_ELEVATION);
      }

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();
      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setDisplayShowTitleEnabled(false);
      }
      mAppBar.addOnOffsetChangedListener(this);
      startAlphaAnimation(mTitle, 0, View.INVISIBLE);

      mTitle.setText(name);
      mMainTitle.setText(name);

      mProfileImg.setOnClickListener(view -> {
         Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
         imagePickerIntent.setType("image/*");
         startActivityForResult(imagePickerIntent, 1);
      });
   }

   @Override
   public void onResume() {
      super.onResume();
      mPresenter.subscribe();
   }

   @Override
   public void onPause() {
      super.onPause();
      mPresenter.unsubscribe();
   }

   @Override
   public void setPresenter(ProfileContract.Presenter presenter) {
      mPresenter = presenter;
   }

   @Override
   public void saveProfilePicture(String picture) {
      mAuthManager.saveSnaptionProfileImage(picture);
   }

   @Override
   public void saveUsername(String username) {
      mInfoView.saveUsername(username);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (resultCode == RESULT_OK) {
         Uri uri = data.getData();
         mProfileImg.setImageURI(uri);
         mType = getContentResolver().getType(uri);

         ImageConverter.convertImage(mProfileImg.getDrawable())
               .subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(s -> mEncodedImage = s,
                     Timber::e,
                     () -> mPresenter.updateProfilePicture(
                           mAuthManager.getSnaptionUserId(), new User(mEncodedImage, mType)));
      }
   }

   @Override
   public void showProfilePictureSuccess() {
      Snackbar.make(mLayout, getString(R.string.update_profile_picture), Snackbar.LENGTH_LONG).show();
   }

   @Override
   public void showUsernameSuccess(String oldUsername, User user) {
      Snackbar
            .make(mLayout, getString(R.string.update_success), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo), view ->
                  mPresenter.updateUsername(
                        mAuthManager.getSnaptionUserId(), user.username, new User(oldUsername)))
            .show();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_edit, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         case R.id.edit_action:
            Toast.makeText(this, "Edit pressed!", Toast.LENGTH_LONG).show();
            break;
         default:
            break;
      }
      return true;
   }

   @Override
   public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
      int maxScroll = appBarLayout.getTotalScrollRange();
      float percentage = (float) Math.abs(offset) / (float) maxScroll;

      handleAlphaOnTitle(percentage);
      handleToolbarTitleVisibility(percentage);

      if (Math.abs(offset) >= mAppBar.getHeight() - TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics())) {
         mActionBar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
               getResources().getDisplayMetrics()));
      }
      else {
         mActionBar.setElevation(0);
      }

      mLayout.setPadding(0, (int) Math.floor(ImageBehavior.getStatusBarHeight(this) * percentage), 0, 0);
   }

   private void handleToolbarTitleVisibility(float percentage) {
      if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
         if (!mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleVisible = true;
         }

      }
      else {
         if (mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleVisible = false;
         }
      }
   }

   private void handleAlphaOnTitle(float percentage) {
      if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
         if (mIsTheTitleContainerVisible) {
            startAlphaAnimation(mMainTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleContainerVisible = false;
         }

      }
      else {
         if (!mIsTheTitleContainerVisible) {
            startAlphaAnimation(mMainTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleContainerVisible = true;
         }
      }
   }

   public static void startAlphaAnimation(View view, long duration, int visibility) {
      AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
            ? new AlphaAnimation(0f, 1f)
            : new AlphaAnimation(1f, 0f);

      alphaAnimation.setDuration(duration);
      alphaAnimation.setFillAfter(true);
      view.startAnimation(alphaAnimation);
   }
}
