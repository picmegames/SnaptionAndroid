package com.snaptiongame.app.presentation.view.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.converters.BranchConverter;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.GameInvite;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.photo.ImmersiveActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GameActivity extends AppCompatActivity implements GameContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
    @BindView(R.id.layout)
    CoordinatorLayout mLayout;

    private ActionBar mActionBar;
    private Menu mMenu;
    private CaptionAdapter mAdapter;
    private AuthenticationManager mAuthManager;
    private GameContract.Presenter mPresenter;
    private CaptionSelectDialogFragment mCaptionDialogFragment;
    private CaptionSelectDialogFragment mCaptionSetDialogFragment;

    /**
     * Member variable to reference the game owner's image
     */
    private String mPickerImageUrl;

    /**
     * Member variable to reference the game owner's name
     */
    private String mPicker;

    /**
     * Member variable to reference the game's id so that additional game information like
     * comments can be accessed
     */
    private int mGameId;

    /**
     * Member variable to reference the game owner's id so that additional user information can
     * be accessed
     */
    private int mPickerId;

    /**
     * Member variable to represent an invite to a game. If the user came from a branch link then
     * the invite will contain a valid gameId and token to access the game. If not then it will
     * be invalid data and use data from the intent to show the game
     */
    private GameInvite mInvite;

    /**
     * Reference to whether the game has been upvoted or not, false by default
     */
    private boolean isUpvoted = false;

    /**
     * Reference to whether the game has been flagged or not, false by default
     */
    private boolean isFlagged = false;

    /**
     * Member variable that contains the image's url to be loaded by glide
     */
    private String mImageUrl;

    public static final String JOIN_SNAPTION = "Join Snaption!";
    public static final String SNAPTION_DESCRIPTION = "Compete to create the best caption for a photo by filling in the blank on a caption with the word or phrase of your choice. ";
    public static final String INVITE_CHANNEL = "GameInvite";
    public static final String INVITE = "invite";
    private static final int AVATAR_SIZE = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        mAuthManager = AuthenticationManager.getInstance();

        Intent intent = getIntent();
        ViewCompat.setTransitionName(mImage, intent.getStringExtra(Game.IMAGE_URL));

        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession((referringParams, error) -> {
            // CALLED when the async initSession returns, won't error if no branch data is found
            if (error == null) {
                mInvite = BranchConverter.deserializeGameInvite(referringParams);
                // IF branch returns a null or invalid invite then display the intent information
                if (mInvite == null || mInvite.gameId == 0) {
                    showGame(intent.getStringExtra(Game.IMAGE_URL), intent.getIntExtra(Game.ID, 0),
                            intent.getIntExtra(Game.PICKER_ID, 0), intent.getBooleanExtra(Game.BEEN_UPVOTED, false),
                            intent.getBooleanExtra(Game.BEEN_FLAGGED, false));
                }
                // ELSE display information from the game invite
                else {
                    mAuthManager.saveToken(mInvite.inviteToken);
                    loadInvitedGame();
                }
                Timber.i("token was " + mInvite.inviteToken + " gameId was " + mInvite.gameId);
            }
            else {
                Timber.e("Branch errored with " + error.getMessage());
            }
        }, intent.getData(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCaptionList.setLayoutManager(layoutManager);
        mAdapter = new CaptionAdapter(new ArrayList<>());
        mCaptionList.setAdapter(mAdapter);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("");
        }

        mImage.setOnClickListener(view -> {
            Intent immersiveIntent = new Intent(this, ImmersiveActivity.class);
            immersiveIntent.putExtra(Game.IMAGE_URL, mImageUrl);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, mImage, ViewCompat.getTransitionName(mImage));
            startActivity(immersiveIntent, transitionActivityOptions.toBundle());
        });
    }

    private void upvoteGame() {
        if (isUpvoted) {
            mMenu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_border_white_24dp);
            isUpvoted = false;
        }
        else {
            mMenu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_white_24dp);
            isUpvoted = true;
        }
        mPresenter.upvoteOrFlagGame(new GameAction(mGameId, isUpvoted, GameAction.UPVOTE, GameAction.GAME_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        mMenu = menu;

        if (isFlagged) {
            mMenu.findItem(R.id.unflag).setVisible(true);
            mMenu.findItem(R.id.flag).setVisible(false);
        }
        else {
            mMenu.findItem(R.id.unflag).setVisible(false);
            mMenu.findItem(R.id.flag).setVisible(true);
        }
        if (isUpvoted) {
            mMenu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_white_24dp);
        }
        else {
            mMenu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_border_white_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.flag:
                flagGame();
                break;
            case R.id.unflag:
                flagGame();
                break;
            case R.id.create_game:
                startCreateGame();
                break;
            case R.id.share:
                mPresenter.shareToFacebook(this, mImage);
                break;
            case R.id.invite_friend_to_game:
                mPresenter.getBranchToken(mGameId);
                break;
            case R.id.upvote:
                upvoteGame();
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
            mPresenter.upvoteOrFlagGame(new GameAction(mGameId, isFlagged, GameAction.FLAGGED, GameAction.GAME_ID));
            mMenu.findItem(R.id.unflag).setVisible(false);
            mMenu.findItem(R.id.flag).setVisible(true);
        }
        else {
            new MaterialDialog.Builder(this)
                    .title(R.string.flag_alert_game)
                    .content(R.string.ask_flag_game)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        isFlagged = true;
                        mPresenter.upvoteOrFlagGame(new GameAction(mGameId, isFlagged, GameAction.FLAGGED, GameAction.GAME_ID));
                        mMenu.findItem(R.id.unflag).setVisible(true);
                        mMenu.findItem(R.id.flag).setVisible(false);
                        Toast.makeText(this, "Flagged", Toast.LENGTH_SHORT).show();
                    })
                    .cancelable(true)
                    .show();
        }
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(this, CreateGameActivity.class);
        createGameIntent.putExtra(Game.IMAGE_URL, mImageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, mImage, ViewCompat.getTransitionName(mImage));
        startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void goToPickerProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
        profileIntent.putExtra(User.USERNAME, mPicker);
        profileIntent.putExtra(User.IMAGE_URL, mPickerImageUrl);
        profileIntent.putExtra(User.ID, mPickerId);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(mPickerImage));
        startActivity(profileIntent, transitionActivityOptions.toBundle());
    }

    @Override
    public void setPickerInfo(String profileUrl, String name) {
        mPickerImageUrl = profileUrl;
        mPicker = name;

        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileUrl)
                    .dontAnimate()
                    .into(mPickerImage);
        }
        else {
            mPickerImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(name.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(name)));
        }
        mPickerName.setText(mPicker);
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                    CaptionSelectDialogFragment.CaptionDialogToShow.CAPTION_CHOOSER,
                    mGameId, -1);
            mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
        }
    }

    public void showGame(String image, int id, int pickerId, boolean beenUpvoted, boolean beenFlagged) {
        mImageUrl = image;
        isUpvoted = beenUpvoted;
        isFlagged = beenFlagged;

        supportPostponeEnterTransition();
        Glide.with(this)
                .load(image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mImage);
        mGameId = id;
        mPickerId = pickerId;

        mPresenter = new GamePresenter(id, pickerId, this);
        mRefreshLayout.setOnRefreshListener(mPresenter::loadCaptions);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorDiscover),
                ContextCompat.getColor(this, R.color.colorPopular)
        );

        mPickerImage.setOnClickListener(this::goToPickerProfile);

        mPresenter.subscribe();
        mRefreshLayout.setRefreshing(true);
    }

    public void loadInvitedGame() {
        GameProvider.getGame(mInvite.gameId, mAuthManager.getInviteToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        game -> showGame(game.imageUrl, game.id, game.pickerId, game.beenUpvoted, game.beenFlagged),
                        Timber::e,
                        () -> Timber.i("Loading game completed successfully.")
                );
    }

    public void displayCaptionChoosingDialog(int setChosen) {
        mCaptionSetDialogFragment.dismiss();
        mCaptionDialogFragment = CaptionSelectDialogFragment.newInstance(
                CaptionSelectDialogFragment.CaptionDialogToShow.CAPTION_CHOOSER,
                mGameId, setChosen);
        mCaptionDialogFragment.show(getFragmentManager(), "dialog");

    }

    public void displaySetChoosingDialog() {
        mCaptionSetDialogFragment.dismiss();
        mCaptionDialogFragment = CaptionSelectDialogFragment.newInstance(
                CaptionSelectDialogFragment.CaptionDialogToShow.SET_CHOOSER,
                mGameId, -1);
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
            if (mCaptionDialogFragment != null)
                mCaptionDialogFragment.dismiss();
            //mCaptionSetDialogFragment.show(getFragmentManager(), "dialog");
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

    @Override
    public void generateInviteUrl(String inviteToken) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(UUID.randomUUID().toString())
                .setTitle(JOIN_SNAPTION)
                .setContentDescription(SNAPTION_DESCRIPTION)
                .setContentImageUrl(mImageUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(GameInvite.INVITE_TOKEN, inviteToken)
                .addContentMetadata(GameInvite.GAME_ID, Integer.toString(mGameId));

        LinkProperties linkProperties = new LinkProperties()
                .setChannel(INVITE_CHANNEL)
                .setFeature(INVITE);

        branchUniversalObject.generateShortUrl(this, linkProperties, (String url, BranchError error) -> {
            if (error == null) {
                Timber.i("got my Branch link to share: " + url);
                inviteFriendIntent(url);
            }
            else {
                Timber.e("Branch " + error);
            }
        });
    }
}
