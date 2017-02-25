package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.FacebookShareProvider;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.presentation.view.creategame.CreateGameActivity;
import com.snaptiongame.snaptionapp.presentation.view.game.GameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class SnaptionCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.top_caption)
    TextView mTopCaption;
    @BindView(R.id.captioner_image)
    CircleImageView mCaptionerImage;
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

    public SnaptionCardViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);

        if (Build.VERSION.SDK_INT >= 21) {
            mImage.setClipToOutline(true);
        }

        mUpvoteButton.setOnClickListener(view -> {
            if (isUpvoted) {
                mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border_grey_400_24dp));
                isUpvoted = false;
            }
            else {
                mUpvoteButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_400_24dp));
                isUpvoted = true;
                Toast.makeText(mContext, "Upvoted!", Toast.LENGTH_SHORT).show();
            }
            upvoteSnaption(mGameId, isUpvoted);
        });

        mFlagButton.setOnClickListener(view -> {
            if (isFlagged) {
                mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_grey_400_24dp));
                isFlagged = false;
            }
            else {
                mFlagButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_flag_black_24dp));
                isFlagged = true;
                Toast.makeText(mContext, "Flagged", Toast.LENGTH_SHORT).show();
            }
            flagSnaption(mGameId, isFlagged);
        });

        itemView.setOnLongClickListener(view -> {
            PopupMenu menu = new PopupMenu(mContext, itemView);
            menu.getMenuInflater().inflate(R.menu.game_menu, menu.getMenu());
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
            gameIntent.putExtra(Snaption.ID, mGameId);
            gameIntent.putExtra(Snaption.PICKER_ID, mPickerId);
            gameIntent.putExtra(Snaption.PICTURE, mImageUrl);

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                            mImage, mContext.getString(R.string.shared_transition));
            mContext.startActivity(gameIntent, transitionActivityOptions.toBundle());
        });
    }

    private void startCreateGame() {
        Intent createGameIntent = new Intent(mContext, CreateGameActivity.class);
        createGameIntent.putExtra(Snaption.PICTURE, mImageUrl);

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation((AppCompatActivity) mContext,
                        mImage, mContext.getString(R.string.shared_transition));
        mContext.startActivity(createGameIntent, transitionActivityOptions.toBundle());
    }

    private void upvoteSnaption(int gameId, boolean isUpvoted) {
        SnaptionProvider.upvoteOrFlagSnaption(new Like(gameId, isUpvoted, Like.UPVOTE, Like.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully liked snaption!")
                );
    }

    private void flagSnaption(int gameId, boolean isFlagged) {
        SnaptionProvider.upvoteOrFlagSnaption(new Like(gameId, isFlagged, Like.FLAGGED, Like.GAME_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully flagged snaption")
                );
    }
}
