package com.snaptiongame.snaptionapp.presentation.view.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.converters.BranchConverter;
import com.snaptiongame.snaptionapp.data.models.Caption;

import com.snaptiongame.snaptionapp.data.models.GameInvite;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;
import com.snaptiongame.snaptionapp.presentation.view.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GameActivity extends AppCompatActivity implements GameContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.caption_list)
    RecyclerView mCaptionList;
    @BindView(R.id.game_image)
    ImageView mImage;
    @BindView(R.id.picker_image)
    ImageView mPickerImage;
    @BindView(R.id.picker_name)
    TextView mPickerName;
    @BindView(R.id.like)
    ImageView mUpvoteButton;

    private ActionBar mActionBar;
    private Menu mMenu;
    private CaptionAdapter mAdapter;
    private AuthenticationManager mAuthManager;
    private GameContract.Presenter mPresenter;
    private CaptionSelectDialogFragment mCaptionDialogFragment;
    private CaptionSelectDialogFragment mCaptionSetDialogFragment;
    private String mPickerImageUrl;
    private String mPicker;
    private int mGameId;
    private int mPickerId;

    private GameInvite mInvite;


    private boolean isUpvoted = false;
    private boolean isFlagged = false;
    private String mImageUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        mAuthManager = AuthenticationManager.getInstance();

        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession((referringParams, error) -> {
            if (error == null) {
                mInvite = BranchConverter.deserializeGameInvite(referringParams);
                Timber.i("token was " + mInvite.inviteToken + " gameId was " + mInvite.gameId);
            } else {
                Timber.e("Branch errored with " + error.getMessage());
            }
        }, this.getIntent().getData(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCaptionList.setLayoutManager(layoutManager);
        mAdapter = new CaptionAdapter(new ArrayList<>());
        mCaptionList.setAdapter(mAdapter);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(getString(R.string.add_caption));
        }

        mUpvoteButton.setOnClickListener(view -> {
            if (isUpvoted) {
                mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_grey_400_24dp));
                isUpvoted = false;
            }
            else {
                mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_400_24dp));
                isUpvoted = true;
                Toast.makeText(this, "Upvoted!", Toast.LENGTH_SHORT).show();
            }
            mPresenter.upvoteOrFlagGame(new Like(mGameId, isUpvoted, Like.UPVOTE, Like.GAME_ID));
        });


        supportPostponeEnterTransition();


        if (mInvite == null) {
            loadRegularGame();
        } else {
            loadInvitedGame();
        }


        Intent intent = getIntent();
        mImageUrl = intent.getStringExtra(Snaption.PICTURE);

        Glide.with(this)
                .load(mImageUrl)
                .fitCenter()
                .into(mImage);

        mPickerImage.setOnClickListener(this::goToPickerProfile);

        mGameId = intent.getIntExtra(Snaption.ID, 0);
        mPickerId = intent.getIntExtra(Snaption.PICKER_ID, 0);
        mPresenter = new GamePresenter(mGameId, mPickerId, this);
        mRefreshLayout.setOnRefreshListener(mPresenter::loadCaptions);

        mPresenter.subscribe();
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        menu.findItem(R.id.unflag).setVisible(false);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.flag:
                mMenu.findItem(R.id.unflag).setVisible(true);
                item.setVisible(false);
                flagGame();
                break;
            case R.id.unflag:
                mMenu.findItem(R.id.flag).setVisible(true);
                item.setVisible(false);
                flagGame();
                break;
            case R.id.create_game:
                startCreateGame();
                break;
            case R.id.share:
                mPresenter.shareToFacebook(this, mImage);
                break;
            case R.id.invite_friend_to_game:
                generateInviteUrl("banana", mGameId);
                break;
            default:
                break;
        }
        return true;
    }



    private void inviteFriendIntent(String url) {
        String title = "Invite friend via";
        Intent inviteIntent = new Intent();
        inviteIntent.setAction(Intent.ACTION_SEND);
        inviteIntent.putExtra(Intent.EXTRA_TEXT, url);


        inviteIntent.setType("text/plain");

        Intent chooser = Intent.createChooser(inviteIntent, title);
        startActivity(chooser);
    }

    private void flagGame() {
        if (isFlagged) {
            isFlagged = false;
        }
        else {
            isFlagged = true;
            Toast.makeText(this, "Flagged", Toast.LENGTH_SHORT).show();
        }
        mPresenter.upvoteOrFlagGame(new Like(mGameId, isFlagged, Like.FLAGGED, Like.GAME_ID));
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(this, CreateGameActivity.class);
        createGameIntent.putExtra(Snaption.PICTURE, mImageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, mImage, getString(R.string.shared_transition));
        startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void goToPickerProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
        profileIntent.putExtra(User.USERNAME, mPicker);
        profileIntent.putExtra(User.PICTURE, mPickerImageUrl);
        profileIntent.putExtra(User.ID, mPickerId);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, getString(R.string.shared_transition));
        startActivity(profileIntent, transitionActivityOptions.toBundle());
    }

    @Override
    public void setPickerInfo(String profileUrl, String name) {
        mPickerImageUrl = profileUrl;
        mPicker = name;

        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileUrl)
                    .into(mPickerImage);
        }
        else {
            mPickerImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(40)
                    .height(40)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(name.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(name)));
        }
        mPickerName.setText(name);
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void addCaption(Caption caption) {
        mAdapter.addCaption(caption);
    }

    @OnClick(R.id.fab)
    public void showAddCaptionDialog() {
        if (!mAuthManager.isLoggedIn()) {
            goToLogin();
        }
        else {
            mCaptionSetDialogFragment = CaptionSelectDialogFragment.newInstance(
                    CaptionSelectDialogFragment.CaptionDialogToShow.SET_CHOOSER,
                    mGameId, -1);
            mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
        }
    }

    public void loadRegularGame() {
        Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .fitCenter()
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
                .into(mImage);
        mGameId = intent.getIntExtra("gameId", 0);
        mPickerId = intent.getIntExtra("pickerId", 0);
    }

    public void loadInvitedGame() {
        mGameId = mInvite.gameId;
    }

    public void displayCaptionChoosingDialog(int setChosen) {
        mCaptionSetDialogFragment.dismiss();
        mCaptionDialogFragment = CaptionSelectDialogFragment.newInstance(
                CaptionSelectDialogFragment.CaptionDialogToShow.CAPTION_CHOOSER,
                mGameId, setChosen);
        mCaptionDialogFragment.show(getFragmentManager(), "dialog");

    }

    public void negativeButtonClicked(CaptionSelectDialogFragment.CaptionDialogToShow whichDialog) {

        if (mCaptionDialogFragment != null)
            mCaptionDialogFragment.dismiss();


        if (whichDialog == CaptionSelectDialogFragment.CaptionDialogToShow.SET_CHOOSER) {
            if (mCaptionSetDialogFragment != null)
                mCaptionSetDialogFragment.dismiss();
        }
        else {

            mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
        }

    }

    private void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void showCaptions(List<Caption> captions) {
        mAdapter.setCaptions(captions);
        mRefreshLayout.setRefreshing(false);
    }
  
    public void generateInviteUrl(String inviteToken, int gameId) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(UUID.randomUUID().toString())
                // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                .setTitle("Join Snaption")
                .setContentDescription("Some description")
                .setContentImageUrl("http://static1.squarespace.com/static/55a5836fe4b0b0843a0e2862/t/571fefa0f8baf30a23c535dd/1473092005381/")
                // You use this to specify whether this content can be discovered publicly - default is public
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                // Here is where you can add custom keys/values to the deep link data
                .addContentMetadata("inviteToken", inviteToken)
                .addContentMetadata("gameId", Integer.toString(gameId));
        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("invite")
                .addControlParameter("$android_url", "https://play.google.com/apps/testing/com.snaptiongame.snaptionapp");
        branchUniversalObject.generateShortUrl(this, linkProperties, (String url, BranchError error) -> {
            if (error == null) {
                Timber.i("got my Branch link to share: " + url);
                inviteFriendIntent(url);
            } else {
                Timber.e("Branch " + error);
            }
        });
    }
}
