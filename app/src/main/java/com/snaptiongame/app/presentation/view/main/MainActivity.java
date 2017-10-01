package com.snaptiongame.app.presentation.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.activityfeed.ActivityFeedFragment;
import com.snaptiongame.app.presentation.view.behaviors.FABScrollBehavior;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.customviews.FilterView;
import com.snaptiongame.app.presentation.view.friends.FriendSearchActivity;
import com.snaptiongame.app.presentation.view.friends.FriendsFragment;
import com.snaptiongame.app.presentation.view.leaderboards.LeaderboardsFragment;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.settings.PreferencesActivity;
import com.snaptiongame.app.presentation.view.utils.MarketUtils;
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
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ImageView coverPhoto;
    CircleImageView profilePicture;
    TextView nameView;
    TextView emailView;
    ActionBar actionBar;
    FilterView filterView;

    private AuthManager authManager;
    private Fragment currentFragment;
    private MaterialDialog filterDialog;
    private Menu menu;
    private String fragTag;
    private int userId;
    private int rightMargin;
    private float defaultElevation;
    private boolean isList = false;
    private boolean lastLoggedInState = false;
    private boolean comingFromGameActivity = false;

    private static final String TEXT_TYPE = "text/plain";
    private static final int BLUR_RADIUS = 40;
    private static final int DEFAULT_MARGIN = 16;
    private static final int DEFAULT_ELEVATION = 4;
    private static final int BOTTOM_MARGIN = 72;
    public static final int WALL_RESULT_CODE = 7777;
    private static final int FRIEND_RESULT_CODE = 1414;
    private static final int ACTIVITY_FEED_RESULT_CODE = 1122;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        authManager = AuthManager.getInstance();

        userId = AuthManager.getUserId();

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        defaultElevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ELEVATION, getResources().getDisplayMetrics());

        View headerView = navigationView.getHeaderView(0);
        coverPhoto = headerView.findViewById(R.id.cover_photo);
        profilePicture = headerView.findViewById(R.id.profile_image);
        nameView = headerView.findViewById(R.id.username);
        emailView = headerView.findViewById(R.id.email);

        headerView.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra(User.ID, AuthManager.getUserId());
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this, profilePicture, ViewCompat.getTransitionName(profilePicture));
                startActivity(profileIntent, transitionActivityOptions.toBundle());
            }
            else {
                goToLogin();
            }
        });

        setupWall();
        initializeWallFragments();
        ShowcaseUtils.showShowcase(this, fab,
                R.string.welcome_message, R.string.wall_showcase_content);

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

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

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(false);
        actionBarDrawerToggle.syncState();

        rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MARGIN,
                getResources().getDisplayMetrics());
    }

    private void setDefaultHeader() {
        RequestOptions options = new RequestOptions()
                .priority(Priority.IMMEDIATE)
                .dontAnimate();

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(options)
                .into(profilePicture);
        Glide.with(this)
                .clear(coverPhoto);
        nameView.setText(R.string.welcome_message);
        emailView.setText(R.string.sub_welcome_message);
    }

    private void initializeWallFragments() {
        if (!AuthManager.isLoggedIn()) {
            currentFragment = WallFragment.getInstance(userId, WallContract.DISCOVER, isList);
            bottomNavigationView.getMenu().findItem(R.id.discover).setChecked(true);
            actionBar.setTitle(R.string.discover);
            setAppStatusBarColors(R.color.colorDiscover, R.color.colorDiscoverDark);
        }
        else {
            currentFragment = WallFragment.getInstance(userId, WallContract.MY_WALL, isList);
            bottomNavigationView.getMenu().findItem(R.id.my_wall).setChecked(true);
            actionBar.setTitle(R.string.my_wall);
            setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        }

        fragTag = WallFragment.TAG;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, currentFragment)
                .commit();
    }

    private void setupWall() {
        if (menu != null) {
            menu.findItem(R.id.filter).setVisible(true);
            menu.findItem(R.id.layout).setVisible(true);
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.share).setVisible(false);
        }

        navigationView.getMenu().findItem(R.id.wall).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        resetFabPosition(true);

        int initItem = bottomNavigationView.getSelectedItemId();
        boolean isLoggedIn = AuthManager.isLoggedIn();

        navigationView.getMenu().findItem(R.id.friends).setVisible(isLoggedIn);
        //navigationView.getMenu().findItem(R.id.leaderboards).setVisible(isLoggedIn);
        navigationView.getMenu().findItem(R.id.activity).setVisible(isLoggedIn);
        navigationView.getMenu().findItem(R.id.log_out).setVisible(isLoggedIn);

        if (!isLoggedIn) {
            bottomNavigationView.getMenu().removeItem(R.id.my_wall);
            if (initItem == R.id.my_wall) {
                bottomNavigationView.setSelectedItemId(R.id.discover);
            }
            else {
                bottomNavigationView.setSelectedItemId(initItem);
            }
        }
        else {
            if (bottomNavigationView.getMenu().findItem(R.id.my_wall) == null) {
                bottomNavigationView.getMenu()
                        .add(0, R.id.my_wall, Menu.FIRST, getString(R.string.my_wall))
                        .setIcon(R.drawable.ic_account_circle_white_24dp);
            }
            bottomNavigationView.setSelectedItemId(initItem);
        }
    }

    private void setUserHeader() {
        String profileImageUrl = AuthManager.getProfileImageUrl();
        String name = AuthManager.getUsername();
        String email = AuthManager.getEmail();

        RequestOptions options = new RequestOptions()
                .priority(Priority.IMMEDIATE)
                .dontAnimate();

        Glide.with(this)
                .load(profileImageUrl)
                .apply(options)
                .into(profilePicture);

        options = new RequestOptions()
                .priority(Priority.IMMEDIATE)
                .transform(new MultiTransformation<>(new CenterCrop(), new BlurTransformation(BLUR_RADIUS),
                        new ColorFilterTransformation(R.color.colorPrimary)));

        Glide.with(this)
                .load(profileImageUrl)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(coverPhoto);

        nameView.setText(name);
        emailView.setText(email);
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
        this.comingFromGameActivity = comingFromGameActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader();

        if (lastLoggedInState != AuthManager.isLoggedIn()) {
            setupWall();
            lastLoggedInState = AuthManager.isLoggedIn();

            if (!comingFromGameActivity) {
                initializeWallFragments();
                comingFromGameActivity = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        this.menu.findItem(R.id.search).setVisible(false);
        this.menu.findItem(R.id.share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent searchIntent = new Intent(this, FriendSearchActivity.class);
                View searchMenuView = toolbar.findViewById(R.id.search);
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
                isList = !isList;
                ((WallFragment) currentFragment).switchLayout(isList);

                if (isList) {
                    menu.findItem(R.id.layout).setIcon(R.drawable.ic_dashboard_white_24dp);
                }
                else {
                    menu.findItem(R.id.layout).setIcon(R.drawable.ic_view_stream_white_24dp);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        if (filterDialog == null) {
            filterView = new FilterView(this);

            filterDialog = new MaterialDialog.Builder(this)
                    .customView(filterView, true)
                    .title(R.string.filter_wall)
                    .positiveText(R.string.filter)
                    .negativeText(R.string.clear)
                    .cancelListener(dialog -> {
                        if (fragTag.equals(WallFragment.TAG) && !((WallFragment) currentFragment).hasTags()) {
                            filterView.clearFilterView();
                        }
                    })
                    .onNegative((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) ->
                            filterView.clearFilterView()
                    )
                    .onAny((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        filterView.chipifyAllUnterminatedTokens();
                        filter(filterView.getChipValues(), filterView.getStatus());
                    })
                    .cancelable(true)
                    .show();
        }
        else {
            filterDialog.show();
        }
    }

    private void filter(List<String> tags, String status) {
        if (fragTag.equals(WallFragment.TAG)) {
            ((WallFragment) currentFragment).filterGames(tags, status);
        }
    }

    private void clearFilterView() {
        if (filterView != null) {
            filterView.clearFilterView();
        }
    }

    private void refreshWall() {
        if (fragTag.equals(WallFragment.TAG)) {
            ((WallFragment) currentFragment).refreshWall();
        }
    }

    private void refreshFriends() {
        if (fragTag.equals(FriendsFragment.TAG)) {
            ((FriendsFragment) currentFragment).refreshFriends();
        }
    }

    private void refreshActivityFeed() {
        if (fragTag.equals(ActivityFeedFragment.TAG)) {
            ((ActivityFeedFragment) currentFragment).refreshActivityFeed();
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
        drawerLayout.closeDrawers();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        switch (item.getItemId()) {
            case R.id.wall:
                setupWall();
                ViewCompat.setElevation(appBarLayout, defaultElevation);

            case R.id.my_wall:
                if (AuthManager.isLoggedIn()) {
                    currentFragment = WallFragment.getInstance(userId, WallContract.MY_WALL, isList);
                    fragTag = WallFragment.TAG;
                    bottomNavigationView.getMenu().findItem(R.id.my_wall).setChecked(true);
                    actionBar.setTitle(R.string.my_wall);
                    setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                    clearFilterView();
                    break;
                }

            case R.id.discover:
                currentFragment = WallFragment.getInstance(userId, WallContract.DISCOVER, isList);
                fragTag = WallFragment.TAG;
                bottomNavigationView.getMenu().findItem(R.id.discover).setChecked(true);
                actionBar.setTitle(R.string.discover);
                setAppStatusBarColors(R.color.colorDiscover, R.color.colorDiscoverDark);
                clearFilterView();
                break;

            case R.id.popular:
                currentFragment = WallFragment.getInstance(userId, WallContract.POPULAR, isList);
                fragTag = WallFragment.TAG;
                bottomNavigationView.getMenu().findItem(R.id.popular).setChecked(true);
                actionBar.setTitle(R.string.popular);
                setAppStatusBarColors(R.color.colorPopular, R.color.colorPopularDark);
                clearFilterView();
                break;

            case R.id.friends:
                currentFragment = FriendsFragment.getInstance();
                fragTag = FriendsFragment.TAG;
                actionBar.setTitle(R.string.friends_label);
                bottomNavigationView.setVisibility(View.GONE);
                resetFabPosition(false);
                setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                menu.findItem(R.id.filter).setVisible(false);
                menu.findItem(R.id.layout).setVisible(false);
                menu.findItem(R.id.search).setVisible(true);
                menu.findItem(R.id.share).setVisible(true);
                ShowcaseUtils.showShowcase(this, toolbar.findViewById(R.id.search),
                        R.string.add_a_friend, R.string.friends_showcase_content);
                fab.setVisibility(View.GONE);
                ViewCompat.setElevation(appBarLayout, defaultElevation);
                break;

            case R.id.leaderboards:
                currentFragment = LeaderboardsFragment.getInstance();
                fragTag = LeaderboardsFragment.TAG;
                actionBar.setTitle(getString(R.string.leaderboards_label));
                bottomNavigationView.setVisibility(View.GONE);
                resetFabPosition(false);
                setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                menu.findItem(R.id.filter).setVisible(false);
                menu.findItem(R.id.layout).setVisible(false);
                menu.findItem(R.id.search).setVisible(false);
                menu.findItem(R.id.share).setVisible(false);
                fab.setVisibility(View.GONE);
                ViewCompat.setElevation(appBarLayout, 0);
                break;

            case R.id.activity:
                currentFragment = ActivityFeedFragment.getInstance();
                fragTag = ActivityFeedFragment.TAG;
                actionBar.setTitle(R.string.activity);
                bottomNavigationView.setVisibility(View.GONE);
                resetFabPosition(false);
                setAppStatusBarColors(R.color.colorPrimary, R.color.colorPrimaryDark);
                menu.findItem(R.id.filter).setVisible(false);
                menu.findItem(R.id.layout).setVisible(false);
                menu.findItem(R.id.search).setVisible(false);
                menu.findItem(R.id.share).setVisible(false);
                fab.setVisibility(View.GONE);
                ViewCompat.setElevation(appBarLayout, defaultElevation);
                break;

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                break;

            case R.id.log_out:
                if (AuthManager.isLoggedIn()) {
                    logout();
                }
                break;

            case R.id.feedback:
                MarketUtils.goToListing(this);
                break;

            default:
                break;
        }

        transaction.replace(R.id.frame, currentFragment).commit();

        return true;
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .title(R.string.log_out_label)
                .content(R.string.log_out_content)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                    authManager.logout();
                    goToLogin();
                })
                .show();
    }

    private void setAppStatusBarColors(int colorResource, int colorResourceDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, colorResource);
            int colorDark = ContextCompat.getColor(this, colorResourceDark);
            getWindow().setStatusBarColor(colorDark);
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    private void resetFabPosition(boolean isWall) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
        CoordinatorLayout.LayoutParams coordinatorParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FABScrollBehavior fabScrollBehavior = new FABScrollBehavior(this, null, isWall);
        int bottomMargin;

        if (isWall) {
            fab.show();
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
        else if (fragTag.equals(ActivityFeedFragment.TAG)) {
            resultCode = ACTIVITY_FEED_RESULT_CODE;
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
        else if (requestCode == ACTIVITY_FEED_RESULT_CODE && resultCode == RESULT_OK) {
            refreshActivityFeed();
        }
    }
}
