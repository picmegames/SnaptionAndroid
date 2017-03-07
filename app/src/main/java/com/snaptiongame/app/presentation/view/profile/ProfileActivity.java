package com.snaptiongame.app.presentation.view.profile;

import android.content.Intent;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.login.LoginActivity;

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

    private AuthenticationManager mAuthManager;
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
        mAuthManager = AuthenticationManager.getInstance();

        // GET previous intent
        Intent profileIntent = getIntent();
        mUserId = profileIntent.getIntExtra(User.ID, 0);
        mIsUserProfile = profileIntent.getBooleanExtra(IS_CURRENT_USER, true);
        mHasSameUserId = (mUserId == mAuthManager.getUserId());

        // IF we are viewing the logged-in user's profile
        if (mIsUserProfile || mHasSameUserId) {
            mName = mAuthManager.getUsername();
            mPicture = mAuthManager.getProfileImageUrl();
        }
        else {
            mName = profileIntent.getStringExtra(User.USERNAME);
            mPicture = profileIntent.getStringExtra(User.PICTURE);
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

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }
        mAppBar.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        mViewPager.setAdapter(new ProfileInfoPageAdapter(getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);
        int white = ContextCompat.getColor(this, android.R.color.white);
        mTabLayout.setTabTextColors(white, white);

        mColorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        mTransparent = ContextCompat.getColor(this, transparent);
    }

    /**
     * This method will show the dialog where the user can edit their information.
     */
    @OnClick(R.id.fab)
    public void showEditDialog() {
        mEditView = new EditProfileView(this, mAuthManager);

        mEditDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.update_info))
                .customView(mEditView, false)
                .positiveText(getString(R.string.confirm))
                .negativeText(R.string.cancel)
                .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
                    mPresenter.updateUsername(mAuthManager.getUsername(),
                            new User(mEditView.getNewUsername()));
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
        mAuthManager.saveProfileImage(picture);
    }

    @Override
    public void saveUsername(String username) {
        mAuthManager.saveUsername(username);
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

        if (mPicture != null && !mPicture.isEmpty()) {
            Glide.with(this)
                    .load(mPicture)
                    .into(mProfileImg);

            Glide.with(this)
                    .load(mPicture)
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
                    .buildRound(mName.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(mName)));

            mCoverPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void showProfilePictureSuccess() {
        Snackbar.make(
                mLayout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
        mPicture = mAuthManager.getProfileImageUrl();
        updateProfilePicture();
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
                        mPresenter.updateUsername(user.username, new User(oldUsername)))
                .show();
        mTitle.setText(user.username);
        mMainTitle.setText(user.username);
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
                mPresenter.logout();
                break;
            default:
                break;
        }
        return true;
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
