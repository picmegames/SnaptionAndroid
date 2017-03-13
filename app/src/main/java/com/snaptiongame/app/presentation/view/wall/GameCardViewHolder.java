package com.snaptiongame.app.presentation.view.wall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.FacebookShareProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.app.presentation.view.customviews.DynamicImageView;
import com.snaptiongame.app.presentation.view.game.GameActivity;

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
    ImageView mFlagButton;
    @BindView(R.id.game_status)
    TextView mGameStatus;

    public Context mContext;

    public int mGameId;
    public int mPickerId;
    public String mImageUrl;

    public boolean isUpvoted = false;
    public boolean isFlagged = false;

    public GameCardViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);

        if (Build.VERSION.SDK_INT >= 21) {
            mImage.setClipToOutline(true);
        }

        mUpvoteButton.setOnClickListener(view -> setBeenUpvoted());
        mFlagButton.setOnClickListener(view -> setBeenFlagged());

        itemView.setOnLongClickListener(view -> {
            PopupMenu menu = new PopupMenu(mContext, itemView);
            menu.getMenuInflater().inflate(R.menu.game_menu, menu.getMenu());
            menu.getMenu().findItem(R.id.invite_friend_to_game).setVisible(false);
            menu.getMenu().findItem(R.id.flag).setVisible(false);
            menu.getMenu().findItem(R.id.unflag).setVisible(false);

            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.create_game:
                        startCreateGame();
                        break;
                    case R.id.share:
                        FacebookShareProvider.shareToFacebook((AppCompatActivity) mContext, mImage);
                        break;
                    default:
                        break;
                }
                return true;
            });

            menu.show();
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
                            mImage, mContext.getString(R.string.shared_transition));
            mContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
        });
    }

    public void hasBeenUpvotedOrFlagged(boolean beenUpvoted, boolean beenFlagged) {
        isUpvoted = beenUpvoted;
        isFlagged = beenFlagged;

        if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
        }
        else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
        }

        if (isFlagged) {
            mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_black_24dp));
        }
        else {
            mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_grey_400_24dp));
        }
    }

    private void setBeenUpvoted() {
        if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
            isUpvoted = false;
        }
        else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
            isUpvoted = true;
        }
        upvoteGame(mGameId, isUpvoted);
    }

    private void setBeenFlagged() {
        if (isFlagged) {
            mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_grey_400_24dp));
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
                        mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_black_24dp));
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
                        mImage, mContext.getString(R.string.shared_transition));
        mContext.startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void upvoteGame(int gameId, boolean isUpvoted) {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, isUpvoted, GameAction.UPVOTE, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully upvoted game!")
                );
    }

    private void flagGame(int gameId, boolean isFlagged) {
        GameProvider.upvoteOrFlagGame(new GameAction(gameId, isFlagged, GameAction.FLAGGED, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> {
                            Timber.i("Successfully flagged game");
                        }
                );
    }
}
