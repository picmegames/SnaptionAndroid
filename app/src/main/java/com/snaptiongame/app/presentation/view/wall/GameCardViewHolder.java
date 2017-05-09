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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.snaptiongame.app.presentation.view.main.MainActivity;
import com.snaptiongame.app.presentation.view.utils.AnimUtils;
import com.snaptiongame.app.presentation.view.utils.ItemListener;

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
    @BindView(R.id.creator_name)
    TextView mCreatorName;
    @BindView(R.id.creator_content)
    LinearLayout mCreatorContent;
    @BindView(R.id.upvote)
    ImageView mUpvoteButton;
    @BindView(R.id.upvote_view)
    LinearLayout mUpvoteView;
    @BindView(R.id.number_of_upvotes)
    TextView mNumberOfUpvotes;
    @BindView(R.id.game_status)
    TextView mGameStatus;
    CircleImageView mCreatorImage;

    public Context mContext;
    public PopupMenu mMenu;
    public View mView;
    private ItemListener mListener;

    public int mGameId;
    public int mCreatorId;
    public String mCreator;
    public String mCreatorImageUrl;
    public String mImageUrl;
    public boolean isClosed;
    public boolean isPublic;

    public boolean isUpvoted = false;

    private static final int CREATOR_ALPHA = 128;

    public GameCardViewHolder(View itemView, ItemListener listener, boolean isList) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
        ButterKnife.bind(this, itemView);
        mListener = listener;

        if (isList) {
            mCreatorImage = ButterKnife.findById(itemView, R.id.creator_image);
        }
        else {
            mCreatorContent.getBackground().setAlpha(CREATOR_ALPHA);
        }

        mMenu = new PopupMenu(mContext, itemView);
        mMenu.getMenuInflater().inflate(R.menu.game_menu, mMenu.getMenu());
        mMenu.getMenu().findItem(R.id.invite_friend_to_game).setVisible(false);
        mMenu.getMenu().findItem(R.id.upvote).setVisible(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImage.setClipToOutline(true);
        }

        final ScaleAnimation growAnimation = AnimUtils.getGrowAnim();
        final ScaleAnimation shrinkAnimation = AnimUtils.getShrinkAnim();

        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            boolean upvoted;//Race Condition fix
            @Override
            public void onAnimationStart(Animation animation) {
                upvoted = isUpvoted;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setUpvoteIcon(upvoted);
                mUpvoteButton.startAnimation(growAnimation);
            }
        });

        mUpvoteView.setOnClickListener(view -> {
            if (AuthManager.isLoggedIn()) {
                mUpvoteButton.startAnimation(shrinkAnimation);
                upvoteGame();
            }
            else {
                goToLogin();
            }
        });

        itemView.setOnLongClickListener(view -> {
            mMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.create_game:
                        if (AuthManager.isLoggedIn()) {
                            startCreateGame();
                        }
                        else {
                            goToLogin();
                        }
                        break;
                    case R.id.share:
                        FacebookShareProvider.shareToFacebook((AppCompatActivity) mContext, mImage);
                        break;
                    case R.id.flag:
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
            gameIntent.putExtra(Game.CREATOR_NAME, mCreator);
            gameIntent.putExtra(Game.CREATOR_IMAGE, mCreatorImageUrl);
            gameIntent.putExtra(Game.CREATOR_ID, mCreatorId);
            gameIntent.putExtra(Game.IMAGE_URL, mImageUrl);
            gameIntent.putExtra(Game.BEEN_UPVOTED, isUpvoted);
            gameIntent.putExtra(Game.IS_CLOSED, isClosed);
            gameIntent.putExtra(Game.IS_PUBLIC, isPublic);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                            mImage, ViewCompat.getTransitionName(mImage));
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).setComingFromGameActivity(true);
            }
            mContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
        });
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(loginIntent);
    }

    public void hasBeenUpvotedOrFlagged(boolean beenUpvoted) {
        isUpvoted = beenUpvoted;

        if (isUpvoted) {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_pink_300_24dp));
        }
        else {
            mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_800_24dp));
        }
    }

    private void setBeenUpvoted() {
        if (isUpvoted) {
            isUpvoted = false;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) - 1));
        }
        else {
            isUpvoted = true;
            mNumberOfUpvotes.setText(String.valueOf(Integer.parseInt(mNumberOfUpvotes.getText().toString()) + 1));
            Toast.makeText(mContext, mContext.getString(R.string.upvoted), Toast.LENGTH_SHORT).show();
        }
        Timber.i("Successfully updated upvote!");
        mListener.updateUpvote(isUpvoted, getAdapterPosition());
    }

    private void setBeenFlagged() {
        new MaterialDialog.Builder(mContext)
                .title(R.string.flag_alert_game)
                .content(R.string.ask_flag_game)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) -> flagGame())
                .cancelable(true)
                .show();
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(mContext, CreateGameActivity.class);
        createGameIntent.putExtra(Game.IMAGE_URL, mImageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                        mImage, ViewCompat.getTransitionName(mImage));
        mContext.startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void upvoteGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(mGameId, !isUpvoted, GameAction.UPVOTE,
                GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setBeenUpvoted,
                        Timber::e
                );
    }

    private void flagGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(mGameId, true, GameAction.FLAGGED, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mListener.updateFlag(getAdapterPosition(), this),
                        Timber::e
                );
    }

    public void unflagGame() {
        GameProvider.upvoteOrFlagGame(new GameAction(mGameId, false, GameAction.FLAGGED, GameAction.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        Timber::e
                );
    }

    private void setUpvoteIcon(boolean isGameUpvoted) {
        if (isGameUpvoted) {
            mUpvoteButton.setImageResource(R.drawable.ic_favorite_border_grey_800_24dp);
        }
        else {
            mUpvoteButton.setImageResource(R.drawable.ic_favorite_pink_300_24dp);
        }
    }
}
