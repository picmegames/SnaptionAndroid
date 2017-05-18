package com.snaptiongame.app.presentation.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.activityfeed.ActivityFeedFragment;
import com.snaptiongame.app.presentation.view.behaviors.FABScrollBehavior;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.friends.FriendSearchActivity;
import com.snaptiongame.app.presentation.view.friends.FriendsFragment;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.settings.PreferencesActivity;
import com.snaptiongame.app.presentation.view.utils.ShowcaseUtils;
import com.snaptiongame.app.presentation.view.wall.WallContract;
import com.snaptiongame.app.presentation.view.wall.WallFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * @author Tyler Wong
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    ImageView mCoverPhoto;
    CircleImageView mProfilePicture;
    TextView mNameView;
    TextView mEmailView;
    ActionBar mActionBar;
    NachoTextView mFilterTextView;

    private AuthManager mAuthManager;
    private Fragment mCurrentFragment;
    private MaterialDialog mFilterDialog;
    private MaterialDialog mFeedbackDialog;
    private WebView mWebView;
    private Menu mMenu;
    private String fragTag;
    private int mUserId;
    private int rightMargin;
    private int bottomMargin;
    private boolean mIsList = false;
    private boolean mLastLoggedInState = false;
    private boolean mComingFromGameActivity = false;

    private static final String TEXT_TYPE = "text/plain";
    private static final int BLUR_RADIUS = 40;
    private static final int DEFAULT_MARGIN = 16;
    private static final int BOTTOM_MARGIN = 72;
    private static final int WALL_RESULT_CODE = 7777;
    private static final int FRIEND_RESULT_CODE = 1414;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuthManager = AuthManager.getInstance();

        mUserId = AuthManager.getUserId();

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);

        View headerView = mNavigationView.getHeaderView(0);
        mCoverPhoto = ButterKnife.findById(headerView, R.id.cover_photo);
        mProfilePicture = ButterKnife.findById(headerView, R.id.profile_image);
        mNameView = ButterKnife.findById(headerView, R.id.username);
        mEmailView = ButterKnife.findById(headerView, R.id.email);

        headerView.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra(User.ID, AuthManager.getUserId());
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this, mProfilePicture, ViewCompat.getTransitionName(mProfilePicture));
                startActivity(profileIntent, transitionActivityOptions.toBundle());
            }
            else {
                goToLogin();
            }
        });

        setupWall();
        initializeWallFragments();
        ShowcaseUtils.showShowcase(this, mFab,
                R.string.welcome_message, R.string.wall_showcase_content);

        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        };

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(false);
        actionBarDrawerToggle.syncState();

        rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MARGIN,
                getResources().getDisplayMetrics());
    }

    private void setDefaultHeader() {
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .dontAnimate()
                .into(mProfilePicture);
        Glide.clear(mCoverPhoto);
        mNameView.setText(R.string.welcome_message);
        mEmailView.setText(R.string.sub_welcome_message);
    }

    private void initializeWallFragments() {
        if (!AuthManager.isLoggedIn()) {
            mCurrentFragment = WallFragment.getInstance(mUserId, WallContract.DISCOVER, mIsList);
            mBottomNavigationView.getMenu().findItem(R.id.discover).setChecked(true);
            mActionBar.setTitle(R.string.discover);
            setAppStatusBarColors(R.color.colorDiscover, R.color.colorDiscoverDark);
        }
        else {
            mCurrentFragment = WallFragment.getInstance(mUserId, WallContract.MY_WALL, mIsList);
            mBottomNavigationView.getMenu().findItem(R.id.my_wall).setChecked(true);
            mActionBar.setTitle(R.string.my_wall);
            setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        }

        fragTag = WallFragment.TAG;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, mCurrentFragment)
                .commit();
    }

    private void setupWall() {
        if (mMenu != null) {
            mMenu.findItem(R.id.filter).setVisible(true);
            mMenu.findItem(R.id.layout).setVisible(true);
            mMenu.findItem(R.id.search).setVisible(false);
            mMenu.findItem(R.id.share).setVisible(false);
        }

        mNavigationView.getMenu().findItem(R.id.wall).setChecked(true);
        mBottomNavigationView.setVisibility(View.VISIBLE);
        resetFabPosition(true);

        int initItem = mBottomNavigationView.getSelectedItemId();

        if (!AuthManager.isLoggedIn()) {
            mNavigationView.getMenu().findItem(R.id.log_out).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.friends).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.activity_feed).setVisible(false);
            mBottomNavigationView.getMenu().removeItem(R.id.my_wall);
            if (initItem == R.id.my_wall) {
                mBottomNavigationView.setSelectedItemId(R.id.discover);
            }
            else {
                mBottomNavigationView.setSelectedItemId(initItem);
            }
        }
        else {
            mNavigationView.getMenu().findItem(R.id.log_out).setVisible(true);
            mNavigationView.getMenu().findItem(R.id.friends).setVisible(true);            mNavigationView.getMenu().findItem(R.id.activity_feed).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.activity_feed).setVisible(true);
            if (mBottomNavigationView.getMenu().findItem(R.id.my_wall) == null) {
                mBottomNavigationView.getMenu()
                        .add(0, R.id.my_wall, Menu.FIRST, getString(R.string.my_wall))
                        .setIcon(R.drawable.ic_account_circle_white_24dp);
            }
            mBottomNavigationView.setSelectedItemId(initItem);
        }
    }

    private void setUserHeader() {
        String profileImageUrl = AuthManager.getProfileImageUrl();
        String name = AuthManager.getUsername();
        String email = AuthManager.getEmail();

        Glide.with(this)
                .load(profileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .dontAnimate()
                .into(mProfilePicture);

        Glide.with(this)
                .load(profileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .bitmapTransform(
                        new CenterCrop(this),
                        new BlurTransformation(this, BLUR_RADIUS),
                        new ColorFilterTransformation(this, R.color.colorPrimary))
                .into(mCoverPhoto);

        mNameView.setText(name);
        mEmailView.setText(email);
    }

    private void setHeader() {
        if (AuthManager.isLoggedIn()) {
            setUserHeader();
        }
        else {
            setDefaultHeader();
        }
    }

    public void setComingFromGameActivity(boolean comingFromGameActivity) {
        this.mComingFromGameActivity = comingFromGameActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader();

        if (mLastLoggedInState != AuthManager.isLoggedIn()) {
            setupWall();
            mLastLoggedInState = AuthManager.isLoggedIn();

            if (!mComingFromGameActivity) {
                initializeWallFragments();
                mComingFromGameActivity = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        mMenu.findItem(R.id.search).setVisible(false);
        mMenu.findItem(R.id.share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent searchIntent = new Intent(this, FriendSearchActivity.class);
                View searchMenuView = mToolbar.findViewById(R.id.search);
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        searchMenuView, getString(R.string.transition_search_back)).toBundle();
                startActivityForResult(searchIntent, FRIEND_RESULT_CODE, options);
                break;
            case R.id.share:
                sendInviteIntent();
                break;
            case R.id.filter:
                showFilterDialog();
                break;
            case R.id.layout:
                mIsList = !mIsList;
                ((WallFragment) mCurrentFragment).switchLayout(mIsList);

                if (mIsList) {
                    mMenu.findItem(R.id.layout).setIcon(R.drawable.ic_dashboard_white_24dp);
                }
                else {
                    mMenu.findItem(R.id.layout).setIcon(R.drawable.ic_view_stream_white_24dp);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        if (mFilterDialog == null) {
            mFilterTextView = new NachoTextView(this);
            mFilterTextView.setChipHeight(R.dimen.chip_height);
            mFilterTextView.setChipSpacing(R.dimen.chip_spacing);
            mFilterTextView.setChipTextSize(R.dimen.chip_text_size);
            mFilterTextView.setChipVerticalSpacing(R.dimen.chip_vertical_spacing);
            mFilterTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            mFilterTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            mFilterTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            mFilterTextView.enableEditChipOnTouch(false, true);

            mFilterDialog = new MaterialDialog.Builder(this)
                    .customView(mFilterTextView, false)
                    .title(R.string.filter_games_title)
                    .positiveText(R.string.filter)
                    .negativeText(R.string.clear)
                    .cancelListener(dialog -> {
                        if (fragTag.equals(WallFragment.TAG) && !((WallFragment) mCurrentFragment).hasTags()) {
                            clearFilterView();
                        }
                    })
                    .onNegative((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) ->
                        clearFilterView()
                    )
                    .onAny((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        mFilterTextView.chipifyAllUnterminatedTokens();
                        filter(mFilterTextView.getChipValues());
                    })
                    .cancelable(true)
                    .show();
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mFilterTextView.getLayoutParams();
            layoutParams.setMargins(rightMargin, rightMargin, rightMargin, rightMargin);
        }
        else {
            mFilterDialog.show();
        }
    }

    private void filter(List<String> tags) {
        if (fragTag.equals(WallFragment.TAG)) {
            ((WallFragment) mCurrentFragment).filterGames(tags);
        }
    }

    private void clearFilterView() {
        if (mFilterTextView != null) {
            mFilterTextView.setText("");
        }
    }

    private void refreshWall() {
        if (fragTag.equals(WallFragment.TAG)) {
            ((WallFragment) mCurrentFragment).refreshWall();
        }
    }

    private void refreshFriends() {
        if (fragTag.equals(FriendsFragment.TAG)) {
            ((FriendsFragment) mCurrentFragment).refreshFriends();
        }
    }

    private void sendInviteIntent() {
        String smsBody = getString(R.string.invite_message) + getString(R.string.store_url);
        Intent inviteIntent = new Intent(Intent.ACTION_SEND);
        inviteIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        inviteIntent.setType(TEXT_TYPE);
        Intent chooser = Intent.createChooser(inviteIntent, getString(R.string.invite_friend_via));
        startActivity(chooser);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        switch (item.getItemId()) {
            case R.id.wall:
                setupWall();

            case R.id.my_wall:
                if (AuthManager.isLoggedIn()) {
                    mCurrentFragment = WallFragment.getInstance(mUserId, WallContract.MY_WALL, mIsList);
                    fragTag = WallFragment.TAG;
                    mBottomNavigationView.getMenu().findItem(R.id.my_wall).setChecked(true);
                    mActionBar.setTitle(R.string.my_wall);
                    setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                    clearFilterView();
                    break;
                }

            case R.id.discover:
                mCurrentFragment = WallFragment.getInstance(mUserId, WallContract.DISCOVER, mIsList);
                fragTag = WallFragment.TAG;
                mBottomNavigationView.getMenu().findItem(R.id.discover).setChecked(true);
                mActionBar.setTitle(R.string.discover);
                setAppStatusBarColors(R.color.colorDiscover, R.color.colorDiscoverDark);
                clearFilterView();
                break;

            case R.id.popular:
                mCurrentFragment = WallFragment.getInstance(mUserId, WallContract.POPULAR, mIsList);
                fragTag = WallFragment.TAG;
                mBottomNavigationView.getMenu().findItem(R.id.popular).setChecked(true);
                mActionBar.setTitle(R.string.popular);
                setAppStatusBarColors(R.color.colorPopular, R.color.colorPopularDark);
                clearFilterView();
                break;

            case R.id.friends:
                mCurrentFragment = FriendsFragment.getInstance();
                fragTag = FriendsFragment.TAG;
                mActionBar.setTitle(R.string.friends_label);
                mBottomNavigationView.setVisibility(View.GONE);
                resetFabPosition(false);
                setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                mMenu.findItem(R.id.filter).setVisible(false);
                mMenu.findItem(R.id.layout).setVisible(false);
                mMenu.findItem(R.id.search).setVisible(true);
                mMenu.findItem(R.id.share).setVisible(true);
                ShowcaseUtils.showShowcase(this, mToolbar.findViewById(R.id.search),
                        R.string.add_a_friend, R.string.friends_showcase_content);
                mFab.setVisibility(View.GONE);
                break;

            case R.id.activity_feed:
                mCurrentFragment = ActivityFeedFragment.getInstance();
                fragTag = ActivityFeedFragment.TAG;
                mActionBar.setTitle(R.string.activity_feed);
                mBottomNavigationView.setVisibility(View.GONE);
                resetFabPosition(false);
                setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                mMenu.findItem(R.id.filter).setVisible(false);
                mMenu.findItem(R.id.layout).setVisible(false);
                mMenu.findItem(R.id.search).setVisible(false);
                mMenu.findItem(R.id.share).setVisible(false);
                mFab.setVisibility(View.GONE);

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                break;

            case R.id.log_out:
                if (AuthManager.isLoggedIn()) {
                    logout();
                }
                break;

            case R.id.feedback:
                mWebView.reload();
                mWebView.loadUrl(getString(R.string.feedback_url));
                if (mFeedbackDialog == null) {
                    mFeedbackDialog = new MaterialDialog.Builder(this)
                            .title(R.string.give_feedback)
                            .customView(mWebView, false)
                            .positiveText(getString(R.string.close))
                            .show();
                }
                else {
                    mFeedbackDialog.show();
                }
                break;

            default:
                break;
        }

        transaction.replace(R.id.frame, mCurrentFragment).commit();

        return true;
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .title(R.string.log_out_label)
                .content(R.string.log_out_content)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                    mAuthManager.logout();
                    goToLogin();
                })
                .show();
    }

    private void setAppStatusBarColors(int colorResource, int colorResourceDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, colorResource);
            int colorDark = ContextCompat.getColor(this, colorResourceDark);
            getWindow().setStatusBarColor(colorDark);
            mActionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    private void resetFabPosition(boolean isWall) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        CoordinatorLayout.LayoutParams coordinatorParams = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        FABScrollBehavior fabScrollBehavior = new FABScrollBehavior(this, null, isWall);
        mFab.show();

        if (isWall) {
            bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN,
                    getResources().getDisplayMetrics());
            coordinatorParams.setBehavior(fabScrollBehavior);
            layoutParams.setMargins(0, 0, rightMargin, bottomMargin);
        }
        else {
            bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MARGIN,
                    getResources().getDisplayMetrics());
            coordinatorParams.setBehavior(fabScrollBehavior);
            layoutParams.setMargins(0, 0, rightMargin, bottomMargin);
        }
    }

    /**
     * This method is called when a user clicks the
     * floating action button on the wall. If the user is
     * logged in, they will be directed to the create game
     * view. If not, they will be directed to the login
     * view.
     */
    @OnClick(R.id.fab)
    public void createGame() {
        if (!AuthManager.isLoggedIn()) {
            goToLogin();
        }
        else {
            goToCreateGame();
        }
    }

    /**
     * This method creates and fires a new intent to switch to
     * a CreateGame activity.
     */
    private void goToCreateGame() {
        Intent createGameIntent = new Intent(this, CreateGameActivity.class);
        startActivityForResult(createGameIntent, WALL_RESULT_CODE);
    }

    private void goToLogin() {
        int resultCode = 0;
        Intent loginIntent = new Intent(this, LoginActivity.class);

        if (fragTag.equals(WallFragment.TAG)) {
            resultCode = WALL_RESULT_CODE;
        }
        else if (fragTag.equals(FriendsFragment.TAG)) {
            resultCode = FRIEND_RESULT_CODE;
        }

        startActivityForResult(loginIntent, resultCode);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WALL_RESULT_CODE && resultCode == RESULT_OK) {
            refreshWall();
        }
        else if (requestCode == FRIEND_RESULT_CODE && resultCode == RESULT_OK) {
            refreshFriends();
        }
    }
}
