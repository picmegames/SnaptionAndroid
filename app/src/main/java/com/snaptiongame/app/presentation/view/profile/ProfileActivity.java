package com.snaptiongame.app.presentation.view.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.behaviors.ProfileImageBehavior;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.photo.ImmersiveActivity;
import com.snaptiongame.app.presentation.view.transformations.BlurTransformation;
import com.snaptiongame.app.presentation.view.transformations.ColorFilterTransformation;
import com.snaptiongame.app.presentation.view.utils.ShowcaseUtils;
import com.snaptiongame.app.presentation.view.utils.TransitionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    Toolbar toolbar;
    @BindView(R.id.cover_photo)
    ImageView coverPhoto;
    @BindView(R.id.profile_image)
    ImageView profileImg;
    @BindView(R.id.name)
    TextView title;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.layout)
    CoordinatorLayout layout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private MaterialDialog editDialog;
    private EditProfileView editView;
    private ActionBar actionBar;
    private Uri uri;
    private Menu menu;

    private String name;
    private String picture;
    private int userId;

    private int colorPrimary;
    private int transparent;

    private ProfileContract.Presenter presenter;
    private boolean isUserProfile;
    private boolean hasSameUserId;
    private boolean isTheTitleVisible = false;
    private boolean isTheTitleContainerVisible = true;

    public static final int IMAGE_PICKER_RESULT = 24;
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
        presenter = new ProfilePresenter(this);

        // GET previous intent
        Intent profileIntent = getIntent();
        userId = profileIntent.getIntExtra(User.ID, 0);
        isUserProfile = profileIntent.getBooleanExtra(IS_CURRENT_USER, false);
        hasSameUserId = (userId == AuthManager.getUserId());

        // IF we are viewing the logged-in user's profile
        if (isUserProfile || hasSameUserId) {
            name = AuthManager.getUsername();
            picture = AuthManager.getProfileImageUrl();
            setupView();
            ShowcaseUtils.showShowcase(this, fab, R.string.profile_showcase_title,
                    R.string.profile_showcase_content);
        }
        else if (profileIntent.hasExtra(User.USERNAME)) {
            name = profileIntent.getStringExtra(User.USERNAME);
            picture = profileIntent.getStringExtra(User.IMAGE_URL);
            fab.setVisibility(View.GONE);
            setupView();
        }
        else {
            fab.setVisibility(View.GONE);
            presenter.loadUser(userId);
        }

        // IF the device is running Lollipop or higher, set elevation on the image
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileImg.setElevation(PROFILE_IMG_ELEVATION);
        }

        profileImg.setOnClickListener(view -> {
            if (picture != null && !picture.isEmpty()) {
                Intent immersiveIntent = new Intent(this, ImmersiveActivity.class);
                immersiveIntent.putExtra(ImmersiveActivity.IMAGE_URL, picture);
                startActivity(immersiveIntent);
            }
        });

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        appBar.addOnOffsetChangedListener(this);
        startAlphaAnimation(title, 0, View.INVISIBLE);

        viewPager.setAdapter(new ProfileInfoPageAdapter(getSupportFragmentManager(), userId));

        tabLayout.setupWithViewPager(viewPager);
        int white = ContextCompat.getColor(this, android.R.color.white);
        tabLayout.setTabTextColors(white, white);

        colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        transparent = ContextCompat.getColor(this, android.R.color.transparent);

        TransitionUtils.setupArcTransition(this, getWindow());
    }

    @Override
    public void showHideAddFriend(boolean isVisible) {
        if (isVisible) {
            menu.findItem(R.id.add_friend).setVisible(true);
            menu.findItem(R.id.remove_friend).setVisible(false);
        }
        else {
            menu.findItem(R.id.add_friend).setVisible(false);
            menu.findItem(R.id.remove_friend).setVisible(true);
        }
    }

    @Override
    public void showUser(User user) {
        name = user.getUsername();
        picture = user.getImageUrl();

        setupView();
    }

    /**
     * This method will show the dialog where the user can edit their information.
     */
    @OnClick(R.id.fab)
    public void showEditDialog() {
        if (isStoragePermissionGranted()) {
            if (editDialog == null) {
                editView = new EditProfileView(this);

                editDialog = new MaterialDialog.Builder(this)
                        .title(getString(R.string.update_info))
                        .customView(editView, false)
                        .positiveText(getString(R.string.confirm))
                        .negativeText(R.string.cancel)
                        .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
                            String newUsername = editView.getNewUsername();

                            if (!newUsername.contains(" ")) {
                                presenter.updateUsername(AuthManager.getUsername(), new User(newUsername));
                            }
                            else {
                                showUsernameFailure(getString(R.string.spaces_in_name));
                            }
                        })
                        .show();
            }
            else {
                editView.clearUsernameField();
                editDialog.show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    private void setupView() {
        // SETUP toolbar and title with user's name
        title.setText(name);
        mainTitle.setText(name);

        // SHOW profile picture
        updateProfilePicture();
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
        this.presenter = presenter;
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

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_RESULT) {
            uri = data.getData();
            presenter.convertImage(getContentResolver().getType(uri), uri);
        }
    }

    private void updateProfilePicture() {
        if (editView != null) {
            editView.updateProfilePicture(picture);
        }

        String initials = "";
        if (!name.isEmpty()) {
            initials = name.substring(0, 1);
        }

        if (picture != null && !picture.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(TextDrawable.builder()
                            .beginConfig()
                            .width(DEFAULT_IMG_SIZE)
                            .height(DEFAULT_IMG_SIZE)
                            .toUpperCase()
                            .endConfig()
                            .buildRound(initials, ColorGenerator.MATERIAL.getColor(name)))
                    .dontAnimate();

            Glide.with(this)
                    .load(picture)
                    .apply(options)
                    .listener(listener)
                    .into(profileImg);

            options = new RequestOptions()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(new ColorDrawable(ColorGenerator.MATERIAL.getColor(picture)))
                    .transform(new MultiTransformation<>(new CenterCrop(), new BlurTransformation(this, BLUR_RADIUS),
                            new ColorFilterTransformation(this, R.color.colorPrimary)));

            Glide.with(this)
                    .load(picture)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(listener)
                    .into(coverPhoto);
        }
        else {
            profileImg.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(DEFAULT_IMG_SIZE)
                    .height(DEFAULT_IMG_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(initials, ColorGenerator.MATERIAL.getColor(name)));

            coverPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

            shouldLoadHideAddFriend();
        }
    }

    private RequestListener listener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            shouldLoadHideAddFriend();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            shouldLoadHideAddFriend();
            return false;
        }
    };

    private void shouldLoadHideAddFriend() {
        if (!hasSameUserId && AuthManager.isLoggedIn()) {
            presenter.loadShouldHideAddFriend(userId);
        }
    }

    @Override
    public void showProfilePictureSuccess() {
        Snackbar.make(layout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
        picture = AuthManager.getProfileImageUrl();
        updateProfilePicture();
    }

    @Override
    public void showProfilePictureFailure() {
        Snackbar.make(layout, getString(R.string.update_profile_picture_success), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUsernameSuccess(String oldUsername, User user) {
        Snackbar.make(layout, getString(R.string.update_success), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), view ->
                        presenter.updateUsername(user.getUsername(), new User(oldUsername)))
                .show();
        title.setText(user.getUsername());
        mainTitle.setText(user.getUsername());
        editView.updateUsername(user.getUsername());
    }

    @Override
    public void showRemoveFriendResult(boolean success) {
        String message = String.format(getString(R.string.remove_friend_failure), name);
        Snackbar snackbar;

        if (success) {
            message = String.format(getString(R.string.remove_friend_success), name);
            snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, view -> presenter.addFriend(userId));
        }
        else {
            snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.try_again, view -> presenter.removeFriend(userId));
        }

        snackbar.show();
    }

    @Override
    public void showAddFriendResult(boolean success) {
        String message = String.format(getString(R.string.add_friend_failure), name);
        Snackbar snackbar;

        if (success) {
            message = String.format(getString(R.string.add_friend_success), name);
            snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, view -> presenter.removeFriend(userId));
        }
        else {
            snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.try_again, view -> presenter.addFriend(userId));
        }
        snackbar.show();
    }

    @Override
    public void showUsernameFailure(String message) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.try_again, view -> showEditDialog())
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!isUserProfile && !hasSameUserId) {
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
            case R.id.add_friend:
                addFriend();
                break;
            case R.id.remove_friend:
                removeFriend();
                break;
            case R.id.log_out:
                logout();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.setVisibility(View.GONE);
    }

    private void addFriend() {
        new MaterialDialog.Builder(this)
                .title(R.string.add_friend)
                .content(String.format(getString(R.string.add_friend_body), name))
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> presenter.addFriend(userId))
                .cancelable(true)
                .show();
    }

    private void removeFriend() {
        new MaterialDialog.Builder(this)
                .title(R.string.remove_friend)
                .content(String.format(getString(R.string.remove_friend_body), name))
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> presenter.removeFriend(userId))
                .cancelable(true)
                .show();
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .title(R.string.log_out_label)
                .content(R.string.log_out_content)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                    presenter.logout();
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

        if (Math.abs(offset) >= appBar.getHeight() - TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics())) {
            actionBar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    getResources().getDisplayMetrics()));
            toolbar.setBackgroundColor(colorPrimary);
        }
        else {
            actionBar.setElevation(0);
            toolbar.setBackgroundColor(transparent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setPadding(0, (int) Math.floor(ProfileImageBehavior.getStatusBarHeight(this) * percentage), 0, 0);
        }
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleVisible = true;
            }
        }
        else {
            if (isTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTheTitleContainerVisible) {
                startAlphaAnimation(mainTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleContainerVisible = false;
            }

        }
        else {
            if (!isTheTitleContainerVisible) {
                startAlphaAnimation(mainTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleContainerVisible = true;
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
