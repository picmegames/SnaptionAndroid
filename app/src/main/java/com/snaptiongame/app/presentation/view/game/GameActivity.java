package com.snaptiongame.app.presentation.view.game;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonParser;
import com.hootsuite.nachos.NachoTextView;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.converters.BranchConverter;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.GameInvite;
import com.snaptiongame.app.data.models.Tag;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.services.notifications.NotificationService;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.customviews.FourThreeImageView;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.friends.FriendsAdapter;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;
import com.snaptiongame.app.presentation.view.login.LoginActivity;
import com.snaptiongame.app.presentation.view.photo.ImmersiveActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;
import com.snaptiongame.app.presentation.view.utils.AnimUtils;
import com.snaptiongame.app.presentation.view.utils.ColorUtils;
import com.snaptiongame.app.presentation.view.utils.GlideUtils;
import com.snaptiongame.app.presentation.view.utils.ShowcaseUtils;
import com.snaptiongame.app.presentation.view.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

import static com.snaptiongame.app.SnaptionApplication.getContext;

/**
 * @author Tyler Wong
 */

public class GameActivity extends AppCompatActivity implements GameContract.View,
        CaptionContract.CaptionSetClickListener, CaptionContract.CaptionClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.caption_list)
    RecyclerView captionList;
    @BindView(R.id.game_image)
    FourThreeImageView image;
    @BindView(R.id.picker_image)
    ImageView pickerImage;
    @BindView(R.id.picker_name)
    TextView pickerName;
    @BindView(R.id.layout)
    CoordinatorLayout layout;
    @BindView(R.id.caption_view_switcher)
    ViewSwitcher captionViewSwitcher;
    @BindView(R.id.switch_title_picker)
    ViewSwitcher outerTitleViewSwitcher;
    @BindView(R.id.switch_create_caption)
    FrameLayout switchCreateCaptionView;
    @BindView(R.id.switch_caption_titles)
    ViewSwitcher switchCaptionTitles;
    @BindView(R.id.fitb_entry)
    TextInputEditText fitBEditTextField;
    @BindView(R.id.fitb_entry_layout)
    TextInputLayout fitBEditTextLayout;
    @BindView(R.id.fab)
    FloatingActionButton addCaptionFab;
    @BindView(R.id.fitb_cancel_button)
    ImageView fitBCancelButton;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    @BindView(R.id.caption_chooser_title)
    TextView captionChooserTitle;
    @BindView(R.id.switch_fitb_entry)
    LinearLayout switchFitBEntry;
    @BindView(R.id.caption_card_holder)
    RecyclerView captionView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ActionBar actionBar;
    private Menu menu;
    private CaptionAdapter adapter;
    private InsetDividerDecoration decoration;
    private GameContract.Presenter presenter;
    private CaptionSetAdapter captionSetAdapter;
    private int currentCaption;
    private TextWatcher textWatcher;
    private MaterialDialog tagsDialog;
    private NachoTextView tagsView;
    private MaterialDialog privateGameDialog;
    private FriendsAdapter friendsAdapter;
    private InfiniteRecyclerViewScrollListener scrollListener;
    private FITBCaptionAdapter fitBAdapter;

    private enum CaptionState {
        List, Random, Sets, Typed, Typed_Empty
    }

    private CaptionState currentCaptionState;
    private CaptionState previousCaptionState;

    /**
     * Member variable to reference the game owner's image
     */
    private String pickerImageUrl;

    /**
     * Member variable to reference the game owner's name
     */
    private String picker;

    /**
     * Member variable to reference the game's id so that additional game information like
     * comments can be accessed
     */
    private int gameId;

    /**
     * Member variable to reference the game owner's id so that additional user information can
     * be accessed
     */
    private int pickerId;

    /**
     * Member variable to represent an invite to a game. If the user came from a branch link then
     * the invite will contain a valid gameId and token to access the game. If not then it will
     * be invalid data and use data from the intent to show the game
     */
    private GameInvite invite;

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
    private String imageUrl;

    private boolean isDark = false;
    private boolean isPublic;
    private float lastRefreshIconRotation = 0.0f;

    private final OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final int REFRESH_ICON = 1;
    private static final int FAB_ICON = 0;
    private static final float NO_ROTATION = 0.0f;
    private static final float FORTY_FIVE_DEGREE_ROTATION = 45.0f;
    private static final float HALF_ROTATION = 180.0f;
    private static final float FULL_ROTATION = 360.0f;
    private static final int LONG_DURATION = 1000;
    private static final int SHORT_ROTATION_DURATION = 300;
    private static final String INVITE_CHANNEL = "GameInvite";
    private static final String INVITE = "invite";
    private static final String TEXT_PLAIN = "text/plain";
    private static final int AVATAR_SIZE = 40;
    private static final float SCRIM_ADJUSTMENT = 0.075f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        presenter = new GamePresenter(this);

        Intent intent = getIntent();

        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession((referringParams, error) -> {
            // CALLED when the async initSession returns, won't error if no branch data is found
            if (error == null) {
                Timber.i(referringParams.toString());
                invite = BranchConverter.Companion.deserializeGameInvite(
                        new JsonParser().parse(referringParams.toString()));
                // IF branch returns a null or invalid invite then display the intent information
                if (invite == null || invite.getGameId() == 0) {
                    if (intent.hasExtra(NotificationService.FROM_NOTIFICATION)) {
                        invite = new GameInvite("", intent.getIntExtra(Game.ID, 0));
                        presenter.loadGame(invite.getGameId());
                    }
                    else {
                        showGame(intent.getStringExtra(Game.IMAGE_URL), intent.getIntExtra(Game.ID, 0),
                                intent.getIntExtra(Game.CREATOR_ID, 0), intent.getStringExtra(Game.CREATOR_NAME),
                                intent.getStringExtra(Game.CREATOR_IMAGE), intent.getBooleanExtra(Game.BEEN_UPVOTED, false),
                                intent.getBooleanExtra(Game.BEEN_FLAGGED, false), intent.getBooleanExtra(Game.IS_CLOSED, false),
                                intent.getBooleanExtra(Game.IS_PUBLIC, false));
                    }
                }
                // ELSE display information from the game invite
                else {
                    AuthManager.saveToken(invite.getInviteToken());
                    presenter.loadGame(invite.getGameId());
                }
            }
            else {
                Timber.e(error.getMessage());
            }
        }, intent.getData(), this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        image.setOnClickListener(view -> {
            Intent immersiveIntent = new Intent(this, ImmersiveActivity.class);
            immersiveIntent.putExtra(ImmersiveActivity.IMAGE_URL, imageUrl);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, image, ViewCompat.getTransitionName(image));
            startActivity(immersiveIntent, transitionActivityOptions.toBundle());
        });

        fitBAdapter = new FITBCaptionAdapter(new ArrayList<>(), this);
        currentCaptionState = CaptionState.List;
    }

    private void upvoteGame() {
        if (isUpvoted) {
            if (!isDark) {
                menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_grey_800_24dp);
                // menu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_border_grey_800_24dp);
            }
            else {
                menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_white_24dp);
                // menu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_border_white_24dp);
            }
            isUpvoted = false;
        }
        else {
//            if (!isDark) {
//                menu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_grey_800_24dp);
//            }
//            else {
//                menu.findItem(R.id.upvote).setIcon(R.drawable.ic_favorite_white_24dp);
//            }
            menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_pink_300_24dp);
            isUpvoted = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        this.menu = menu;

        if (isUpvoted) {
            this.menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_pink_300_24dp);
        }
        else {
            this.menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_white_24dp);
        }

        showPrivateIcon(isPublic);

        return true;
    }

    private void showPrivateIcon(boolean isPublic) {
        if (isPublic) {
            menu.findItem(R.id.is_private).setVisible(false);
        }
        else {
            menu.findItem(R.id.is_private).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.flag:
                if (AuthManager.isLoggedIn()) {
                    flagDialog();
                }
                else {
                    goToLogin();
                }
                break;
            case R.id.tags:
                showTagsDialog();
                break;
            case R.id.is_private:
                showPrivateDialog();
                break;
            case R.id.create_game:
                if (AuthManager.isLoggedIn()) {
                    startCreateGame();
                }
                else {
                    goToLogin();
                }
                break;
            case R.id.share:
                presenter.shareToFacebook(this, image);
                break;
            case R.id.invite_friend_to_game:
                if (AuthManager.isLoggedIn()) {
                    presenter.getBranchToken(gameId);
                }
                else {
                    goToLogin();
                }
                break;
            case R.id.upvote:
                if (AuthManager.isLoggedIn()) {
                    presenter.upvoteOrFlagGame(new GameAction(gameId, !isUpvoted, GameAction.UPVOTE,
                            GameAction.GAME_ID));
                    upvoteGame();
                }
                else {
                    goToLogin();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void showPrivateDialog() {
        if (privateGameDialog == null) {
            friendsAdapter = new FriendsAdapter(new ArrayList<>());
            presenter.loadInvitedUsers(gameId);

            privateGameDialog = new MaterialDialog.Builder(this)
                    .title(R.string.participants)
                    .adapter(friendsAdapter, new LinearLayoutManager(this))
                    .positiveText(R.string.close)
                    .cancelable(true)
                    .show();
        }
        else {
            privateGameDialog.show();
        }
    }

    private void showTagsDialog() {
        if (tagsDialog == null) {
            tagsView = new NachoTextView(this);
            tagsView.setBackground(null);
            tagsView.setChipHeight(R.dimen.chip_height);
            tagsView.setChipSpacing(R.dimen.chip_spacing);
            tagsView.setChipTextSize(R.dimen.chip_text_size);
            tagsView.setEnabled(false);
            presenter.loadTags(gameId);

            tagsDialog = new MaterialDialog.Builder(this)
                    .title(R.string.tags_label)
                    .customView(tagsView, true)
                    .positiveText(R.string.close)
                    .cancelable(true)
                    .show();
        }
        else {
            tagsDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        captionList.removeItemDecoration(decoration);
    }

    private void inviteFriendIntent(String url) {
        String title = getString(R.string.invite_friend_via);
        Intent inviteIntent = new Intent();
        inviteIntent.setAction(Intent.ACTION_SEND);
        inviteIntent.putExtra(Intent.EXTRA_TEXT, url);
        inviteIntent.setType(TEXT_PLAIN);

        Intent chooser = Intent.createChooser(inviteIntent, title);
        startActivity(chooser);
    }

    private void flagDialog() {
        if (isFlagged) {
            presenter.upvoteOrFlagGame(new GameAction(gameId, !isFlagged, GameAction.FLAGGED,
                    GameAction.GAME_ID));
        }
        else {
            new MaterialDialog.Builder(this)
                    .title(R.string.flag_alert_game)
                    .content(R.string.ask_flag_game)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        presenter.upvoteOrFlagGame(new GameAction(gameId, !isFlagged,
                                GameAction.FLAGGED, GameAction.GAME_ID));
                    })
                    .cancelable(true)
                    .show();
        }
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(this, CreateGameActivity.class);
        createGameIntent.putExtra(Game.GAME_ID, gameId);
        createGameIntent.putExtra(Game.IMAGE_URL, imageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, image, ViewCompat.getTransitionName(image));
        startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void goToPickerProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
        profileIntent.putExtra(User.USERNAME, picker);
        profileIntent.putExtra(User.IMAGE_URL, pickerImageUrl);
        profileIntent.putExtra(User.ID, pickerId);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(pickerImage));
        startActivity(profileIntent, transitionActivityOptions.toBundle());
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void showCaptionSubmissionError() {
        Toast.makeText(this, R.string.failed_caption_submission, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fab)
    public void showAddCaptionDialog() {
        boolean successfulCaptionSubmission = false;
        refreshLayout.setRefreshing(false);

        if (!AuthManager.isLoggedIn()) {
            goToLogin();
        }
        else {
            if (currentCaptionState == CaptionState.Typed) {
                successfulCaptionSubmission = confirmAndPrepareCaption();
            }
            // Go back to caption view
            if (!captionViewSwitcher.getCurrentView().equals(refreshLayout) && successfulCaptionSubmission) {
                rotateIcon(FULL_ROTATION, LONG_DURATION, FAB_ICON);
                // Switches between list and fitbs
                captionViewSwitcher.showPrevious();
                // Switches bettween icons and edit text
                outerTitleViewSwitcher.showPrevious();
                switchCaptionTitles.showNext();
            }
            // Shown when a user first enters the caption view
            else if (currentCaptionState == CaptionState.List
                    && captionViewSwitcher.getCurrentView().equals(refreshLayout)) {
                captionChooserTitle.setText(getString(R.string.random_captions));
                rotateIcon(FORTY_FIVE_DEGREE_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
                captionViewSwitcher.showNext();
                outerTitleViewSwitcher.showNext();
                initializeCaptionView();
            }
            // when a user clicks cancel on the fab
            else {
                captionViewSwitcher.showPrevious();
                outerTitleViewSwitcher.showPrevious();
                refreshIcon.setImageResource(R.drawable.ic_refresh_grey_800_24dp);
                rotateIcon(NO_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
                rotateIcon(NO_ROTATION, SHORT_ROTATION_DURATION, REFRESH_ICON);
                lastRefreshIconRotation = NO_ROTATION;

                if (currentCaptionState == CaptionState.Typed_Empty) {
                    switchCaptionTitles.showPrevious();
                }

                currentCaptionState = CaptionState.List;
            }
        }
        hideKeyboard();
    }

    @Override
    public void showPrivateGameDialog(List<Friend> invitedUsers) {
        friendsAdapter.setFriends(invitedUsers);
    }

    @Override
    public void showTags(List<Tag> tags) {
        List<String> tagValues = new ArrayList<>();
        for (Tag tag : tags) {
            tagValues.add(tag.getName());
        }
        tagsView.setText(tagValues);
    }

    private void rotateIcon(float rotation, int duration, int whichIcon) {
        View viewToRotate;

        if (whichIcon == FAB_ICON) {
            viewToRotate = addCaptionFab;
        }
        else {
            viewToRotate = refreshIcon;
        }

        ViewCompat.animate(viewToRotate)
                .rotation(rotation)
                .withLayer()
                .setDuration(duration)
                .setInterpolator(interpolator)
                .start();
    }

    private boolean confirmAndPrepareCaption() {
        String curEntry = fitBEditTextField.getText().toString();
        fitBAdapter.resetCaption();
        fitBEditTextField.setText("");
        fitBEditTextLayout.setHint("");
        adapter.clear();
        presenter.addCaption(fitBAdapter.getCaption(currentCaption).getId(),
                curEntry);
        refreshLayout.setRefreshing(true);
        addCaptionFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.ic_add_white_24dp));
        currentCaptionState = CaptionState.List;
        return true;
    }

    private void initializeCaptionView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        refreshIcon.setVisibility(View.VISIBLE);
        captionView.setLayoutManager(layoutManager);
        captionView.setAdapter(fitBAdapter);
        currentCaptionState = CaptionState.Random;
    }

    @OnClick(R.id.refresh_icon)
    public void refreshCaptions() {
        captionChooserTitle.setText(getString(R.string.random_captions));
        refreshIcon.setImageResource(R.drawable.ic_refresh_grey_800_24dp);

        if (lastRefreshIconRotation == FULL_ROTATION) {
            rotateIcon(NO_ROTATION, SHORT_ROTATION_DURATION, REFRESH_ICON);
            lastRefreshIconRotation = NO_ROTATION;
        }
        else {
            rotateIcon(FULL_ROTATION, SHORT_ROTATION_DURATION, REFRESH_ICON);
            lastRefreshIconRotation = FULL_ROTATION;
        }

        captionView.setAdapter(fitBAdapter);
        presenter.refreshCaptions();

        if (currentCaptionState == CaptionState.Sets) {
            rotateIcon(NO_ROTATION, SHORT_ROTATION_DURATION, REFRESH_ICON);
            lastRefreshIconRotation = NO_ROTATION;
            currentCaptionState = CaptionState.Random;
        }
    }

    @OnFocusChange(R.id.fitb_entry)
    public void removeFITBUnderScores() {
        String curText = fitBEditTextField.getText().toString();
        if (curText.matches("/[_]/")) {
            fitBEditTextField.setText("");
        }
    }

    @OnClick(R.id.fitb_cancel_button)
    public void resetCaptionChoosing() {
        if (currentCaptionState == CaptionState.Typed) {
            addCaptionFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_add_white_24dp));
            rotateIcon(FORTY_FIVE_DEGREE_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
        }
        hideKeyboard();
        switchCaptionTitles.showNext();
        currentCaptionState = previousCaptionState;
        fitBAdapter.resetCaption();
    }

    @OnClick(R.id.caption_sets)
    public void loadCaptionSets() {
        captionChooserTitle.setText(getString(R.string.caption_sets));
        captionSetAdapter = new CaptionSetAdapter(new ArrayList<>(), this);
        presenter.loadCaptionSets();
        showProgressHideRecyclerView();
        captionView.setAdapter(captionSetAdapter);

        if (currentCaptionState != CaptionState.Sets) {
            refreshIcon.setImageResource(R.drawable.ic_arrow_forward_grey_800_24dp);
            rotateIcon(HALF_ROTATION, SHORT_ROTATION_DURATION, REFRESH_ICON);
        }

        currentCaptionState = CaptionState.Sets;
    }

    @Override
    public void showGame(String image, int gameId, int pickerId, String pickerName, String pickerImage,
                         boolean beenUpvoted, boolean beenFlagged, boolean isClosed, boolean isPublic) {
        imageUrl = image;
        isUpvoted = beenUpvoted;
        isFlagged = beenFlagged;
        pickerImageUrl = pickerImage;
        picker = pickerName;
        this.isPublic = isPublic;

        ViewCompat.setTransitionName(this.image, imageUrl);

        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .priority(Priority.IMMEDIATE);

        Glide.with(this)
                .load(image)
                .apply(options)
                .listener(imageLoadListener)
                .into(this.image);

        this.gameId = gameId;
        this.pickerId = pickerId;

        if (pickerImage != null && !pickerImage.isEmpty()) {

            options = new RequestOptions()
                    .dontAnimate()
                    .placeholder(new ColorDrawable(ContextCompat.getColor(this, R.color.grey_300)));

            Glide.with(this)
                    .load(pickerImage)
                    .apply(options)
                    .into(this.pickerImage);
        }
        else {
            this.pickerImage.setImageDrawable(TextDrawable.builder()
                    .beginConfig()
                    .width(AVATAR_SIZE)
                    .height(AVATAR_SIZE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(pickerName.substring(0, 1),
                            ColorGenerator.MATERIAL.getColor(pickerName)));
        }
        this.pickerName.setText(picker);

        if (isClosed) {
            addCaptionFab.setVisibility(View.GONE);

            if (AuthManager.isLoggedIn() && AuthManager.isClosedGameDialogEnabled()) {
                new MaterialDialog.Builder(this)
                        .title(R.string.game_closed_title)
                        .content(R.string.game_closed_content)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) ->
                                startCreateGame()
                        )
                        .onNegative((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) ->
                                new MaterialDialog.Builder(this)
                                        .title(R.string.create_game_title)
                                        .content(R.string.create_game_content)
                                        .positiveText(R.string.got_it)
                                        .show()
                        )
                        .checkBoxPromptRes(R.string.dont_ask_again, false, (CompoundButton compoundButton, boolean isChecked) ->
                                AuthManager.setIsClosedGameDialogEnabled(!isChecked)
                        )
                        .show();
            }
        }
        else {
            ShowcaseUtils.showShowcase(this, addCaptionFab,
                    R.string.game_showcase_title, R.string.game_showcase_content);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        captionList.setLayoutManager(layoutManager);

        scrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadCaptions(page);
            }
        };

        adapter = new CaptionAdapter(new ArrayList<>(), captionList);
        captionList.setAdapter(adapter);
        captionList.addOnScrollListener(scrollListener);
        decoration = new InsetDividerDecoration(
                CaptionCardViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(this, R.color.divider));
        captionList.addItemDecoration(decoration);

        refreshLayout.setOnRefreshListener(() -> {
            adapter.clear();
            scrollListener.resetState();
            presenter.loadCaptions(1);
        });
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));

        this.pickerImage.setOnClickListener(this::goToPickerProfile);

        presenter.setGameId(this.gameId);
        presenter.subscribe();
        refreshLayout.setRefreshing(true);

        if (menu != null) {
            showPrivateIcon(isPublic);
        }
    }

    private RequestListener imageLoadListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
            final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24, getResources().getDisplayMetrics());

            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip)
                    .generate(palette -> {
                        @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                        }
                        else {
                            isDark = lightness == ColorUtils.IS_DARK;
                        }

                        if (!isDark) {
                            ActionBar actionBar = getSupportActionBar();

                            if (actionBar != null) {
                                actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(
                                        GameActivity.this, R.drawable.ic_arrow_back_grey_800_24dp));

                                final Drawable more = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_more_vert_grey_800_24dp, null);
                                toolbar.setOverflowIcon(more);

                                menu.findItem(R.id.is_private).setIcon(R.drawable.ic_lock_grey_800_24dp);
                                menu.findItem(R.id.tags).setIcon(R.drawable.ic_local_offer_grey_800_24dp);

                                if (isUpvoted) {
                                    menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_pink_300_24dp);
                                }
                                else {
                                    menu.findItem(R.id.upvote).setIcon(R.drawable.ic_arrow_upward_grey_800_24dp);
                                }
                            }
                        }

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            int statusBarColor = getWindow().getStatusBarColor();
                            final Palette.Swatch topColor = ColorUtils.getMostPopulousSwatch(palette);
                            if (topColor != null &&
                                    (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                                statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                        isDark, SCRIM_ADJUSTMENT);
                                image.setBackgroundColor(statusBarColor);
                                // set a light status bar on M+
                                if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ViewUtils.setLightStatusBar(layout);
                                }
                            }

                            if (statusBarColor != getWindow().getStatusBarColor()) {
                                ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                                        getWindow().getStatusBarColor(), statusBarColor);
                                statusBarColorAnim.addUpdateListener(animation ->
                                        getWindow().setStatusBarColor(
                                                (int) animation.getAnimatedValue())
                                );
                                statusBarColorAnim.setDuration(1000L);
                                statusBarColorAnim.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(GameActivity.this));
                                statusBarColorAnim.start();
                            }
                        }
                    });

            Palette.from(bitmap)
                    .clearFilters()
                    .generate(palette -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            image.setForeground(ViewUtils.createRipple(palette, 0.3f, 0.6f,
                                    ContextCompat.getColor(GameActivity.this, R.color.grey_500),
                                    true));
                        }
                    });

            image.setBackground(null);
            return false;
        }
    };

    private void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void showCaptions(List<Caption> captions) {
        adapter.addCaptions(captions);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void resetScrollState() {
        scrollListener.resetState();
    }

    @Override
    public void generateInviteUrl(String inviteToken) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(UUID.randomUUID().toString())
                .setTitle(getString(R.string.join_snaption))
                .setContentDescription(getString(R.string.snaption_description))
                .setContentImageUrl(imageUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(GameInvite.INVITE_TOKEN, inviteToken)
                .addContentMetadata(GameInvite.GAME_ID, Integer.toString(gameId));

        LinkProperties linkProperties = new LinkProperties()
                .setChannel(INVITE_CHANNEL)
                .setFeature(INVITE);

        branchUniversalObject.generateShortUrl(this, linkProperties, (String url, BranchError error) -> {
            if (error == null) {
                inviteFriendIntent(url);
            }
            else {
                Timber.e(error.getMessage());
            }
        });
    }

    @Override
    public void captionClicked(View v, int position, List<String> fitbs) {
        final String beforeBlank = fitbs.get(0);
        final String afterBlank = fitbs.get(2);
        final String placeHolder = "______";

        if (currentCaptionState != CaptionState.Typed && currentCaptionState != CaptionState.Typed_Empty) {
            previousCaptionState = currentCaptionState;
        }

        if (currentCaptionState == CaptionState.Typed) {
            addCaptionFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_add_white_24dp));
            rotateIcon(FORTY_FIVE_DEGREE_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
        }

        if (textWatcher != null) {
            fitBEditTextField.removeTextChangedListener(textWatcher);
        }

        fitBEditTextField.setText("");
        currentCaptionState = CaptionState.Typed_Empty;
        currentCaption = position;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        fitBEditTextLayout.setVisibility(View.VISIBLE);
        fitBEditTextField.setHint(getString(R.string.fitb));
        fitBEditTextLayout.setHint(beforeBlank + placeHolder + afterBlank);
        fitBEditTextField.requestFocus();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fitBEditTextLayout.setHint(beforeBlank + s + afterBlank);

                if (!s.toString().replaceAll("\\s+", "").isEmpty()) {
                    if (currentCaptionState != CaptionState.Typed) {
                        addCaptionFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                                R.drawable.ic_check_white_24dp));
                        rotateIcon(NO_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
                    }

                    currentCaptionState = CaptionState.Typed;
                }
                else {
                    if (currentCaptionState != CaptionState.Typed_Empty) {
                        addCaptionFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                                R.drawable.ic_add_white_24dp));
                        rotateIcon(FORTY_FIVE_DEGREE_ROTATION, SHORT_ROTATION_DURATION, FAB_ICON);
                    }

                    fitBEditTextLayout.setHint(beforeBlank + placeHolder + afterBlank);
                    currentCaptionState = CaptionState.Typed_Empty;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        fitBEditTextField.addTextChangedListener(textWatcher);

        imm.showSoftInput(fitBEditTextField, InputMethodManager.SHOW_IMPLICIT);

        if (switchCaptionTitles.getCurrentView() != switchFitBEntry) {
            switchCaptionTitles.showNext();
        }
    }

    private void showRecyclerViewHideProgress() {
        captionView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressHideRecyclerView() {
        captionView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFitBCaptions(List<FitBCaption> captions) {
        fitBAdapter = new FITBCaptionAdapter(captions, this);
        captionView.setAdapter(fitBAdapter);
        showRecyclerViewHideProgress();
    }

    @Override
    public void showRandomCaptions(List<FitBCaption> captions) {
        fitBAdapter.setCaptions(captions);
        fitBAdapter.notifyDataSetChanged();
        showRecyclerViewHideProgress();
    }

    @Override
    public void onGameUpdated(String type) {
        if (type.equals(GameAction.FLAGGED)) {
            Toast.makeText(this, R.string.flagged, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else if (type.equals(GameAction.UPVOTE) && isUpvoted) {
            Toast.makeText(this, R.string.upvoted, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGameErrored(String type) {
        if (type.equals(GameAction.UPVOTE)) {
            upvoteGame();
            Toast.makeText(this, R.string.upvote_fail, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, R.string.flagged_fail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showCaptionSets(List<CaptionSet> captionSets) {
        captionSetAdapter.setCaptionSets(captionSets);
        showRecyclerViewHideProgress();
    }

    @Override
    public void captionSetClicked(View v, int setId, int position) {
        captionChooserTitle.setText(captionSetAdapter.getSetName(position));
        presenter.loadFitBCaptions(setId);
        showProgressHideRecyclerView();
    }
}
