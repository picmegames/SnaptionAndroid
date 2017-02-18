package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

import static android.R.color.transparent;

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
   @BindView(R.id.tab_layout)
   TabLayout mTabLayout;
   @BindView(R.id.view_pager)
   ViewPager mViewPager;

   private MaterialDialog mEditDialog;
   private EditProfileView mEditView;
   private ActionBar mActionBar;
   private Uri mUri;

   private int mColorPrimary;
   private int mTransparent;

   private AuthenticationManager mAuthManager;
   private ProfileContract.Presenter mPresenter;
   private boolean mIsTheTitleVisible = false;
   private boolean mIsTheTitleContainerVisible = true;

   private static final int PROFILE_IMG_ELEVATION = 40;
   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION = 200;
   private static final int BLUR_RADIUS = 40;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_profile);
      ButterKnife.bind(this);

      mAuthManager = AuthenticationManager.getInstance(this);
      mPresenter = new ProfilePresenter(this);

      String name = getIntent().getStringExtra(AuthenticationManager.FULL_NAME);
      String profileUrl = getIntent().getStringExtra(AuthenticationManager.PROFILE_IMAGE_URL);

      supportPostponeEnterTransition();

      updateProfilePicture(profileUrl);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         mProfileImg.setElevation(PROFILE_IMG_ELEVATION);
      }

      mToolbar.showOverflowMenu();
      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();
      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setDisplayShowTitleEnabled(false);
      }
      mAppBar.addOnOffsetChangedListener(this);
      startAlphaAnimation(mTitle, 0, View.INVISIBLE);

      mViewPager.setAdapter(
            new ProfileInfoPageAdapter(getSupportFragmentManager(), this));

      mTabLayout.setupWithViewPager(mViewPager);
      int white = ContextCompat.getColor(this, android.R.color.white);
      mTabLayout.setTabTextColors(white, white);

      mTitle.setText(name);
      mMainTitle.setText(name);

      mColorPrimary  = ContextCompat.getColor(this, R.color.colorPrimary);
      mTransparent = ContextCompat.getColor(this, transparent);
   }

   @OnClick(R.id.fab)
   public void showEditDialog() {
      mEditView = new EditProfileView(this, mAuthManager);

      mEditDialog = new MaterialDialog.Builder(this)
            .title(getString(R.string.update_info))
            .customView(mEditView, false)
            .positiveText(getString(R.string.confirm))
            .negativeText(R.string.cancel)
            .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
               mPresenter.updateUsername(mAuthManager.getSnaptionUserId(),
                     mAuthManager.getSnaptionUsername(), new User(mEditView.getNewUsername()));
            })
            .onNegative((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {

            })
            .show();
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
      mAuthManager.saveSnaptionUsername(username);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (resultCode == RESULT_OK) {
         mUri = data.getData();
         mPresenter.convertImage(mAuthManager.getSnaptionUserId(), getContentResolver(), mUri);
      }
   }

   private void updateProfilePicture(String profileUrl) {
      if (mEditView != null) {
         mEditView.updateProfilePicture(profileUrl);
      }

      Glide.with(this)
            .load(profileUrl)
            .dontAnimate()
            .listener(new RequestListener<String, GlideDrawable>() {
               @Override
               public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                          boolean isFirstResource) {
                  supportStartPostponedEnterTransition();
                  return false;
               }

               @Override
               public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                              boolean isFromMemoryCache, boolean isFirstResource) {
                  supportStartPostponedEnterTransition();
                  return false;
               }
            })
            .into(mProfileImg);

      Glide.with(this)
            .load(profileUrl)
            .bitmapTransform(
                  new CenterCrop(this),
                  new BlurTransformation(this, BLUR_RADIUS),
                  new ColorFilterTransformation(this, R.color.colorPrimary))
            .into(mCoverPhoto);
   }

   @Override
   public void showProfilePictureSuccess() {
      Snackbar.make(
            mLayout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
      updateProfilePicture(mAuthManager.getProfileImageUrl());
   }

   @Override
   public void showProfilePictureFailure() {
      Snackbar.make(
            mLayout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
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
   public void showUsernameFailure(String oldUsername, User user) {
      Snackbar
            .make(mLayout, getString(R.string.update_failure), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.try_again), view -> mEditDialog.show())
            .show();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.profile_menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
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
         mToolbar.setBackgroundColor(mColorPrimary);
      }
      else {
         mActionBar.setElevation(0);
         mToolbar.setBackgroundColor(mTransparent);
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
