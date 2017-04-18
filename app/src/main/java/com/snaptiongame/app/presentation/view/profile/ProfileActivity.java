package com.snaptiongame.app.presentation.view.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.behaviors.ProfileImageBehavior;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.photo.ImmersiveActivity;
import com.snaptiongame.app.presentation.view.transitions.TransitionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

import static android.R.color.transparent;

/**
 * The Profile Activity is an activity that displays a Game user's information. It can be used
 * to display either the current logged in user, or the user's friend. From here, a user will be
 * able to change their username or profile picture, log out, view their past games, or view more
 * info about themselves.
 *
 * @author Tyler Wong
 * @version 1.0
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
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private MaterialDialog mEditDialog;
    private EditProfileView mEditView;
    private ActionBar mActionBar;
    private Uri mUri;

    private String mName;
    private String mPicture;
    private int mUserId;

    private int mColorPrimary;
    private int mTransparent;

    private ProfileContract.Presenter mPresenter;
    private boolean mIsUserProfile;
    private boolean mHasSameUserId;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private static final int PROFILE_IMG_ELEVATION = 40;
    private static final int DEFAULT_IMG_SIZE = 140;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final int BLUR_RADIUS = 40;
    private static final String SPACES_DELIMITER = "\\s+";

    public static final String IS_CURRENT_USER = "is_current_user";

    /**
     * This method creates and initializes the view for the Profile Activity.
     *
     * @param savedInstanceState The previous state of the activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mPresenter = new ProfilePresenter(this);

        // GET previous intent
        Intent profileIntent = getIntent();
        mUserId = profileIntent.getIntExtra(User.ID, 0);
        mIsUserProfile = profileIntent.getBooleanExtra(IS_CURRENT_USER, true);
        mHasSameUserId = (mUserId == AuthManager.getUserId());

        // IF we are viewing the logged-in user's profile
        if (mIsUserProfile || mHasSameUserId) {
            mName = AuthManager.getUsername();
            mPicture = AuthManager.getProfileImageUrl();
        }
        else {
            mName = profileIntent.getStringExtra(User.USERNAME);
            mPicture = profileIntent.getStringExtra(User.IMAGE_URL);
            mFab.setVisibility(View.GONE);
        }

        // SETUP toolbar and title with user's name
        mTitle.setText(mName);
        mMainTitle.setText(mName);

        // SHOW profile picture
        updateProfilePicture();

        // IF the device is running Lollipop or higher, set elevation on the image
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProfileImg.setElevation(PROFILE_IMG_ELEVATION);
        }

        mProfileImg.setOnClickListener(view -> {
            if (mPicture != null && !mPicture.isEmpty()) {
                Intent immersiveIntent = new Intent(this, ImmersiveActivity.class);
                immersiveIntent.putExtra(ImmersiveActivity.IMAGE_URL, mPicture);
                startActivity(immersiveIntent);
            }
        });

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }
        mAppBar.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        mViewPager.setAdapter(new ProfileInfoPageAdapter(getSupportFragmentManager(), mUserId));

        mTabLayout.setupWithViewPager(mViewPager);
        int white = ContextCompat.getColor(this, android.R.color.white);
        mTabLayout.setTabTextColors(white, white);

        mColorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        mTransparent = ContextCompat.getColor(this, transparent);

        TransitionUtils.setupArcTransition(this, getWindow());
    }

    /**
     * This method will show the dialog where the user can edit their information.
     */
    @OnClick(R.id.fab)
    public void showEditDialog() {
        if (isStoragePermissionGranted()) {
            if (mEditDialog == null) {
                mEditView = new EditProfileView(this);

                mEditDialog = new MaterialDialog.Builder(this)
                        .title(getString(R.string.update_info))
                        .customView(mEditView, false)
                        .positiveText(getString(R.string.confirm))
                        .negativeText(R.string.cancel)
                        .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
                            String newUsername = mEditView.getNewUsername();
                            if (!newUsername.replaceAll(SPACES_DELIMITER, "").isEmpty()) {
                                mPresenter.updateUsername(AuthManager.getUsername(), new User(newUsername));
                            }
                            else {
                                showUsernameFailure(getString(R.string.empty_username));
                            }
                        })
                        .onNegative((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {

                        })
                        .show();
            }
            else {
                mEditDialog.show();
            }
        }
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showEditDialog();
        }
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void saveProfilePicture(String picture) {
        AuthManager.saveProfileImage(picture);
    }

    @Override
    public void saveUsername(String username) {
        AuthManager.saveUsername(username);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mUri = data.getData();
            mPresenter.convertImage(getContentResolver().getType(mUri), mUri);
        }
    }

    private void updateProfilePicture() {
        if (mEditView != null) {
            mEditView.updateProfilePicture(mPicture);
        }

        String initials = "";
        if (!mName.isEmpty()) {
            initials = mName.substring(0, 1);
        }

        if (mPicture != null && !mPicture.isEmpty()) {
            Glide.with(this)
                    .load(mPicture)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .priority(Priority.IMMEDIATE)
                    .placeholder(TextDrawable.builder()
                            .beginConfig()
                            .width(DEFAULT_IMG_SIZE)
                            .height(DEFAULT_IMG_SIZE)
                            .toUpperCase()
                            .endConfig()
                            .buildRound(initials, ColorGenerator.MATERIAL.getColor(mName)))
                    .dontAnimate()
                    .into(mProfileImg);

            Glide.with(this)
                    .load(mPicture)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .priority(Priority.IMMEDIATE)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(mPicture)))
                    .bitmapTransform(
                            new CenterCrop(this),
                            new BlurTransformation(this, BLUR_RADIUS),
                            new ColorFilterTransformation(this, R.color.colorPrimary))
                    .into(mCoverPhoto);
        }
        else {
            mProfileImg.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(DEFAULT_IMG_SIZE)
                    .height(DEFAULT_IMG_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(initials, ColorGenerator.MATERIAL.getColor(mName)));

            mCoverPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void showProfilePictureSuccess() {
        Snackbar.make(mLayout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
        mPicture = AuthManager.getProfileImageUrl();
        updateProfilePicture();
    }

    @Override
    public void showProfilePictureFailure() {
        Snackbar.make(mLayout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUsernameSuccess(String oldUsername, User user) {
        Snackbar.make(mLayout, getString(R.string.update_success), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), view ->
                        mPresenter.updateUsername(user.username, new User(oldUsername)))
                .show();
        mTitle.setText(user.username);
        mMainTitle.setText(user.username);
        mEditView.updateUsername(user.username);
    }

    @Override
    public void showUsernameFailure(String message) {
        Snackbar.make(mLayout, message, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), view -> mEditDialog.show())
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!mIsUserProfile && !mHasSameUserId) {
            menu.findItem(R.id.log_out).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.log_out:
                logout();
                break;
            default:
                break;
        }
        return true;
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .title(R.string.log_out_label)
                .content(R.string.log_out_content)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                    mPresenter.logout();
                    finish();
                })
                .show();
    }

    @Override
    public void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLayout.setPadding(0, (int) Math.floor(ProfileImageBehavior.getStatusBarHeight(this) * percentage), 0, 0);
        }
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
