package com.snaptiongame.app.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.friends.FriendsFragment;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.settings.PreferencesActivity;
import com.snaptiongame.app.presentation.view.wall.TabbedWallFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * @author Tyler Wong
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    ImageView mCoverPhoto;
    CircleImageView mProfilePicture;
    TextView mNameView;
    TextView mEmailView;

    private AuthenticationManager mAuthManager;
    private Fragment mCurrentFragment;
    private String fragTag;

    private static final int BLUR_RADIUS = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthManager = AuthenticationManager.getInstance();
        mAuthManager.registerCallback(this::setHeader);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        View headerView = mNavigationView.getHeaderView(0);
        mCoverPhoto = ButterKnife.findById(headerView, R.id.cover_photo);
        mProfilePicture = ButterKnife.findById(headerView, R.id.profile_image);
        mNameView = ButterKnife.findById(headerView, R.id.username);
        mEmailView = ButterKnife.findById(headerView, R.id.email);

        headerView.setOnClickListener(view -> {
            if (mAuthManager.isLoggedIn()) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra(User.ID, mAuthManager.getSnaptionUserId());
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this, mProfilePicture, getString(R.string.shared_transition));
                startActivity(profileIntent, transitionActivityOptions.toBundle());
            }
            else {
                goToLogin();
            }
        });

        mCurrentFragment = new TabbedWallFragment();
        fragTag = TabbedWallFragment.TAG;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, mCurrentFragment).commit();
        mNavigationView.getMenu().getItem(0).setChecked(true);

        mNavigationView.setNavigationItemSelectedListener(this);

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
        actionBarDrawerToggle.syncState();
    }

    private void setDefaultHeader() {
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into(mProfilePicture);
        Glide.clear(mCoverPhoto);
        mNameView.setText(getString(R.string.welcome_message));
        mEmailView.setText("");
    }

    private void setUserHeader() {
        String profileImageUrl = mAuthManager.getProfileImageUrl();
        String name = mAuthManager.getSnaptionUsername();
        String email = mAuthManager.getEmail();

        Glide.with(this)
                .load(profileImageUrl)
                .into(mProfilePicture);
        Glide.with(this)
                .load(profileImageUrl)
                .bitmapTransform(
                        new CenterCrop(this),
                        new BlurTransformation(this, BLUR_RADIUS),
                        new ColorFilterTransformation(this, R.color.colorPrimary))
                .into(mCoverPhoto);
        mNameView.setText(name);
        mEmailView.setText(email);
    }

    private void setHeader() {
        if (mAuthManager.isLoggedIn()) {
            setUserHeader();
        }
        else {
            setDefaultHeader();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthManager.connectGoogleApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthManager.disconnectGoogleApi();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.wall:
                mCurrentFragment = TabbedWallFragment.getInstance();
                fragTag = TabbedWallFragment.TAG;
                break;

            case R.id.friends:
                mCurrentFragment = FriendsFragment.getInstance();
                fragTag = FriendsFragment.TAG;
                break;

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                break;

            case R.id.log_out:
                if (mAuthManager.isLoggedIn()) {
                    mAuthManager.logout();
                    goToLogin();
                }
                break;

            default:
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, mCurrentFragment).commit();

        return true;
    }

    @Override
    public void onBackPressed() {
        // Don't allow back button in MainActivity
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
        if (!mAuthManager.isLoggedIn()) {
            goToLogin();
        }
        else {
            if (fragTag.equals(TabbedWallFragment.TAG)) {
                goToCreateGame();
            }
            else if (fragTag.equals(FriendsFragment.TAG)) {
                ((FriendsFragment) mCurrentFragment).inviteFriends();
            }
        }
    }

    /**
     * This method creates and fires a new intent to switch to
     * a CreateGame activity.
     */
    private void goToCreateGame() {
        Intent createGameIntent = new Intent(this, CreateGameActivity.class);
        startActivity(createGameIntent);
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
