package com.snaptiongame.app.presentation.view.wall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.FacebookShareProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.customviews.DynamicImageView;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GameCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    DynamicImageView mImage;
    @BindView(R.id.top_caption)
    TextView mTopCaption;
    @BindView(R.id.captioner_image)
    CircleImageView mCaptionerImage;
    @BindView(R.id.captioner_name)
    TextView mCaptionerName;
    @BindView(R.id.upvote)
    ImageView mUpvoteButton;
    @BindView(R.id.flag)
    ImageView mFlagIcon;
    @BindView(R.id.game_status)
    TextView mGameStatus;

    public Context mContext;
    public PopupMenu mMenu;
    public View mView;

    public int mGameId;
    public int mPickerId;
    public String mImageUrl;

    public boolean isUpvoted = false;
    public boolean isFlagged = false;

    public GameCardViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
        ButterKnife.bind(this, itemView);

        mMenu = new PopupMenu(mContext, itemView);
        mMenu.getMenuInflater().inflate(R.menu.game_menu, mMenu.getMenu());
        mMenu.getMenu().findItem(R.id.invite_friend_to_game).setVisible(false);
        mMenu.getMenu().findItem(R.id.upvote).setVisible(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImage.setClipToOutline(true);
        }

        mUpvoteButton.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                setBeenUpvoted();
            }
            else {
                goToLogin();
            }
        });

        itemView.setOnLongClickListener(view -> {
            mMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.create_game:
                        startCreateGame();
                        break;
                    case R.id.share:
                        FacebookShareProvider.shareToFacebook((AppCompatActivity) mContext, mImage);
                        break;
                    case R.id.flag:
                    case R.id.unflag:
                        if (AuthManager.isLoggedIn()) {
                            setBeenFlagged();
                        }
                        else {
                            goToLogin();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            });

            mMenu.show();
            return true;
        });

        itemView.setOnClickListener(view -> {
            Intent gameIntent = new Intent(mContext, GameActivity.class);
            gameIntent.putExtra(Game.ID, mGameId);
            gameIntent.putExtra(Game.PICKER_ID, mPickerId);
            gameIntent.putExtra(Game.IMAGE_URL, mImageUrl);
            gameIntent.putExtra(Game.BEEN_UPVOTED, isUpvoted);
            gameIntent.putExtra(Game.BEEN_FLAGGED, isFlagged);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                            mImage, ViewCompat.getTransitionName(mImage));
            mContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
        });
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(loginIntent);
    }

    public void hasBeenUpvotedOrFlagged(boolean beenUpvoted, boolean beenFlagged) {
        isUpvoted = beenUpvoted;
        isFlagged = beenFlagged;

        if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_pink_300_24dp));
        }
        else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_800_24dp));
        }

        if (isFlagged) {
            mMenu.getMenu().findItem(R.id.flag).setVisible(false);
            mMenu.getMenu().findItem(R.id.unflag).setVisible(true);
            mFlagIcon.setVisibility(View.VISIBLE);
        }
        else {
            mMenu.getMenu().findItem(R.id.flag).setVisible(true);
            mMenu.getMenu().findItem(R.id.unflag).setVisible(false);
            mFlagIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setBeenUpvoted() {
        if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_800_24dp));
            isUpvoted = false;
        }
        else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_pink_300_24dp));
            isUpvoted = true;
            Toast.makeText(mContext, mContext.getString(R.string.upvoted), Toast.LENGTH_LONG).show();
        }
        upvoteGame(mGameId, isUpvoted);
    }

    private void setBeenFlagged() {
        if (isFlagged) {
            mFlagIcon.setVisibility(View.INVISIBLE);
            mMenu.getMenu().findItem(R.id.flag).setVisible(true);
            mMenu.getMenu().findItem(R.id.unflag).setVisible(false);
            isFlagged = false;
            flagGame(mGameId, isFlagged);
        }
        else {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.flag_alert_game)
                    .content(R.string.ask_flag_game)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> {
                        mFlagIcon.setVisibility(View.VISIBLE);
                        mMenu.getMenu().findItem(R.id.flag).setVisible(false);
                        mMenu.getMenu().findItem(R.id.unflag).setVisible(true);
                        isFlagged = true;
                        flagGame(mGameId, isFlagged);
                        Toast.makeText(mContext, "Flagged", Toast.LENGTH_SHORT).show();
                    })
                    .cancelable(true)
                    .show();
        }
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(mContext, CreateGameActivity.class);
        createGameIntent.putExtra(Game.IMAGE_URL, mImageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                        mImage, ViewCompat.getTransitionName(mImage));
        mContext.startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void upvoteGame(int gameId, boolean isUpvoted) {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, isUpvoted, GameAction.UPVOTE, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Timber.i("Successfully upvoted game!"),
                        Timber::e
                );
    }

    private void flagGame(int gameId, boolean isFlagged) {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, isFlagged, GameAction.FLAGGED, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Timber.i("Successfully flagged game"),
                        Timber::e
                );
    }
}
